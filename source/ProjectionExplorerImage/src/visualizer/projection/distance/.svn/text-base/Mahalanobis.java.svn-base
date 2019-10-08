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

package visualizer.projection.distance;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.CholeskyDecomposition;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Mahalanobis {

    public DistanceMatrix getDistanceMatrix(Matrix matrix) {
        float[][] points = matrix.toMatrix();
        DoubleMatrix2D cov = this.createCovarianceMatrix(points);

        DoubleMatrix2D identity = DoubleFactory2D.sparse.identity(points[0].length);
        CholeskyDecomposition chol = new CholeskyDecomposition(cov);
        DoubleMatrix2D cov_inverse = chol.solve(identity);

        DistanceMatrix dmat = new DistanceMatrix(matrix.getRowCount());
        dmat.setIds(matrix.getIds());

        float[] cdata = new float[matrix.getRowCount()];
        for (int i = 0; i < matrix.getRowCount(); i++) {
            cdata[i] = matrix.getRow(i).getKlass();
        }

        dmat.setClassData(cdata);

        for (int i = 1; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < i; j++) {
                DoubleMatrix1D diff = DoubleFactory1D.sparse.make(points[0].length);

                float[] vect1 = matrix.getRow(i).toArray();
                float[] vect2 = matrix.getRow(j).toArray();

                for (int k = 0; k < vect1.length; k++) {
                    diff.setQuick(k, vect1[k] - vect2[k]);
                }

                DoubleMatrix1D diff_cinv = DoubleFactory1D.sparse.make(points[0].length);

                for (int k = 0; k < points[0].length; k++) {
                    double zDotProduct = cov_inverse.viewColumn(k).zDotProduct(diff);
                    diff_cinv.setQuick(k, zDotProduct);
                }

                double dist = diff.zDotProduct(diff_cinv);
                dmat.setDistance(i, j, (float) Math.sqrt(dist));
            }
        }

        return dmat;
    }

    private DoubleMatrix2D createCovarianceMatrix(float[][] points) {
        //calculating the mean
        double[] mean = new double[points[0].length];
        Arrays.fill(mean, 0.0f);

        for (int i = 0; i < points.length; i++) {
            //calculating
            for (int j = 0; j < points[i].length; j++) {
                mean[j] += points[i][j];
            }
        }

        for (int i = 0; i < mean.length; i++) {
            mean[i] /= points.length;
        }

        //extracting the mean
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j] -= mean[j];
            }
        }

        double[][] covmatrix = new double[points[0].length][];

        for (int i = 0; i < covmatrix.length; i++) {
            covmatrix[i] = new double[points[0].length];
        }

        for (int i = 0; i < covmatrix.length; i++) {
            for (int j = 0; j < covmatrix.length; j++) {
                covmatrix[i][j] = this.covariance(points, i, j);
            }
        }

        return new DenseDoubleMatrix2D(covmatrix);
    }

    //calculate the covariance between columns a and b
    private float covariance(float[][] points, int a, int b) {
        float cov = 0.0f;

        for (int i = 0; i < points.length; i++) {
            cov += points[i][a] * points[i][b];
        }

        cov /= points.length;

        return cov;
    }

    public static void main(String[] args) {
        try {
            String filename = "G:\\User\\users\\Documents\\FERNANDO\\Tese\\datasets\\cbr-ilp-ir-son.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            Mahalanobis m = new Mahalanobis();
            DistanceMatrix dmat = m.getDistanceMatrix(matrix);

            dmat.save(filename + "_mahalanobis.dmat");

        } catch (IOException ex) {
            Logger.getLogger(Mahalanobis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
