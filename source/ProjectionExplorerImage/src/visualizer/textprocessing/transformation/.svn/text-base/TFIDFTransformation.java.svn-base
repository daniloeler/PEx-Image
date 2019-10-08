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

package visualizer.textprocessing.transformation;

import visualizer.matrix.Matrix;
import visualizer.matrix.SparseVector;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class TFIDFTransformation implements MatrixTransformation {

    @Override
    public Matrix tranform(Matrix matrix, Object parameter) {
        //Store the number of documents which the term occur
        float[] docsFreq = new float[matrix.getDimensions()];

        //Count the number of documents which the terms occurr
        for (int lin = 0; lin < matrix.getRowCount(); lin++) {
            SparseVector sv = (SparseVector) matrix.getRow(lin);
            int svlength = sv.getIndex().length;

            for (int col = 0; col < svlength; col++) {
                docsFreq[sv.getIndex()[col]]++;
            }
        }

        //Calculate the tfidf
        for (int lin = 0; lin < matrix.getRowCount(); lin++) {
            SparseVector sv = (SparseVector) matrix.getRow(lin);
            sv.shouldUpdateNorm();

            int svlength = sv.getIndex().length;

            for (int col = 0; col < svlength; col++) {
                //get the term-frequency
                float tf = sv.getValues()[col];

                float idf = 0.0f;
                if (docsFreq[col] != 0) {
                    idf = (float) Math.log(matrix.getRowCount() / docsFreq[sv.getIndex()[col]]);
                }

                //Calculate and store the tidf
                sv.getValues()[col] = (tf * idf);
            }
        }

        return matrix;
    }

}
