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
public class Kmedoids extends Clustering {

    public Kmedoids(int nrclusters) {
        super(nrclusters);
        this.medoids = new int[nrclusters];
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(Dissimilarity diss, Matrix matrix) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
        return this.execute(dmat);
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(DistanceMatrix dmat) throws IOException {
        //It will store the clusters
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < this.nrclusters; i++) {
            clusters.add(new ArrayList<Integer>());
        }

        //Variável auxiliar para armazenar as medóides antigas
        int[] oldMedoids = new int[nrclusters];

        //inicializar as medoides com alguns pontos
        for (int i = 0; i < nrclusters; i++) {
            medoids[i] = i * (dmat.getElementCount() / nrclusters);
        }

        //indica se as medóides foram modificadas
        boolean medoidModified = true;

        //serve para executar o algoritmo até um número limitado de vazes
        int nroIterations = 0;

        while (medoidModified && nroIterations < dmat.getElementCount()) {
            //colocar os pontos dentro dos seus respectivos clusters

            int numberElements = Integer.MAX_VALUE;

            for (int point = 0; point < dmat.getElementCount(); point++) {
                int nearestCluster = 0; //armazenará a cluster que o ponto pertencerá
                float distance = dmat.getDistance(point, medoids[nearestCluster]);

                //verificar qual cluster o ponto está mais próximo (com base na medóide)
                for (int cluster = 1; cluster < clusters.size(); cluster++) {
                    float distance2 = dmat.getDistance(point, medoids[cluster]);

                    if (distance > distance2) {
                        nearestCluster = cluster;
                        distance = distance2;
                    } else if (distance == distance2) {
                        if (numberElements > clusters.get(cluster).size()) {
                            numberElements = clusters.get(cluster).size() + 1;
                            nearestCluster = cluster;
                            distance = distance2;
                        }
                    }
                }

                clusters.get(nearestCluster).add(point);
            }

            //Armazena os valores antigos das medóides
            oldMedoids = medoids;

            //Atualiza as medóides
            this.updateMedoids(dmat, clusters);

            //Se não houve modificação, para o looping
            medoidModified = isMedoidModified(oldMedoids);

            //Incrementa a quantidade de vezes que foram realocados os pontos
            nroIterations++;
        }

        return clusters;
    }

    public int[] getMedoids() {
        return this.medoids;
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

            medoids[cluster] = medoid;
        }
    }

    private boolean isMedoidModified(int[] oldMedoids) {
        for (int medoid = 0; medoid < oldMedoids.length; medoid++) {
            if (oldMedoids[medoid] != this.medoids[medoid]) {
                return true;
            }
        }
        return false;
    }

    private int[] medoids;
}
