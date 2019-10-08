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
public class HierarchicalClustering extends Clustering {

    /** Creates a new instance of HierarchicalClustering
     * @param nrclusters
     * @param type 
     */
    public HierarchicalClustering(int nrclusters, HierarchicalClusteringType type) {
        super(nrclusters);
        this.type = type;
    }

    public HierarchicalClustering(HierarchicalClusteringType type) {
        super(0);
        this.type = type;
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(Dissimilarity diss, Matrix matrix) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
        return this.execute(dmat);
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(DistanceMatrix dmat) throws IOException {
        this.init(dmat);

        //Inicialmente todos os pontos devem ser clusters unitários
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < this.distances.size(); i++) {
            clusters.add(new ArrayList<Integer>());
            clusters.get(i).add(i);
        }

        //Forma os clusters até alcançar o número pré-determinado
        while (clusters.size() > nrclusters) {
            int[] cmin = this.joinNearestClusters(clusters);

            //Faço o update da matriz de distâncias
            this.updateDistanceMatrix(cmin);
        }

        return clusters;
    }

    public float[] getPointsHeight(Matrix matrix, Dissimilarity diss) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
        this.init(dmat);

        //Inicialmente todos os pontos devem ser clusters unitários
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
        float[] height = new float[this.distances.size()];

        for (int i = 0; i < this.distances.size(); i++) {
            clusters.add(new ArrayList<Integer>());
            clusters.get(i).add(i);
            height[i] = 0.0f;
        }

        //Forma os clusters até alcançar o número pré-determinado
        while (clusters.size() > 1) {
            int[] cmin = this.joinNearestClusters(clusters);

            //Armazeno a distância entre os clusters
            for (int i = 0; i < clusters.get(cmin[0]).size(); i++) {
                height[clusters.get(cmin[0]).get(i)] += 1;
            }

            //Faço o update da matriz de distâncias
            this.updateDistanceMatrix(cmin);
        }

        return height;
    }

    private void init(DistanceMatrix dmat) throws IOException {
        //Cria a matriz de distâncias
        for (int i = 0; i < dmat.getElementCount(); i++) {
            ArrayList<Float> lin = new ArrayList<Float>();
            for (int j = 0; j < dmat.getElementCount() - 1; j++) {
                lin.add(0.0f);
            }

            this.distances.add(lin);
        }

        //Calcula e preenche a matriz com as distâncias
        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int j = 0; j < dmat.getElementCount(); j++) {
                if (i == j) {
                    this.distances.get(i).add(j, 0.0f);
                } else {
                    float distance = dmat.getDistance(i, j);
                    this.distances.get(i).set(j, distance);
                    this.distances.get(j).set(i, distance);
                }
            }
        }
    }

    private int[] joinNearestClusters(ArrayList<ArrayList<Integer>> clusters) {
        float minDistance = distances.get(0).get(1);
        int[] cmin = new int[2];
        cmin[0] = 0;
        cmin[1] = 1;

        //Procurar os clusters mais próximos c1 e c2
        for (int c1 = 0; c1 < clusters.size(); c1++) {
            for (int c2 = c1 + 1; c2 < clusters.size(); c2++) {
                if (minDistance > distances.get(c1).get(c2)) {
                    minDistance = distances.get(c1).get(c2);
                    cmin[0] = c1;
                    cmin[1] = c2;
                }
            }
        }

        //Agrupe os dois clusters mais próximos: c1min e c2min
        //coloco dentro de c1min o c2min
        for (int i = 0; i < clusters.get(cmin[1]).size(); i++) {
            clusters.get(cmin[0]).add(clusters.get(cmin[1]).get(i));
        }

        //apago c2min
        clusters.remove(cmin[1]);

        return cmin;
    }

    private void updateDistanceMatrix(int[] cmin) {
        if (this.type == HierarchicalClusteringType.SLINK) {
            //Linha: copia a menor/maior distância entre os clusters c1min e c2min para o cluster c1min
            for (int i = 0; i < distances.get(0).size(); i++) {
                //distances[c1min][i] = (distances[c1min][i] < distances[c2min][i]) ? distances[c1min][i] : distances[c2min][i]; //single-link
                if (distances.get(cmin[0]).get(i) < distances.get(cmin[1]).get(i)) {
                    distances.get(cmin[0]).set(i, distances.get(cmin[0]).get(i));
                } else {
                    distances.get(cmin[0]).set(i, distances.get(cmin[1]).get(i));
                }
                //distances[i][c1min] = (distances[i][c1min] < distances[i][c2min]) ? distances[i][c1min] : distances[i][c2min]; //single-link
                if (distances.get(i).get(cmin[0]) < distances.get(i).get(cmin[1])) {
                    distances.get(i).set(cmin[0], distances.get(i).get(cmin[0]));
                } else {
                    distances.get(i).set(cmin[0], distances.get(i).get(cmin[1]));
                }
            }
        } else if (this.type == HierarchicalClusteringType.CLINK) {
            //Linha: copia a menor/maior distância entre os clusters c1min e c2min para o cluster c1min
            for (int i = 0; i < distances.get(0).size(); i++) {
                //distances[c1min][i] = (distances[c1min][i] > distances[c2min][i]) ? distances[c1min][i] : distances[c2min][i]; //complete-link
                if (distances.get(cmin[0]).get(i) > distances.get(cmin[1]).get(i)) {
                    distances.get(cmin[0]).set(i, distances.get(cmin[0]).get(i));
                } else {
                    distances.get(cmin[0]).set(i, distances.get(cmin[1]).get(i));
                }

                //distances[i][c1min] = (distances[i][c1min] > distances[i][c2min]) ? distances[i][c1min] : distances[i][c2min]; //complete-link
                if (distances.get(i).get(cmin[0]) > distances.get(i).get(cmin[1])) {
                    distances.get(i).set(cmin[0], distances.get(i).get(cmin[0]));
                } else {
                    distances.get(i).set(cmin[0], distances.get(i).get(cmin[1]));
                }
            }
        } else if (this.type == HierarchicalClusteringType.ALINK) {
            //Linha: copia a menor/maior distância entre os clusters c1min e c2min para o cluster c1min
            for (int i = 0; i < distances.get(0).size(); i++) {
                //distances[c1min][i] = (distances[c1min][i]*2 + distances[c2min][i]) / 2; //average-link
                distances.get(cmin[0]).set(i, (distances.get(cmin[0]).get(i) * 2 + distances.get(cmin[1]).get(i)) / 2);

                //distances[i][c1min] = (distances[i][c1min]*2 + distances[i][c2min]) / 2; //average-link
                distances.get(i).set(cmin[0], (distances.get(i).get(cmin[0]) * 2 + distances.get(i).get(cmin[1])) / 2);
            }
        }

        //Apago a linha das distâncias do cluster c2min
        distances.remove(cmin[1]);

        //Apago a coluna das distâncias do cluster c2min
        for (int k = 0; k < distances.size(); k++) {
            distances.get(k).remove(cmin[1]);
        }
    }

    private HierarchicalClusteringType type = HierarchicalClusteringType.CLINK;
    private ArrayList<ArrayList<Float>> distances = new ArrayList<ArrayList<Float>>();
}
