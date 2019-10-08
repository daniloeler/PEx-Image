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

package visualizer.projection.projclus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.ForceScheme;
import visualizer.projection.Projection;
import visualizer.projection.ProjectionData;
import visualizer.projection.Projector;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.Euclidean;
import visualizer.util.KNN;
import visualizer.projection.ProjectorFactory;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.datamining.clustering.BKmeans;
import visualizer.datamining.clustering.Kmedoids;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ProjClusProjection extends Projection {

    @Override
    public float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view) {
        long start = System.currentTimeMillis();
        
        float[][] projection = null;

        try {
            //defining the distance diss to be used
            diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());

            ArrayList<float[][]> projections = new ArrayList<float[][]>();

            if (view != null) {
                view.setStatus("Creating the clusters...", 35);
            }

            BKmeans bkmeans = new BKmeans((int) Math.sqrt(matrix.getRowCount()));
            this.clusters = bkmeans.execute(this.diss, matrix);
            Matrix centroids = bkmeans.getCentroids();

            //Positioning the pivots (centroids)
            if (view != null) {
                view.setStatus("Positioning the pivots...", 60);
            }

            float[][] pivotsProjection = this.createPivotsProjection(centroids, pdata);

            //For each cluster
            if (view != null) {
                view.setStatus("Projecting the clusters...", 65);
            }

            for (int cluster = 0; cluster < clusters.size(); cluster++) {
                if (view != null) {
                    view.setStatus("Projecting the clusters...", 65 + cluster / 5);
                }

                //Create a distance matrix
                DistanceMatrix dmat_aux = this.createDistanceMatrix(matrix, clusters.get(cluster));

                //Based on the projection type, create the projection using JAVA REFLECTION
                Projector proj = ProjectorFactory.getInstance(pdata.getProjectorType());
                float[][] projection_aux = proj.project(dmat_aux);

                //Add the new projection to the list of projections
                projections.add(projection_aux);

                //Improve the projection
                if (projection_aux != null && dmat_aux.getElementCount() > 3) {
                    ForceScheme force = new ForceScheme(pdata.getFractionDelta(), projection_aux.length);
                    for (int i = 0; i < pdata.getNumberIterations(); i++) {
                        force.iteration(dmat_aux, projection_aux);
                    }
                }
            }

            if (view != null) {
                view.setStatus("Assembling the final projection...", 85);
            }

            projection = new float[matrix.getRowCount()][];

            //Armazena as maiores distancias ate o centroid
            float[] centroidMaxDistances = new float[clusters.size()];
            float overallMaxDistance = Float.MIN_VALUE;
            Arrays.fill(centroidMaxDistances, Float.MIN_VALUE);

            //Encontar as maiores distancias
            for (int i = 0; i < clusters.size(); i++) {
                if (view != null) {
                    view.setStatus("Assembling the final projection...", 85 + i / 10);
                }
                for (int j = 0; j < clusters.get(i).size(); j++) {
                    float distance = diss.calculate(centroids.getRow(i),
                            matrix.getRow(clusters.get(i).get(j)));
                    if (distance > centroidMaxDistances[i]) {
                        centroidMaxDistances[i] = distance;
                    }
                    //Store the overall max distance
                    if (distance > overallMaxDistance) {
                        overallMaxDistance = distance;
                    }
                }
            }

            for (int i = 0; i < clusters.size(); i++) {
                float[][] p = projections.get(i);
                this.normalize2D(p, 0, centroidMaxDistances[i] / overallMaxDistance);

                //////////////////////////////////////////////////////////////////////
                //  1.2. somar a essas projecoes normalizadas as coordenadas X e Y
                //       dos centroides dos seus agrupamentos
                for (int j = 0; j < p.length; j++) {
                    p[j][0] = p[j][0] + (pivotsProjection[i][0] * pdata.getClusterFactor());
                    p[j][1] = p[j][1] + (pivotsProjection[i][1] * pdata.getClusterFactor());
                }
                //////////////////////////////////////////////////////////////////////

                //2. remontar a projecao final
                for (int j = 0; j < p.length; j++) {
                    projection[clusters.get(i).get(j)] = p[j];
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjClusProjection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        long finish = System.currentTimeMillis();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Time to project using ProjClus: " + (finish - start) / 1000.0f + "s");

        return projection;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        try {
            this.dmat = dmat;

            KNN knn = new KNN(pdata.getKnnNumberNeighbors());
            this.knnneighbors = knn.execute(dmat);

            //The clusters projections
            ArrayList<float[][]> projections = new ArrayList<float[][]>();

            if (view != null) {
                view.setStatus("Creating the clusters...", 55);
            }
            Kmedoids km = new Kmedoids((int) Math.sqrt(dmat.getElementCount()));
            this.clusters = km.execute(dmat);
            int[] medoids = km.getMedoids();

            //////////////////////////////////////////////////////////////////////////////////
            ArrayList<Integer> medoids_aux = new ArrayList<Integer>();
            for (int i = 0; i < medoids.length; i++) {
                medoids_aux.add(medoids[i]);
            }

            //Desfazer os agrupamentos pequenos, juntando-os ao seu agrupamento mais proximo
            for (int cluster = 0; cluster < clusters.size(); cluster++) {
                //se um cluster tiver menos de 4 elementos
                if (clusters.get(cluster).size() < 4) {
                    //encontrar o medoid mais proximo do mesmo e unir os dois clusters
                    int nearestMedoid = 0;
                    float distance = dmat.getDistance(medoids_aux.get(cluster), medoids_aux.get(nearestMedoid));
                    for (int m = 1; m < medoids_aux.size(); m++) {
                        if (cluster != m) {
                            float distance2 = dmat.getDistance(medoids_aux.get(cluster), medoids_aux.get(m));
                            if (distance2 < distance) {
                                distance = distance2;
                                nearestMedoid = m;
                            }
                        }
                    }

                    //unir os dois medoids
                    for (int i = 0; i < clusters.get(cluster).size(); i++) {
                        clusters.get(nearestMedoid).add(clusters.get(cluster).get(i));
                    }

                    //remover o cluster com poucos elementos
                    clusters.remove(cluster);
                    medoids_aux.remove(cluster);
                }
            }
            medoids = new int[medoids_aux.size()];
            for (int i = 0; i < medoids_aux.size(); i++) {
                medoids[i] = medoids_aux.get(i);
            }

            //Positioning the pivots (centroids)
            if (view != null) {
                view.setStatus("Positioning the pivots...", 60);
            }
            float[][] pivotsProjection = this.createPivotsProjection(dmat, medoids, pdata);


            if (view != null) {
                view.setStatus("Projecting the clusters...", 65);
            }
            for (int cluster = 0; cluster < clusters.size(); cluster++) {
                if (view != null) {
                    view.setStatus("Projecting the clusters...", 65 + cluster / 5);
                }

                //Create a distance matrix
                DistanceMatrix dmat_c = this.createDistanceMatrix(dmat, clusters.get(cluster));

                //Based on the projection type, create the projection using JAVA REFLECTION
                Projector proj = ProjectorFactory.getInstance(pdata.getProjectorType());
                float[][] projection = proj.project(dmat_c);

                //Add the new projection to the list of projections
                projections.add(projection);

                //Improve the projection
                if (projection != null) {
                    ForceScheme force = new ForceScheme(pdata.getFractionDelta(), projection.length);
                    for (int i = 0; i < pdata.getNumberIterations(); i++) {
                        force.iteration(dmat_c, projection);
                    }
                }
            }

            if (view != null) {
                view.setStatus("Assembling the final projection...", 85);
            }
            float[][] projection = new float[dmat.getElementCount()][];

            //Armazena as maiores distancias ate o medoid
            float[] medoidMaxDistances = new float[clusters.size()];
            float overallMaxDistance = Float.MIN_VALUE;
            Arrays.fill(medoidMaxDistances, Float.MIN_VALUE);

            //Encontar as maiores distancias
            for (int i = 0; i < clusters.size(); i++) {
                if (view != null) {
                    view.setStatus("Assembling the final projection...", 85 + i / 5);
                }
                for (int j = 0; j < clusters.get(i).size(); j++) {
                    float distance = dmat.getDistance(medoids[i], clusters.get(i).get(j));

                    if (distance > medoidMaxDistances[i]) {
                        medoidMaxDistances[i] = distance;
                    }
                    //Store the overall max distance
                    if (distance > overallMaxDistance) {
                        overallMaxDistance = distance;
                    }
                }
            }

            for (int i = 0; i < clusters.size(); i++) {
                float[][] p = projections.get(i);
                this.normalize2D(p, 0, medoidMaxDistances[i] / overallMaxDistance);

                //////////////////////////////////////////////////////////////////////
                //  1.2. somar a essas projecoes normalizadas as coordenadas X e Y
                //       dos centroides dos seus agrupamentos
                for (int j = 0; j < p.length; j++) {
                    p[j][0] = p[j][0] + (pivotsProjection[i][0] * pdata.getClusterFactor());
                    p[j][1] = p[j][1] + (pivotsProjection[i][1] * pdata.getClusterFactor());
                }
                //////////////////////////////////////////////////////////////////////
                //2. remontar a projecao final
                for (int j = 0; j < p.length; j++) {
                    projection[clusters.get(i).get(j)] = p[j];
                }
            }

            return projection;
        } catch (IOException ex) {
            Logger.getLogger(ProjClusProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
        return new ProjClusProjectionView(pdata);
    }

    private float[][] createPivotsProjection(Matrix pivots, ProjectionData pdata) {
        float[][] projection = null;

        try {
            DistanceMatrix dmat = new DistanceMatrix(pivots, this.diss);

            //Based on the projection type, create the projection using JAVA REFLECTION
            Projector proj = ProjectorFactory.getInstance(pdata.getProjectorType());
            projection = proj.project(dmat);

            //Improve the projection
            if (projection != null) {
                ForceScheme force = new ForceScheme(pdata.getFractionDelta(), projection.length);
                for (int i = 0; i < pdata.getNumberIterations(); i++) {
                    force.iteration(dmat, projection);
                }
            }

            //Normalizing the projection
            this.normalize2D(projection, 0, 1);

        } catch (IOException ex) {
            Logger.getLogger(ProjClusProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return projection;
    }

    private DistanceMatrix createDistanceMatrix(Matrix matrix,
            ArrayList<Integer> cluster) throws IOException {
        //Create the new points matrix
        Matrix nMatrix = MatrixFactory.getInstance(matrix.getClass());

        for (int i = 0; i < cluster.size(); i++) {
            nMatrix.addRow(matrix.getRow(cluster.get(i)));
        }

        return new DistanceMatrix(nMatrix, this.diss);
    }

    private void normalize2D(float[][] projection, float begin, float end) {
        float maxX = projection[0][0];
        float minX = projection[0][0];
        float maxY = projection[0][1];
        float minY = projection[0][1];
        
        for (int _ins = 1; _ins < projection.length; _ins++) {
            if (minX > projection[_ins][0]) {
                minX = projection[_ins][0];
            } else if (maxX < projection[_ins][0]) {
                maxX = projection[_ins][0];
            }

            if (minY > projection[_ins][1]) {
                minY = projection[_ins][1];
            } else if (maxY < projection[_ins][1]) {
                maxY = projection[_ins][1];
            }
        }
        
        float endY = (maxY - minY) / (maxX - minX);
        //float endY = end;

        //for each position in the ArrayList ... normalize!
        for (int _ins = 0; _ins < projection.length; _ins++) {
            if (maxX - minX > 0.0) {
                projection[_ins][0] = (projection[_ins][0] - minX) / (maxX - minX);
            } else {
                projection[_ins][0] = 0;
            }
            if (maxY - minY > 0.0) {
                projection[_ins][1] = (projection[_ins][1] - minY) / ((maxY - minY) * endY);
            } else {
                projection[_ins][1] = 0;
            }
        }
    }

    private DistanceMatrix createDistanceMatrix(DistanceMatrix dmat, ArrayList<Integer> cluster) {
        DistanceMatrix dmat_c = null;

        //Creating the pivots distance matrix
        dmat_c = new DistanceMatrix(cluster.size());

        for (int i = 0; i < cluster.size() - 1; i++) {
            for (int j = cluster.size() - 1; j > i; j--) {
                dmat_c.setDistance(i, j, dmat.getDistance(cluster.get(i), cluster.get(j)));
            }
        }

        return dmat_c;
    }

    private float[][] createPivotsProjection(DistanceMatrix dmat, int[] medoids, ProjectionData pdata) {
        float[][] projection = null;


        //Creating the pivots distance matrix
        DistanceMatrix dmat_p = new DistanceMatrix(medoids.length);

        for (int i = 0; i < medoids.length; i++) {
            for (int j = medoids.length - 1; j > i; j--) {
                dmat_p.setDistance(i, j, dmat.getDistance(medoids[i], medoids[j]));
            }
        }

        //Based on the projection type, create the projection using JAVA REFLECTION
        Projector proj = ProjectorFactory.getInstance(pdata.getProjectorType());
        projection = proj.project(dmat_p);

        //Improve the projection
        if (projection != null) {
            ForceScheme force = new ForceScheme(pdata.getFractionDelta(), projection.length);
            for (int i = 0; i < pdata.getNumberIterations(); i++) {
                force.iteration(dmat_p, projection);
            }
        }

        //Normalizing the projection
        this.normalize2D(projection, 0, 1);

        return projection;
    }

    private Dissimilarity diss = new Euclidean();
    private ArrayList<ArrayList<Integer>> clusters;
}
