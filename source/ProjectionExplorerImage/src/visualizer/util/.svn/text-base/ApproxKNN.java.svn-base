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

package visualizer.util;

import visualizer.datamining.clustering.BKmeans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ApproxKNN {

    /** Creates a new instance of ApproxKNN
     * @param nrNeighbors
     */
    public ApproxKNN(int nrNeighbors) {
        this.nrNeighbors = nrNeighbors;
    }

    public Pair[][] execute(Matrix matrix, Dissimilarity diss) throws IOException {
        BKmeans bkmeans = new BKmeans((int) Math.pow(matrix.getRowCount(), 0.5f));
        ArrayList<ArrayList<Integer>> clusters = bkmeans.execute(diss, matrix);
        Matrix centroids = bkmeans.getCentroids();

        return this.execute(matrix, diss, clusters, centroids);
    }

    public Pair[][] execute(Matrix matrix, Dissimilarity diss,
            ArrayList<ArrayList<Integer>> clusters, Matrix centroids) throws IOException {
        long start = System.currentTimeMillis();

        if (this.nrNeighbors > matrix.getRowCount() - 1) {
            throw new IOException("Number of neighbors bigger than the number of " +
                    "elements minus one (an element is not computed as a neighbor " +
                    "of itself)!");
        }

        Pair[][] neighbors = null;

        //init the neighbors list
        neighbors = new Pair[matrix.getRowCount()][];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = new Pair[this.nrNeighbors];

            for (int j = 0; j < neighbors[i].length; j++) {
                neighbors[i][j] = new Pair(-1, Float.MAX_VALUE);
            }
        }

        //defining the number of clusters to visit
        int nrClustersNeighbors = Math.max(5, (int) Math.sqrt(Math.sqrt(matrix.getRowCount())) * 3);
        nrClustersNeighbors = Math.min(nrClustersNeighbors, clusters.size() - 1);

        //calculating the nearest neighbors of the clusters
        DistanceMatrix dmat_clusters = new DistanceMatrix(centroids, diss);
        KNN clustersKNN = new KNN(dmat_clusters.getElementCount() - 1);
        Pair[][] clustersNeighbors = clustersKNN.execute(dmat_clusters);

        //for each cluster
        for (int c = 0; c < clusters.size(); c++) {
            //for each element on the same cluster
            for (int e = 0; e < clusters.get(c).size(); e++) {
                int el = clusters.get(c).get(e);

                //check inside the same cluster
                for (int j = 0; j < clusters.get(c).size(); j++) {
                    int other = clusters.get(c).get(j);

                    if (el == other) {
                        continue;
                    }

                    float dist = diss.calculate(matrix.getRow(el), matrix.getRow(other));

                    if (dist < neighbors[el][neighbors[el].length - 1].value) {
                        for (int k = 0; k < neighbors[el].length; k++) {
                            if (neighbors[el][k].value > dist) {
                                for (int n = neighbors[el].length - 1; n > k; n--) {
                                    neighbors[el][n].index = neighbors[el][n - 1].index;
                                    neighbors[el][n].value = neighbors[el][n - 1].value;
                                }

                                neighbors[el][k].index = other;
                                neighbors[el][k].value = dist;
                                break;
                            }
                        }
                    }
                }

                //defining how many clusters to visit
                int nrtovisit = 1;
                int count = clusters.get(c).size() - 1;

                for (int cn = 0; cn < clustersNeighbors[c].length; cn++) {
                    count += clusters.get(clustersNeighbors[c][cn].index).size();

                    if (count > this.nrNeighbors) {                        
                        break;
                    } else {
                        nrtovisit++;
                    }
                }

                nrtovisit = (nrtovisit > nrClustersNeighbors) ? nrtovisit : nrClustersNeighbors;

                //check inside the nearest clusters
                //for each neighbor cluster
                for (int cn = 0; cn < nrtovisit; cn++) {
                    int clneighbor = clustersNeighbors[c][cn].index;

                    for (int j = 0; j < clusters.get(clneighbor).size(); j++) {
                        int other = clusters.get(clneighbor).get(j);

                        if (el == other) {
                            continue;
                        }

                        float dist = diss.calculate(matrix.getRow(el), matrix.getRow(other));

                        if (dist < neighbors[el][neighbors[el].length - 1].value) {
                            for (int k = 0; k < neighbors[el].length; k++) {
                                if (neighbors[el][k].value > dist) {
                                    for (int n = neighbors[el].length - 1; n > k; n--) {
                                        neighbors[el][n].index = neighbors[el][n - 1].index;
                                        neighbors[el][n].value = neighbors[el][n - 1].value;
                                    }

                                    neighbors[el][k].index = other;
                                    neighbors[el][k].value = dist;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        long finish = System.currentTimeMillis();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Approximated KNN time: " + (finish - start) / 1000.0f + "s");

        return neighbors;
    }

    private int nrNeighbors = 5;
}
