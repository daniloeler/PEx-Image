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

import visualizer.matrix.DenseVector;
import visualizer.matrix.SparseVector;
import visualizer.matrix.Vector;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class CityBlock implements Dissimilarity {

    public float calculate(Vector v1, Vector v2) {
        assert (v1.size() == v2.size()) : "ERROR: vectors of different sizes!";
        assert (v1.getClass() == v2.getClass()) :
                "Error: only supported comparing vectors of the same type";

        if (v1 instanceof SparseVector) {
            if (((SparseVector) v2).getIndex().length > ((SparseVector) v1).getIndex().length) {
                SparseVector tmp = (SparseVector) v1;
                v1 = v2;
                v2 = tmp;
            }

            int v1length = ((SparseVector) v1).getIndex().length;
            int v2length = ((SparseVector) v2).getIndex().length;
            int[] v1index = ((SparseVector) v1).getIndex();
            int[] v2index = ((SparseVector) v2).getIndex();
            float[] v1values = v1.getValues();
            float[] v2values = v2.getValues();

            int i = 0;
            int j = 0;
            float dist = 0.0f;

            while (i < v1length) {
                if (j < v2length) {
                    if (v1index[i] == v2index[j]) {
                        dist += Math.abs(v1values[i] - v2values[j]);
                        i++;
                        j++;
                    } else if (v1index[i] < v2index[j]) {
                        dist += Math.abs(v1values[i]);
                        i++;
                    } else {
                        dist += Math.abs(v2values[j]);
                        j++;
                    }
                } else {
                    break;
                }
            }

            while (i < v1length) {
                dist += Math.abs(v1values[i]);
                i++;
            }

            while (j < v2length) {
                dist += Math.abs(v2values[j]);
                j++;
            }

            return dist;

        } else if (v1 instanceof DenseVector) {

            float[] vector1 = v1.getValues();
            float[] vector2 = v2.getValues();

            float dist = 0.0f;

            int length = vector1.length;
            for (int i = 0; i < length; i++) {
                dist += Math.abs(vector1[i] - vector2[i]);
            }

            return dist;
        }

        return 0.0f;
    }
}
