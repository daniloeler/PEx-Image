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

package visualizer.dimensionreduction;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class PCA extends DimensionalityReduction {

    public PCA(int targetDimension) {
        super(targetDimension);
    }

    @Override
    protected float[][] execute(Matrix matrix, Dissimilarity diss) throws IOException {
        float[][] points = matrix.toMatrix();
        double[][] covmatrix_aux = this.createCovarianceMatrix(points);

        DoubleMatrix2D covmatrix = new DenseDoubleMatrix2D(covmatrix_aux);
        EigenvalueDecomposition dec = new EigenvalueDecomposition(covmatrix);
        DoubleMatrix2D decomp = dec.getV();

        //storing the eigenvalues
        this.eigenvalues = new float[covmatrix_aux.length];
        DoubleMatrix1D eigenvalues_aux = dec.getRealEigenvalues();
        for (int i = 0; i < covmatrix_aux.length; i++) {
            this.eigenvalues[i] = (float) eigenvalues_aux.get((covmatrix_aux.length - i - 1));
        }

        double[][] points_aux2 = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            points_aux2[i] = new double[points[i].length];
            for (int j = 0; j < points[i].length; j++) {
                points_aux2[i][j] = points[i][j];
            }
        }

        double[][] decomp_aux = new double[covmatrix_aux.length][];
        for (int i = 0; i < covmatrix_aux.length; i++) {
            decomp_aux[i] = new double[targetDimension];

            for (int j = 0; j < targetDimension; j++) {
                decomp_aux[i][j] = decomp.getQuick(i, covmatrix_aux[0].length - j - 1);
            }
        }

        DoubleMatrix2D decompostion = new DenseDoubleMatrix2D(decomp_aux);
        DoubleMatrix2D points_aux = new DenseDoubleMatrix2D(points_aux2);
        DoubleMatrix2D proj = points_aux.zMult(decompostion, null, 1.0, 1.0, false, false);

        //copying the projection
        float[][] projection = new float[points.length][];
        double[][] projection_aux = proj.toArray();

        for (int i = 0; i < projection_aux.length; i++) {
            projection[i] = new float[targetDimension];
            for (int j = 0; j < projection_aux[i].length; j++) {
                projection[i][j] = (float) projection_aux[i][j];
            }
        }

        return projection;
    }

    public void setUseSamples(boolean useSamples) {
        this.useSamples = useSamples;
    }

    public float[] getEigenvalues() {
        return eigenvalues;
    }

    private float[][] useSamples(float[][] points) {
        float[][] samples = new float[points.length / 4][];
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        int i = 0;
        while (indexes.size() < samples.length) {
            int index = (int) (Math.random() * (points.length - 1));
            if (!indexes.contains(index)) {
                samples[i] = points[index];
                indexes.add(index);
                i++;
            }
        }

        return samples;
    }

    private double[][] createCovarianceMatrix(float[][] points) {
        if (this.useSamples) {
            points = this.useSamples(points);
        }

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
        return covmatrix;
    }

    //calculate the covariance between columns a and b
    private float covariance(float[][] points, int a, int b) {
        float covariance = 0.0f;
        for (int i = 0; i < points.length; i++) {
            covariance += points[i][a] * points[i][b];
        }
        covariance /= (points.length - 1);
        return covariance;
    }

    public static void main(String[] args) {
        try {
            String filename = ".\\test\\data\\cbr-ilp-ir.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            PCA pca = new PCA(200);
            pca.execute(matrix, null);
            float[] eigenvalues1 = pca.getEigenvalues();
            
            float total=0.0f;
            for(int i=0; i < eigenvalues1.length; i++) {
                total += eigenvalues1[i];
            }
            
            
            float partial=0.0f;
            for(int i=0; i < eigenvalues1.length; i++) {
                partial += eigenvalues1[i];
                
                System.out.println(i + ": " + partial/total);
            }            
        } catch (IOException ex) {
            Logger.getLogger(PCA.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    
    private boolean useSamples = false;
    private float[] eigenvalues;
}
