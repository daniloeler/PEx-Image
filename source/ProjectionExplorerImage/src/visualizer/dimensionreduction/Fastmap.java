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

import java.io.IOException;
import java.util.Arrays;
import visualizer.matrix.Matrix;
import visualizer.matrix.Vector;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Fastmap extends DimensionalityReduction {

    public Fastmap(int targetDimension) {
        super(targetDimension);
    }

    @Override
    protected float[][] execute(Matrix matrix, Dissimilarity diss) throws IOException {
        //creating the projection
        float[][] projection = new float[matrix.getRowCount()][];
        for (int i = 0; i < matrix.getRowCount(); i++) {
            projection[i] = new float[this.targetDimension];
            Arrays.fill(projection[i], 0.0f);
        }

        for (int curDim = 0; curDim < this.targetDimension; curDim++) {
            //choosen pivots for this recursion
            int[] pivots = this.chooseDistantObjects(matrix, projection, curDim, diss);
            float pDistance = this.distance(matrix.getRow(pivots[0]), matrix.getRow(pivots[1]),
                    projection[pivots[0]], projection[pivots[1]], curDim, diss);

            //if the distance between the pivots is 0, then set 0 for each instance for this dimension
            if (pDistance == 0.0f) {
                //for each instance in the table
                for (int i = 0; i < matrix.getRowCount(); i++) {
                    projection[i][curDim] = 0.0f;
                }
            } else {
                for (int i = 0; i < matrix.getRowCount(); i++) {
                    //current dimension xi = (distance between the instance and the first pivot)^2 +
                    //                       (distance between both pivots)^2 -
                    //                       (distance between the instance and the secod pivot)^2)
                    //                        all divided by 2 times the (distance between both pivots)

                    float lvxi = (float) ((Math.pow(this.distance(matrix.getRow(pivots[0]), matrix.getRow(i),
                            projection[pivots[0]], projection[i], curDim, diss), 2) +
                            Math.pow(this.distance(matrix.getRow(pivots[0]), matrix.getRow(pivots[1]),
                            projection[pivots[0]], projection[pivots[1]], curDim, diss), 2) -
                            Math.pow(this.distance(matrix.getRow(pivots[1]), matrix.getRow(i),
                            projection[pivots[1]], projection[i], curDim, diss), 2)) /
                            (2 * this.distance(matrix.getRow(pivots[0]), matrix.getRow(pivots[1]),
                            projection[pivots[0]], projection[pivots[1]], curDim, diss)));

                    projection[i][curDim] = lvxi;
                }
            }
        }

        return projection;
    }

    private float distance(Vector vectA, Vector vectB, float[] projA,
            float[] projB, int dimension, Dissimilarity diss) throws IOException {
        //original distance
        float dist = diss.calculate(vectA, vectB);

        //transforming the distance if necessary
        for (int i = 0; i < dimension; i++) {
            dist = (float) (Math.sqrt(Math.abs(Math.pow(dist, 2) - Math.pow((projA[i] - projB[i]), 2))));
        }

        return dist;
    }

    private int[] chooseDistantObjects(Matrix matrix, float[][] projection,
            int dimension, Dissimilarity diss) throws IOException {

        int[] choosen = new int[2];

        //chossing the first object randomly
        int x = (int) (Math.random() * (matrix.getRowCount() - 1));
        float maxdist = Float.MIN_VALUE;

        //for each instance
        for (int i = 0; i < matrix.getRowCount(); i++) {
            float aux = this.distance(matrix.getRow(x), matrix.getRow(i),
                    projection[x], projection[i], dimension, diss);
            if (aux > maxdist) {
                maxdist = aux;
                x = i;
            }
        }

        int y = 0;
        maxdist = Float.MIN_VALUE;

        for (int i = 0; i < matrix.getRowCount(); i++) {
            float aux = this.distance(matrix.getRow(x), matrix.getRow(i),
                    projection[x], projection[i], dimension, diss);

            if (aux > maxdist) {
                maxdist = aux;
                y = i;
            }
        }

        choosen[0] = x;
        choosen[1] = y;

        return choosen;
    }

}
