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

package visualizer.projection.lsp;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.CholeskyDecomposition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import lspsolver.Solver;
import visualizer.graph.Graph;
import visualizer.projection.ForceScheme;
import visualizer.projection.Projection;
import visualizer.projection.ProjectionData;
import visualizer.projection.Projector;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;
import visualizer.util.ApproxKNN;
import visualizer.util.KNN;
import visualizer.graph.Scalar;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.util.Pair;
import visualizer.projection.ProjectorFactory;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.idmap.IDMAPProjection;
import visualizer.datamining.clustering.BKmeans;
import visualizer.util.PExConstants;
import visualizer.datamining.clustering.Kmedoids;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class LSPProjection2D extends Projection {

    @Override
    public float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view) {
        float projection[][] = null;
        ArrayList<ArrayList<Integer>> clusters = null;
        Matrix centroids = null;

        try {
            this.diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());

            if (pdata.getControlPointsChoice() == ControlPointsType.KMEANS) {
                if (view != null) {
                    view.setStatus("Calculating the B-KMEANS...", 40);
                }

                //clustering the points
                BKmeans bkmeans = new BKmeans(pdata.getNumberControlPoints());
                clusters = bkmeans.execute(LSPProjection2D.this.diss, matrix);
                centroids = bkmeans.getCentroids();
                controlPoints = bkmeans.getMedoids(matrix);

                //if less medoids are returned than the expected (due to the
                //clustering method employed), choose on the clusters other
                //medoids
                ArrayList<Integer> medoids_aux = new ArrayList<Integer>();
                for (int i = 0; i < controlPoints.length; i++) {
                    medoids_aux.add(controlPoints[i]);
                }

                while (medoids_aux.size() < pdata.getNumberControlPoints()) {
                    for (int c = 0; c < clusters.size() &&
                            medoids_aux.size() < pdata.getNumberControlPoints(); c++) {
                        if (clusters.get(c).size() > matrix.getRowCount() / pdata.getNumberControlPoints()) {
                            for (int i = 0; i < clusters.get(c).size(); i++) {
                                if (!medoids_aux.contains(clusters.get(c).get(i))) {
                                    medoids_aux.add(clusters.get(c).get(i));
                                    break;
                                }
                            }
                        }
                    }
                }

                controlPoints = new int[medoids_aux.size()];
                for (int i = 0; i < controlPoints.length; i++) {
                    controlPoints[i] = medoids_aux.get(i);
                }

                pdata.setNumberControlPoints(controlPoints.length);

            } else if (pdata.getControlPointsChoice() == ControlPointsType.RANDOM) {
                //Random choice
                controlPoints = new int[pdata.getNumberControlPoints()];
                for (int i = 0; i < controlPoints.length; i++) {
                    controlPoints[i] = (int) (Math.random() * matrix.getRowCount());
                }

                BKmeans bkmeans = new BKmeans(pdata.getNumberControlPoints());
                clusters = bkmeans.execute(this.diss, matrix);
                centroids = bkmeans.getCentroids();
            }

            if (view != null) {
                view.setStatus("Calculating the nearest neigbbors...", 35);
            }

            //getting the nearest neighbors            
            int max = Math.max(pdata.getKnnNumberNeighbors(), pdata.getNumberNeighborsConnection());
            int min = Math.min(pdata.getKnnNumberNeighbors(), pdata.getNumberNeighborsConnection());
           
            ApproxKNN appknn = new ApproxKNN(max);
            Pair[][] maxneighbors = appknn.execute(matrix, this.diss, clusters, centroids);
            Pair[][] minneighbors = new Pair[maxneighbors.length][];

            for (int i = 0; i < minneighbors.length; i++) {
                minneighbors[i] = new Pair[min];

                for (int j = 0; j < min; j++) {
                    minneighbors[i][j] = maxneighbors[i][j];
                }
            }

            Pair[][] mesh = null;

            if (pdata.getKnnNumberNeighbors() > pdata.getNumberNeighborsConnection()) {
                this.knnneighbors = maxneighbors;
                mesh = minneighbors;
            } else {
                this.knnneighbors = minneighbors;
                mesh = maxneighbors;
            }

            Matrix matrix_cp = MatrixFactory.getInstance(matrix.getClass());
            for (int i = 0; i < this.controlPoints.length; i++) {
                matrix_cp.addRow(matrix.getRow(this.controlPoints[i]));
            }

            DistanceMatrix dmat_cp = new DistanceMatrix(matrix_cp, this.diss);

            //Projecting the control points
            if (view != null) {
                view.setStatus("Projecting...", 40);
            }

            IDMAPProjection idmap=new IDMAPProjection();
            projection_cp = idmap.project(dmat_cp, pdata, view);                   
            
//            ISOMAPProjection isomap = new ISOMAPProjection();
//            projection_cp = isomap.project(dmat_cp, pdata, view);

            //creating the KNN mesh
            if (view != null) {
                view.setStatus("Creating the mesh...", 45);
            }

            MeshGenerator meshgen = new MeshGenerator();
            mesh = meshgen.execute(mesh, matrix, this.diss);

            //creating the final projection
            if (view != null) {
                view.setStatus("Solving the system...", 65);
            }

            projection = this.createFinalProjection(mesh, matrix, pdata);

        } catch (IOException ex) {
            Logger.getLogger(LSPProjection2D.class.getName()).log(Level.SEVERE, null, ex);
        }

        return projection;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        try {
            this.dmat = dmat;

            KNN knn = new KNN(pdata.getKnnNumberNeighbors());
            this.knnneighbors = knn.execute(dmat);

            //Choosen the control points
            if (pdata.getControlPointsChoice() == ControlPointsType.KMEDOIDS) {
                if (view != null) {
                    view.setStatus("Calculating the KMEDOIDS...", 40);
                }

                Kmedoids kmedois = new Kmedoids(pdata.getNumberControlPoints());
                kmedois.execute(dmat);
                controlPoints = kmedois.getMedoids();
            } else if (pdata.getControlPointsChoice() == ControlPointsType.RANDOM) {
                //Random choice
                controlPoints = new int[pdata.getNumberControlPoints()];
                for (int i = 0; i < controlPoints.length; i++) {
                    controlPoints[i] = (int) (Math.random() * dmat.getElementCount());
                }
            }

            //Creating the control points distance matrix
            DistanceMatrix dmat_cp = new DistanceMatrix(pdata.getNumberControlPoints());

            for (int i = 0; i < pdata.getNumberControlPoints(); i++) {
                for (int j = 0; j < pdata.getNumberControlPoints(); j++) {
                    if (i != j) {
                        dmat_cp.setDistance(i, j, dmat.getDistance(controlPoints[i], controlPoints[j]));
                    }
                }
            }

            //Projecting the control points
            if (view != null) {
                view.setStatus("Projecting...", 40);
            }

            Projector proj = ProjectorFactory.getInstance(pdata.getProjectorType());
            projection_cp = proj.project(dmat_cp);

            if (projection_cp != null) {
                ForceScheme force = new ForceScheme(pdata.getFractionDelta(), projection_cp.length);
                for (int i = 0; i < pdata.getNumberIterations(); i++) {
                    if (view != null) {
                        view.setStatus("Improving the projection...",
                                (int) (45 + (i * 50.0f / pdata.getNumberIterations())));
                    }
                    force.iteration(dmat_cp, projection_cp);
                }
            }

            //creating the KNN mesh
            if (view != null) {
                view.setStatus("Creating the mesh...", 45);
            }

            KNN knnmesh = new KNN(pdata.getNumberNeighborsConnection());
            Pair[][] mesh = knnmesh.execute(dmat);

            MeshGenerator meshgen = new MeshGenerator();
            mesh = meshgen.execute(mesh, dmat);

            //creating the final projection
            if (view != null) {
                view.setStatus("Solving the system...", 50);
            }

            return this.createFinalProjection(mesh, dmat, pdata);
        } catch (IOException ex) {
            Logger.getLogger(LSPProjection2D.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
        return new LSPProjectionView(pdata);
    }

    @Override
    public void postProcessing(Graph graph) {
        Scalar s = graph.addScalar(PExConstants.PIVOTS);

        for (int i = 0; i < graph.getVertex().size(); i++) {
            graph.getVertex().get(i).setScalar(s, 0.0f);
        }

        for (int i = 0; i < this.controlPoints.length; i++) {
            graph.getVertex().get(controlPoints[i]).setScalar(s, 1.0f);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    //USING POINTS
    private float[][] createFinalProjection(Pair[][] neighbors, Matrix matrix,
            ProjectionData pdata) throws IOException {

        float projection[][] = new float[matrix.getRowCount()][];

        if (System.getProperty("os.name").toLowerCase().equals("windows xp") ||
                System.getProperty("os.name").toLowerCase().equals("windows vista") ||
                System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
            this.projectUsingProgram(pdata, neighbors, projection);
        } else {
            this.projectUsingColt(pdata, neighbors, projection);
        }

        Runtime.getRuntime().gc();

        return projection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //USING DISTANCE MATRIX
    private float[][] createFinalProjection(Pair[][] neighbors, DistanceMatrix dmat,
            ProjectionData pdata) {
        float projection[][] = new float[dmat.getElementCount()][];

        if (System.getProperty("os.name").toLowerCase().equals("windows xp") ||
                System.getProperty("os.name").toLowerCase().equals("windows vista") ||
                System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
            this.projectUsingProgram(pdata, neighbors, projection);
        } else {
            this.projectUsingColt(pdata, neighbors, projection);
        }

        Runtime.getRuntime().gc();

        return projection;
    }

    private void projectUsingColt(ProjectionData pdata, Pair[][] neighbors, float[][] projection) {
        long start = System.currentTimeMillis();

        int nRows = neighbors.length + pdata.getNumberControlPoints();
        int nColumns = neighbors.length;
        SparseDoubleMatrix2D A = new SparseDoubleMatrix2D(nRows, nColumns);

        for (int i = 0; i < neighbors.length; i++) {
            A.setQuick(i, i, 1.0f);

            for (int j = 0; j < neighbors[i].length; j++) {
                A.setQuick(i, neighbors[i][j].index, (-(1.0f / neighbors[i].length)));
            }
        }

        //Creating the Fij
        for (int i = 0; i < pdata.getNumberControlPoints(); i++) {
            A.setQuick((projection.length + i), this.controlPoints[i], 1.0f);
        }

        SparseDoubleMatrix2D B = new SparseDoubleMatrix2D(nRows, 2);
        for (int i = 0; i < this.projection_cp.length; i++) {
            B.setQuick((neighbors.length + i), 0, this.projection_cp[i][0]);
            B.setQuick((neighbors.length + i), 1, this.projection_cp[i][1]);
        }

        //Solving
        DoubleMatrix2D AtA = A.zMult(A, null, 1.0, 1.0, true, false);
        DoubleMatrix2D AtB = A.zMult(B, null, 1.0, 1.0, true, false);

        start = System.currentTimeMillis();
        CholeskyDecomposition chol = new CholeskyDecomposition(AtA);
        DoubleMatrix2D X = chol.solve(AtB);

        for (int i = 0; i < X.rows(); i++) {
            projection[i] = new float[2];
            projection[i][0] = (float) X.getQuick(i, 0);
            projection[i][1] = (float) X.getQuick(i, 1);
        }

        long finish = System.currentTimeMillis();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Solving the system using Colt time: " + (finish - start) / 1000.0f + "s");
    }

    private void projectUsingProgram(ProjectionData pdata, Pair[][] neighbors, float[][] projection) {
        long start = System.currentTimeMillis();

        int nRows = neighbors.length + pdata.getNumberControlPoints();
        int nColumns = neighbors.length;

        Solver solver = new Solver(nRows, nColumns);

        try {
            ////////////////////////////////////////////
            //creating matrix A
            for (int i = 0; i < neighbors.length; i++) {
                solver.addToA(i, i, 1.0f);

                for (int j = 0; j < neighbors[i].length; j++) {
                    solver.addToA(i, neighbors[i][j].index, (-(1.0f / neighbors[i].length)));
                }
            }

            for (int i = 0; i < pdata.getNumberControlPoints(); i++) {
                solver.addToA((projection.length + i), this.controlPoints[i], 1.0f);
            }

            ////////////////////////////////////////////
            //creating matrix B
            for (int i = 0; i < this.projection_cp.length; i++) {
                solver.addToB((neighbors.length + i), 0, this.projection_cp[i][0]);
                solver.addToB((neighbors.length + i), 1, this.projection_cp[i][1]);
            }

            float[] result = solver.solve();
            for (int i = 0; i < result.length; i += 2) {
                projection[i / 2] = new float[2];
                projection[i / 2][0] = result[i];
                projection[i / 2][1] = result[i + 1];
            }

        } catch (IOException ex) {
            Logger.getLogger(LSPProjection2D.class.getName()).log(Level.SEVERE, null, ex);
        }

        long finish = System.currentTimeMillis();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Solving the system using LSPSolver time: " + (finish - start) / 1000.0f + "s");
    }

    private float[][] projection_cp;
    private int[] controlPoints;
    private Dissimilarity diss;
}
