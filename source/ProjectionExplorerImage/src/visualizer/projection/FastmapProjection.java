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

package visualizer.projection;

import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class FastmapProjection extends Projector {

    public FastmapProjection() {
        this.targetDimension = 2;
    }

    public FastmapProjection(int targetDimension) {
        this.targetDimension = targetDimension;
    }

    @Override
    public float[][] project(DistanceMatrix dmat) {
        float[][] points = null;
        try {
            dmat = (DistanceMatrix) dmat.clone();

            points = new float[dmat.getElementCount()][];
            for (int i = 0; i < dmat.getElementCount(); i++) {
                points[i] = new float[this.targetDimension];
            }

            if (points.length < 4) {
                this.doTheFastmapLessThan4Points(points, dmat);
            } else {
                this.doTheFastmap(points, dmat);
            }
            this.normalize(points);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return points;
    }

    public void doTheFastmapLessThan4Points(float[][] points, DistanceMatrix dmat) {
        if (points.length == 1) {
            points[0][0] = 0;
            points[0][1] = 0;
        } else if (points.length == 2) {
            points[0][0] = 0;
            points[0][1] = 0;
            points[1][0] = dmat.getDistance(0, 1);
            points[1][1] = 0;
        } else if (points.length == 3) {
            points[0][0] = 0;
            points[0][1] = 0;
            points[1][0] = dmat.getDistance(0, 1);
            points[1][1] = 0;
            points[2][0] = dmat.getDistance(0, 1);
            points[2][1] = dmat.getDistance(1, 2);
        }
    }

    private void doTheFastmap(float[][] points, DistanceMatrix dmat) {
        int currentDimension = 0;

        while (currentDimension < this.targetDimension) {
            //choosen pivots for this recursion
            int[] lvchoosen = this.chooseDistantObjects(dmat);
            float lvdistance = dmat.getDistance(lvchoosen[0], lvchoosen[1]);

            //if the distance between the pivots is 0, then set 0 for each instance for this dimension
            if (lvdistance == 0) {
                //for each instance in the table
                for (int lvi = 0; lvi < dmat.getElementCount(); lvi++) {
                    points[lvi][currentDimension] = 0.0f;
                }
            } else { //if the distance is not equal to 0, then
                //instances iterator
                for (int lvi = 0; lvi < dmat.getElementCount(); lvi++) {
                    //current dimension xi = (distance between the instance and the first pivot)^2+(distance between both pivots)^2
                    //								  -(distance between the instance and the secod pivot)^2)
                    //all divided by 2 times the (distance between both pivots)

                    float lvxi = (float) ((Math.pow(dmat.getDistance(lvchoosen[0], lvi), 2) +
                            Math.pow(dmat.getDistance(lvchoosen[0], lvchoosen[1]), 2) -
                            Math.pow(dmat.getDistance(lvi, lvchoosen[1]), 2)) /
                            (2 * dmat.getDistance(lvchoosen[0], lvchoosen[1])));

                    points[lvi][currentDimension] = lvxi;
                }

                //updating the distances table with equation 4 of Faloutsos' paper (in detail below)
                if (currentDimension < this.targetDimension - 1) {
                    this.updateDistances(dmat, points, currentDimension);
                }
            }

            //Increase the current dimension
            currentDimension++;
        }
    }

    private int[] chooseDistantObjects(DistanceMatrix dmat) {
        int[] choosen = new int[2];
        int x = 0, y = 1;
        for (int i = 0; i < dmat.getElementCount() - 1; i++) {
            for (int j = i + 1; j < dmat.getElementCount(); j++) {
                if (dmat.getDistance(x, y) < dmat.getDistance(i, j)) {
                    x = i;
                    y = j;
                }
            }
        }

        choosen[0] = x;
        choosen[1] = y;

        return choosen;
    }

    private void updateDistances(DistanceMatrix dmat, float[][] points, int currentDimension) {
        //for each instance
        for (int lvinst = 0; lvinst < dmat.getElementCount(); lvinst++) {
            //for each another instance
            for (int lvinst2 = lvinst + 1; lvinst2 < dmat.getElementCount(); lvinst2++) {
                float value = (float) (Math.sqrt(Math.abs(Math.pow(dmat.getDistance(lvinst, lvinst2), 2) -
                        Math.pow((points[lvinst][currentDimension] -
                        points[lvinst2][currentDimension]), 2))));

                dmat.setDistance(lvinst, lvinst2, value);
            }
        }
    }

    private int targetDimension; //The number of dimensions to reduce
}
