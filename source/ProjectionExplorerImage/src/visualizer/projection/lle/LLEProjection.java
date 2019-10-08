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

package visualizer.projection.lle;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.Projection;
import visualizer.projection.ProjectionData;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.DissimilarityType;
import visualizer.util.KNN;
import visualizer.util.Pair;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class LLEProjection extends Projection {

    @Override
    public float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view) {
        try {
            if (view != null) {
                view.setStatus("Finding the nearest neighbors...", 40);
            }

            //   1. Find neighbours in X space
            Dissimilarity diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());

            KNN knn = new KNN(pdata.getNumberNeighborsConnection());
            Pair[][] neighbors = knn.execute(matrix, diss);

            if (view != null) {
                view.setStatus("Constructing the weights...", 50);
            }

            //   2. Solve for reconstruction weights W
            SparseDoubleMatrix2D w = new SparseDoubleMatrix2D(matrix.getRowCount(), matrix.getRowCount());

            for (int i = 0; i < matrix.getRowCount(); i++) {
                //create matrix Z consisting of all neighbours of Xi
                float[][] z_aux = new float[neighbors[i].length][];

                //subtract Xi from every column of Z
                float[] x = matrix.getRow(i).toArray();

                for (int j = 0; j < neighbors[i].length; j++) {
                    z_aux[j] = matrix.getRow(neighbors[i][j].index).toArray();

                    for (int n = 0; n < z_aux[j].length; n++) {
                        z_aux[j][n] = z_aux[j][n] - x[n];
                    }
                }

                //compute the local covariance C=Z'*Z
                SparseDoubleMatrix2D z = new SparseDoubleMatrix2D(z_aux.length, matrix.getDimensions());

                for (int j = 0; j < z_aux.length; j++) {
                    for (int n = 0; n < z_aux[j].length; n++) {
                        z.setQuick(j, n, z_aux[j][n]);
                    }
                }

                DoubleMatrix2D c = z.zMult(z, null, 1.0, 1.0, false, true);

//                if (k > matrix.getDimensions()) {
                //[e] If K>D, the local covariance will not be full rank, and it should be 
                //    regularized by seting C=C+eps*I where I is the identity matrix and 
                //    eps is a small constant of order 1e-3*trace(C). 
                //    This ensures that the system to be solved in step 2 has a unique solution.
                Algebra alg1 = new Algebra();
                double eps = 0.001 * alg1.trace(c);

                for (int j = 0; j < z_aux.length; j++) {
                    c.setQuick(j, j, c.getQuick(j, j) + eps);
                }
//                }

                //solve linear system C*w = 1 for w
                DoubleMatrix2D b = new DenseDoubleMatrix2D(z_aux.length, 1);
                for (int j = 0; j < z_aux.length; j++) {
                    b.setQuick(j, 0, 1.0);
                }

                Algebra alg2 = new Algebra();
                DoubleMatrix2D result = alg2.solve(c, b);

                // set Wij=0 if j is not a neighbor of i
                // set the remaining elements in the ith row of W equal to w/sum(w);
                double sum = 0.0f;
                for (int j = 0; j < result.rows(); j++) {
                    sum += result.getQuick(j, 0);
                }

                //instead of constructing W, construct (I-W)
                w.setQuick(i, i, 1.0);
                for (int j = 0; j < result.rows(); j++) {
                    double value = (result.getQuick(j, 0) / sum);
                    w.setQuick(i, neighbors[i][j].index, -value);
                }
            }

            //   3. Compute embedding coordinates Y using weights W.
            //create sparse matrix M = (I-W)'*(I-W)
            DoubleMatrix2D m = w.zMult(w, null, 1.0, 1.0, true, false);

            if (view != null) {
                view.setStatus("Calculating the eigenvectors...", 70);
            }

            //find bottom d+1 eigenvectors of M 
            //     (corresponding to the d+1 smallest eigenvalues) 
            EigenvalueDecomposition dec = new EigenvalueDecomposition(m);
            DoubleMatrix2D eigenvectors = dec.getV();
            DoubleMatrix1D eigenvalues = dec.getRealEigenvalues();

            if (view != null) {
                view.setStatus("Assembling the final projection...", 90);
            }

            //set the qth ROW of Y to be the q+1 smallest eigenvector
            //     (discard the bottom eigenvector [1,1,1,1...] with eigenvalue zero)
            float[][] projection = new float[matrix.getRowCount()][];

            for (int i = 0; i < projection.length; i++) {
                projection[i] = new float[2];
            }

            for (int i = 1, j = 0; i < eigenvalues.size() && j < 2; i++) {
                if (eigenvalues.get(i) > EPSILON) {
                    for (int n = 0; n < projection.length; n++) {
                        projection[n][j] = (float) eigenvectors.getQuick(n, i);
                    }

                    j++;
                }
            }

            return projection;

        } catch (IOException ex) {
            Logger.getLogger(LLEProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
        return new LLEProjectionView(pdata);
    }

    public static void main(String[] args) {
        try {
            String filename = "D:\\My Documents\\FERNANDO\\Tese\\cbrilpirson.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            for (int i = 3; i <= 25; i += 1) {
                BufferedWriter out = null;

                try {
                    out = new BufferedWriter(new FileWriter(filename + "_" + i + ".prj"));

                    ProjectionData pdata = new ProjectionData();
                    pdata.setDissimilarityType(DissimilarityType.COSINE_BASED);
                    pdata.setNumberNeighborsConnection(i);

                    LLEProjection lle = new LLEProjection();
                    float[][] projection = lle.project(matrix, pdata, null);

                    out.write("DY\r\n");
                    out.write(Integer.toString(projection.length) + "\r\n");
                    out.write("2\r\n");
                    out.write("x;y\r\n");

                    for (int j = 0; j < projection.length; j++) {
                        out.write(matrix.getRow(j).getId());
                        out.write(";");
                        out.write(Float.toString(projection[j][0]));
                        out.write(";");
                        out.write(Float.toString(projection[j][1]));
                        out.write(";");
                        out.write(Float.toString(matrix.getRow(j).getKlass()));
                        out.write("\r\n");
                    }

                } catch (IOException e) {
                    throw new IOException(e.getMessage());
                } finally {
                    //fechar o arquivo
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException ex) {
                            Logger.getLogger(LLEProjection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LLEProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static final float EPSILON = Float.MIN_VALUE;
}
