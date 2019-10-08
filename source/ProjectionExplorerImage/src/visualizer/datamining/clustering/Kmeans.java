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
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.matrix.Vector;
import visualizer.matrix.MatrixUtils;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Kmeans extends Clustering {

    public Kmeans(int nrclusters) {
        super(nrclusters);
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(Dissimilarity diss, Matrix matrix) throws IOException {
        try {
            //Variável auxiliar para armazenar as centróides antigas
            Matrix oldCentroids;

            //indica se as centróides foram modificadas
            boolean isCentroidModified = true;

            //serve para executar o algoritmo até um número limitado de vazes
            int numberIterations = 0;

            //inicializar as centróides com alguns pontos
            this.centroids = MatrixFactory.getInstance(matrix.getClass());
            for (int i = 0; i < this.nrclusters; i++) {
                this.centroids.addRow((Vector) matrix.getRow(i).clone());
            }

            while (isCentroidModified && numberIterations < 30) {
                this.clusters.clear();
                for (int i = 0; i < this.nrclusters; i++) {
                    this.clusters.add(new ArrayList<Integer>());
                }

                //colocar os pontos dentro dos seus respectivos clusters
                for (int point = 0; point < matrix.getRowCount(); point++) {
                    int nearestCentroid = 0; //armazenará a centróide que o ponto pertencerá
                    float distance1 = diss.calculate(matrix.getRow(point),
                            this.centroids.getRow(nearestCentroid));

                    //para cada cluster existente, verificar qual centróide o ponto
                    //está mais próximo
                    for (int cluster = 1; cluster < this.nrclusters; cluster++) {
                        float distance2 = diss.calculate(matrix.getRow(point),
                                this.centroids.getRow(cluster));

                        if (distance1 > distance2) {
                            nearestCentroid = cluster;
                            distance1 = distance2;
                        }
                    }

                    //colocar o ponto no cluster apropriado
                    this.clusters.get(nearestCentroid).add(point);
                }

                //verifica se algum cluster ficou vazio
                for (int i = 0; i < this.clusters.size(); i++) {
                    //se o cluster for vazio, copia aleatóriamente algum ponto como
                    //centróide
                    if (this.clusters.get(i).size() == 0) {
                        int index = (int) (Math.random() * (matrix.getRowCount() - 1));
                        this.centroids.setRow(i, (Vector) matrix.getRow(index).clone());
                    }
                }

                //Armazena os valores antigos das centróides
                oldCentroids = this.centroids;

                //Atualiza as centróides
                this.updateCentroids(matrix);

                //Se não houve modificação, para o looping
                isCentroidModified = isCentroidModified(oldCentroids);

                //Incrementa a quantidade de vezes que foram realocados os pontos
                numberIterations++;
            }

            //eliminate empty clusters        
            for (int i = this.clusters.size() - 1; i >= 0; i--) {
                if (this.clusters.get(i).size() == 0) {
                    this.clusters.remove(i);
                    this.centroids.removeRow(i);
                }
            }

            return this.clusters;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Kmeans.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public ArrayList<ArrayList<Integer>> execute(DistanceMatrix dmat) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Matrix getCentroids() {
        return this.centroids;
    }

    public int[] getMedoids(Matrix matrix, Dissimilarity diss) throws IOException {
        int[] m = new int[this.centroids.getRowCount()];

        for (int i = 0; i < m.length; i++) {
            int point = -1;
            float distance = Float.MAX_VALUE;

            for (int j = 0; j < this.clusters.get(i).size(); j++) {
                float distance2 = diss.calculate(this.centroids.getRow(i),
                        matrix.getRow(this.clusters.get(i).get(j)));
                if (distance > distance2) {
                    point = this.clusters.get(i).get(j);
                    distance = distance2;
                }
            }
            m[i] = point;
        }

        return m;
    }

    private void updateCentroids(Matrix matrix) throws IOException {
        //zerar as centroids
        this.centroids = MatrixFactory.getInstance(matrix.getClass());

        //para cada cluster
        for (int cluster = 0; cluster < this.nrclusters; cluster++) {
            Matrix vectors = MatrixFactory.getInstance(matrix.getClass());

            //para cada ponto dentro do cluster
            for (int el = 0; el < clusters.get(cluster).size(); el++) {
                vectors.addRow(matrix.getRow(clusters.get(cluster).get(el)));
            }

            Vector centroid = MatrixUtils.mean(vectors);
            this.centroids.addRow(centroid);
        }
    }

    private boolean isCentroidModified(Matrix oldCentroids) {
        for (int centroid = 0; centroid < oldCentroids.getRowCount(); centroid++) {
            if (!oldCentroids.getRow(centroid).equals(this.centroids.getRow(centroid))) {
                return false;
            }
        }

        return false;
    }

    private ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
    private Matrix centroids;
}
