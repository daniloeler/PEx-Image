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

package visualizer.projection.mds;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
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
import visualizer.projection.idmap.IDMAPProjection;
import visualizer.projection.lle.LLEProjection;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ClassicalScalingProjection extends Projection {

    @Override
    public float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view) {
        try {

            if (view != null) {
                view.setStatus("Calculating the dissimilarities...", 40);
            }

            Dissimilarity diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());
            DistanceMatrix dmat_aux = new DistanceMatrix(matrix, diss);
            dmat_aux.setIds(matrix.getIds());
            dmat_aux.setClassData(matrix.getClassData());

            float[][] projection = this.project(dmat_aux, pdata, view);
            return projection;
        } catch (IOException ex) {
            Logger.getLogger(IDMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        this.dmat = dmat;

        //1. Compute the matrix of squared dissimilarities D^2.
        DoubleMatrix2D D = new DenseDoubleMatrix2D(dmat.getElementCount(), dmat.getElementCount());

        for (int i = 0; i < dmat.getElementCount() - 1; i++) {
            for (int k = i + 1; k < dmat.getElementCount(); k++) {
                D.setQuick(i, k, dmat.getDistance(i, k) * dmat.getDistance(i, k));
                D.setQuick(k, i, dmat.getDistance(i, k) * dmat.getDistance(i, k));
            }
        }

        if (view != null) {
            view.setStatus("Applying the double centering...", 50);
        }

        //2. Apply double centering to this matrix: B = -1/2 * J * D^2 * J
        //      J = I - n^-1 * 1 * 1T
        DoubleMatrix2D J = DoubleFactory2D.dense.identity(dmat.getElementCount());
        double value = 1.0 / dmat.getElementCount();

        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int k = 0; k < dmat.getElementCount(); k++) {
                J.setQuick(i, k, J.getQuick(i, k) - value);
            }
        }

        DoubleMatrix2D b = J.zMult(D, null, -0.5, 1.0, false, false).zMult(J, null, 1.0, 1.0, false, false);

        if (view != null) {
            view.setStatus("Calculating the eigenvectors...", 70);
        }

        //3. Compute the eigendecomposition of B = Q+A+.
        EigenvalueDecomposition dec = new EigenvalueDecomposition(b);
        DoubleMatrix2D eigenvectors = dec.getV();
        DoubleMatrix1D eigenvalues = dec.getRealEigenvalues();

        if (view != null) {
            view.setStatus("Assembling the final projection...", 90);
        }

        //4. Let the matrix of the first m eigenvalues greater than zero be A+
        //and Q+ the first m columns of Q. Then, the coordinate matrix of
        //classical scaling is given by X = Q+A^1/2+ .
        DenseDoubleMatrix2D Q = new DenseDoubleMatrix2D(dmat.getElementCount(), 2);
        DenseDoubleMatrix2D A = new DenseDoubleMatrix2D(2, 2);

        for (int i = eigenvalues.size() - 1, k = 0; i >= 0 && k < 2; i--) {
            if (eigenvalues.get(i) > EPSILON) {
                for (int n = 0; n < Q.rows(); n++) {
                    Q.setQuick(n, k, eigenvectors.getQuick(n, i));
                }

                A.setQuick(k, k, Math.sqrt(eigenvalues.getQuick(i)));
                k++;
            }
        }

        DoubleMatrix2D result = Q.zMult(A, null, 1.0, 1.0, false, false);

        float[][] projection = new float[result.rows()][];

        for (int i = 0; i < projection.length; i++) {
            projection[i] = new float[2];

            projection[i][0] = (float) result.getQuick(i, 0);
            projection[i][1] = (float) result.getQuick(i, 1);
        }

        return projection;
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
        return new ClassicalScalingProjectionView(pdata);
    }

    public static void main(String[] args) {
        try {
            String filename = "G:\\User\\users\\Documents\\FERNANDO\\Tese\\datasets\\iris-std.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            BufferedWriter out = null;

            try {
                out = new BufferedWriter(new FileWriter(filename + ".prj"));

                ProjectionData pdata = new ProjectionData();
                pdata.setDissimilarityType(DissimilarityType.EUCLIDEAN);

                ClassicalScalingProjection csp = new ClassicalScalingProjection();
                float[][] projection = csp.project(matrix, pdata, null);

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
        } catch (IOException ex) {
            Logger.getLogger(LLEProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static final float EPSILON = Float.MIN_VALUE;
}
