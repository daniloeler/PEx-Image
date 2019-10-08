/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
@inproceedings{paulovich2007pex,
author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
Minghim},
title = {The Projection Explorer: A Flexible Tool for Projection-based 
Multidimensional Visualization},
booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
Computer Graphics and Image Processing (SIBGRAPI 2007)},
year = {2007},
isbn = {0-7695-2996-8},
pages = {27--34},
doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
publisher = {IEEE Computer Society},
address = {Washington, DC, USA},
}
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.datamining.clustering;

import visualizer.projection.distance.Dissimilarity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Euclidean;
import visualizer.util.KNN;
import visualizer.util.Pair;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Jdbscan2D extends Clustering {

    public Jdbscan2D() {
        super(0);
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(Dissimilarity diss, Matrix matrix) throws IOException {
        if (diss instanceof Euclidean) {
            DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
            return this.execute(dmat);
        } else {
            throw new IOException("The dissimilarity should be Euclidean!");
        }
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(DistanceMatrix dmat) throws IOException {
        float eps = this.calculateEps(dmat);

        //fill the points list
        ArrayList<Point> points_aux = new ArrayList<Point>();

        for (int i = 0; i < dmat.getElementCount(); i++) {
            points_aux.add(new Point(i));
        }

        this.dbscan(dmat, points_aux, eps, 7);

        nrclusters = 0;
        for (Point p : points_aux) {
            if (p.clusterId > nrclusters) {
                nrclusters = p.clusterId;
            }
        }

        //creating the clusters
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < nrclusters + 1; i++) {
            clusters.add(new ArrayList<Integer>());
        }

        for (Point p : points_aux) {
            if (p.clusterId > -1) {
                clusters.get(p.clusterId).add(p.id);
            }
        }

        return clusters;
    }

    private float calculateEps(DistanceMatrix dmat) throws IOException {
        float[] max_ray = new float[dmat.getElementCount()];

        KNN knn = new KNN(nrNeighbors);
        Pair[][] neighbors = knn.execute(dmat);

        for (int p = 0; p < neighbors.length; p++) {
            float max_dist = Float.MIN_VALUE;
            for (int n = 0; n < neighbors[p].length; n++) {
                if (neighbors[p][n].value > max_dist) {
                    max_dist = neighbors[p][n].value;
                }
            }
            max_ray[p] = max_dist;
        }

        Arrays.sort(max_ray);

        return max_ray[(int) (dmat.getElementCount() * 0.85f)];
    }

    private void dbscan(DistanceMatrix dmat, ArrayList<Point> points, float eps, int minPts) {
        int clusterId = nextId(Point.NOISE);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);

            if (p.clusterId == Point.UNCLASSIFIED) {
                if (expandCluster(dmat, points, p, clusterId, eps, minPts)) {
                    clusterId = nextId(clusterId);
                }
            }
        }
    }

    private boolean expandCluster(DistanceMatrix dmat, ArrayList<Point> points,
            Point p, int clusterId, float eps, int minPts) {
        ArrayList<Point> seeds = regionQuery(dmat, points, p, eps);

        if (seeds.size() < minPts) {
            changeClusterId(p, Point.NOISE);
            return false;
        } else {
            changeClusterIds(seeds, clusterId);
            seeds.remove(p);

            while (seeds.size() > 0) {
                Point currentP = seeds.get(0);
                ArrayList<Point> result = regionQuery(dmat, points, currentP, eps);

                if (result.size() >= minPts) {
                    for (int i = 0; i < result.size(); i++) {
                        Point resultP = result.get(i);

                        if (resultP.clusterId == Point.UNCLASSIFIED || resultP.clusterId == Point.NOISE) {
                            if (resultP.clusterId == Point.UNCLASSIFIED) {
                                seeds.add(resultP);
                            }

                            changeClusterId(resultP, clusterId);
                        }
                    }
                }
                seeds.remove(currentP);
            }
            return true;
        }
    }

    private ArrayList<Point> regionQuery(DistanceMatrix dmat, ArrayList<Point> points,
            Point p, float eps) {
        ArrayList<Point> query = new ArrayList<Point>();

        for (int i = 0; i < points.size(); i++) {
            if (dmat.getDistance(p.id, points.get(i).id) <= eps) {
                query.add(points.get(i));
            }
        }

        return query;
    }

    private void changeClusterId(Point p, int id) {
        p.clusterId = id;
    }

    private void changeClusterIds(ArrayList<Point> points, int id) {
        for (Point p : points) {
            p.clusterId = id;
        }
    }

    private int nextId(int id) {
        return ++id;
    }

    class Point {

        public static final int UNCLASSIFIED = -2;
        public static final int NOISE = -1;
        public Point(int id) {
            this.id = id;
        }

        public int id = 0;
        public int clusterId = Point.UNCLASSIFIED;
    }

    private int nrNeighbors = 4;
}
