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

package visualizer.matrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class MatrixUtils {

    public static Vector mean(Matrix matrix) {
        assert (matrix.getRowCount() > 0) : "More than zero vectors must be used!";

        if (matrix instanceof SparseMatrix) {
            float[] mean = new float[matrix.getDimensions()];
            Arrays.fill(mean, 0.0f);

            int size = matrix.getRowCount();
            for (int i = 0; i < size; i++) {
                int[] index = ((SparseVector) matrix.getRow(i)).getIndex();
                float[] values = matrix.getRow(i).getValues();

                for (int j = 0; j < index.length; j++) {
                    mean[index[j]] += values[j];
                }
            }

            for (int j = 0; j < mean.length; j++) {
                mean[j] = mean[j] / size;
            }

            return new SparseVector(mean);

        } else if (matrix instanceof DenseMatrix) {
            float[] mean = new float[matrix.getDimensions()];
            Arrays.fill(mean, 0.0f);

            int size = matrix.getRowCount();
            for (int i = 0; i < size; i++) {
                float[] values = matrix.getRow(i).getValues();

                for (int j = 0; j < values.length; j++) {
                    mean[j] += values[j];
                }
            }

            for (int j = 0; j < mean.length; j++) {
                mean[j] = mean[j] / size;
            }

            return new DenseVector(mean);
        }

        return null;
    }

    public static SparseMatrix convert(DenseMatrix matrix) {
        SparseMatrix newmatrix = new SparseMatrix();

        newmatrix.setAttributes(matrix.getAttributes());

        for (int i = 0; i < matrix.getRowCount(); i++) {
            Vector dv = matrix.getRow(i);
            newmatrix.addRow(new SparseVector(dv.toArray(), dv.getId(), dv.getKlass()));
        }

        return newmatrix;
    }
    
    public static DenseMatrix convert(SparseMatrix matrix) {
        DenseMatrix newmatrix = new DenseMatrix();

        newmatrix.setAttributes(matrix.getAttributes());

        for (int i = 0; i < matrix.getRowCount(); i++) {
            Vector dv = matrix.getRow(i);
            newmatrix.addRow(new SparseVector(dv.toArray(), dv.getId(), dv.getKlass()));
        }

        return newmatrix;
    }

    public static void main(String[] args) {
        try {
            String filename = "C:\\Documents and Settings\\Fernando\\Mijn documenten\\dataset\\output3.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            SparseMatrix convert = MatrixUtils.convert((DenseMatrix) matrix);
            convert.save(filename + ".sp.data");

        } catch (IOException ex) {
            Logger.getLogger(MatrixUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
