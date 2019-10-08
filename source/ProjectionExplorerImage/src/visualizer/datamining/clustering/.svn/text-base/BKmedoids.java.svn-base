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

import java.io.IOException;
import java.util.ArrayList;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class BKmedoids extends Clustering {

    /** Creates a new instance of BKmedoids
     * @param nrclusters 
     */
    public BKmedoids(int nrclusters) {
        super(nrclusters);
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(Dissimilarity diss, Matrix matrix) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
        return this.execute(dmat);
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(DistanceMatrix dmat) throws IOException {
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
        clusters.add(new ArrayList<Integer>());

        //Create a single cluster with all points
        for (int i = 0; i < dmat.getElementCount(); i++) {
            clusters.get(0).add(i);
        }

        //Choose a point as the initial centroid
        this.medoids = new ArrayList<Integer>();
        this.medoids.add(0);

        int numberIterations = 0;
        while (numberIterations < this.nrclusters * 5 && clusters.size() < this.nrclusters) {
            //Search the gratest cluster (number of points)
            int greatestCluster = 0;
            int greatestElements = clusters.get(0).size();

            for (int k = 1; k < clusters.size(); k++) {
                if (clusters.get(k).size() > greatestElements) {
                    greatestCluster = k;
                    greatestElements = clusters.get(k).size();
                }
            }

            //Split the greatest cluster
            this.splitCluster(greatestCluster, clusters, dmat);

            //Eliminate empty clusters
            this.eliminateEmptyClusters(clusters);

            //Search the smallest cluster (number of points)
            int smallestCluster = 0;
            int smallestElements = clusters.get(0).size();

            for (int k = 1; k < clusters.size(); k++) {
                if (clusters.get(k).size() < smallestElements) {
                    smallestCluster = k;
                    smallestElements = clusters.get(k).size();
                }
            }

            //Join the smallest cluster
            this.joinCluster(smallestCluster, clusters, dmat);

            numberIterations++;
        }

        return clusters;
    }

    private void eliminateEmptyClusters(ArrayList<ArrayList<Integer>> clusters) {
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).size() == 0) {
                clusters.remove(i);
                this.medoids.remove(i--);
            }
        }
    }

    private void splitCluster(int cluster, ArrayList<ArrayList<Integer>> clusters, DistanceMatrix dmat) {
        //Copy the cluster which will be split
        ArrayList<Integer> oldCluster = clusters.get(cluster);

        //Remove the cluster and its centroid
        clusters.remove(cluster);
        medoids.remove(cluster);

        //Choose two pivots
        int[] pivots = getPivots(oldCluster);

        //Create two new clusters and its centroids as these pivots
        ArrayList<Integer> cluster1 = new ArrayList<Integer>();
        int medoids1 = pivots[0];

        ArrayList<Integer> cluster2 = new ArrayList<Integer>();
        int medoids2 = pivots[1];

        //For each cluster
        for (int i = 0; i < oldCluster.size(); i++) {
            float distanceCentroid1 = dmat.getDistance(oldCluster.get(i), medoids1);
            float distanceCentroid2 = dmat.getDistance(oldCluster.get(i), medoids2);

            if (distanceCentroid1 < distanceCentroid2) {
                cluster1.add(oldCluster.get(i));
            } else if (distanceCentroid1 > distanceCentroid2) {
                cluster2.add(oldCluster.get(i));
            } else {
                if (cluster1.size() < cluster2.size()) {
                    cluster1.add(oldCluster.get(i));
                } else {
                    cluster2.add(oldCluster.get(i));
                }
            }
        }

        //Add the two new clusters
        clusters.add(cluster1);
        clusters.add(cluster2);

        //And its centroids
        this.medoids.add(medoids1);
        this.medoids.add(medoids2);

        //Update the medoids
        this.updateMedoids(dmat, clusters);
    }

    private void updateMedoids(DistanceMatrix dmat, ArrayList<ArrayList<Integer>> clusters) {
        //Para cada cluster
        for (int cluster = 0; cluster < clusters.size(); cluster++) {
            int medoid = clusters.get(cluster).get(0);
            float sumDistances = dmat.getMaxDistance();

            //para cada ponto do cluster
            for (int point = 0; point < clusters.get(cluster).size(); point++) {
                float sumDistances2 = 0.0f;

                //Encontrar a média da distância desse ponto para todos os outros pontos
                for (int point2 = 0; point2 < clusters.get(cluster).size(); point2++) {
                    sumDistances2 += dmat.getDistance(clusters.get(cluster).get(point), clusters.get(cluster).get(point2));
                }
                sumDistances2 /= clusters.get(cluster).size();

                //Assinalar como medóide o ponto que minimiza a distância média
                if (sumDistances > sumDistances2) {
                    sumDistances = sumDistances2;
                    medoid = clusters.get(cluster).get(point);
                }
            }
            medoids.set(cluster, medoid);
        }
    }

    private void joinCluster(int cluster, ArrayList<ArrayList<Integer>> clusters, DistanceMatrix dmat) {
        if (clusters.get(cluster).size() < 5) {

            //Find the nearest cluster
            int nearest = 0;
            float distance1 = dmat.getDistance(this.medoids.get(cluster), this.medoids.get(0));
            for (int j = 0; j < clusters.size(); j++) {
                if (cluster != j) {
                    float distance2 = dmat.getDistance(this.medoids.get(cluster), this.medoids.get(j));
                    if (distance1 > distance2) {
                        distance1 = distance2;
                        nearest = j;
                    }
                }
            }

            //Update the clusters
            for (int j = 0; j < clusters.get(cluster).size(); j++) {
                clusters.get(nearest).add(clusters.get(cluster).get(j));
            }

            //Remove cluster
            clusters.remove(cluster);

            //Remove centroid
            this.medoids.remove(cluster);

            //Update the medoids
            this.updateMedoids(dmat, clusters);
        }
    }

    private int[] getPivots(ArrayList<Integer> cluster) {
        int[] pivots = new int[2];
        pivots[0] = cluster.get(0);
        pivots[1] = cluster.get(cluster.size() - 1);
        return pivots;
    }

    public int[] getMedoids() {
        int[] c = new int[this.medoids.size()];
        for (int i = 0; i < this.medoids.size(); i++) {
            c[i] = this.medoids.get(i);
        }
        return c;
    }

    private ArrayList<Integer> medoids;
}
