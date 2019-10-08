/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx), based on the code presented 
 * in:
 * 
 * http://www.ilpiola.it/roberto/jsammon/index_e.asp
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

package visualizer.projection.sammon;

import java.io.IOException;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class SammonMapping {

    /** Creates a new instance of SammonMapping */
    public SammonMapping() {
    }

    public float getMF() {
        return MF;
    }

    public void setMF(float MF) {
        this.MF = MF;
    }

    public float[][] createInitialProjection(Matrix matrix) throws IOException {
        float[][] points = matrix.toMatrix();

        float[][] projection = new float[points.length][];
        for (int i = 0; i < projection.length; i++) {
            projection[i] = new float[2];
        }

        int dimensions = points[0].length;
        float[] mins = new float[dimensions];
        float[] maxs = new float[dimensions];
        float[] normalized_averages = new float[dimensions];
        float[] normalized_variances = new float[dimensions];
        float[] averages = new float[dimensions];
        float[] variances = new float[dimensions];

        for (int i = 0; i < dimensions; i++) {
            mins[i] = points[0][i];
            maxs[i] = points[0][i];
            averages[i] = points[0][i];
            variances[i] = 0.0f;
            normalized_variances[i] = 0.0f;
        }

        float t = 0.0f;
        for (int i = 1; i < projection.length; i++) {
            if (dimensions != points[i].length) {
                throw new IOException("Corrupt data!");
            }

            for (int j = 0; j < dimensions; j++) {
                t = points[i][j];
                if (t < mins[j]) {
                    mins[j] = t;
                }
                if (t > maxs[j]) {
                    maxs[j] = t;
                }
                averages[j] += t;
            }
        }

        int best_one = 0, second_one = 0;

        for (int j = 0; j < dimensions; j++) {
            averages[j] = averages[j] / (float) projection.length;
            normalized_averages[j] = (averages[j] - mins[j]) / (maxs[j] - mins[j]);
        }

        for (int j = 0; j < dimensions; j++) {
            for (int i = 0; i < projection.length; i++) {
                t = points[i][j] - averages[j];
                variances[j] += t * t;
                t = (points[i][j] - mins[j]) / (maxs[j] - mins[j]) - normalized_averages[j];
                normalized_variances[j] += t * t;
            }
            variances[j] = variances[j] / (float) projection.length;
            normalized_variances[j] = normalized_variances[j] / (float) projection.length;
        }

        for (int j = 0; j < dimensions; j++) {
            if (normalized_variances[j] >= normalized_variances[best_one]) {
                second_one = best_one;
                best_one = j;
            } else if (normalized_variances[j] >= normalized_variances[second_one]) {
                second_one = j;
            }
        }

        for (int i = 0; i < projection.length; i++) {
            projection[i][0] = points[i][best_one];
            projection[i][1] = points[i][second_one];
        }

        return projection;
    }

    public float iteration(DistanceMatrix dmatRn, float[][] projection) throws IOException {
        int nrPoints = dmatRn.getElementCount();
        float sumSquareMeanError = 0;
        float error = 0; // Sammon error
        float sumDistRn = 0;
        float sumInDer1 = 0;
        float sumInDer2 = 0;
        float delta_pq = 0;
        float c = 0;

        //necessary to calculate the gradient
        float[][] projection_aux = new float[projection.length][];
        for (int i = 0; i < projection.length; i++) {
            projection_aux[i] = new float[2];
            projection_aux[i][0] = projection[i][0];
            projection_aux[i][1] = projection[i][1];
        }

        //computing the initial error
        for (int i = 0; i < nrPoints - 1; i++) {
            for (int j = i + 1; j < nrPoints; j++) {
                if (dmatRn.getDistance(i, j) < EPSILON) {
                    dmatRn.setDistance(i, j, EPSILON);
                }
                sumDistRn += dmatRn.getDistance(i, j); // I need of this for calculating the error
            }
        }

        c = (-2 / sumDistRn);

        for (int p = 0; p < nrPoints; p++) {
            for (int q = 0; q < 2; q++) {
                sumInDer1 = 0;
                sumInDer2 = 0;

                for (int j = 0; j < nrPoints; j++) {
                    if (j != p) {
                        float distPJ = this.distR2(projection_aux[p], projection_aux[j]);

                        sumInDer1 += ((dmatRn.getDistance(p, j) - distPJ) /
                                (dmatRn.getDistance(p, j) * distPJ)) *
                                (projection[p][q] - projection[j][q]);

                        sumInDer2 += (1 / (dmatRn.getDistance(p, j) * distPJ)) *
                                ((dmatRn.getDistance(p, j) - distPJ) -
                                (((Math.pow((projection[p][q] - projection[j][q]), 2) / distPJ)) *
                                (1 + ((dmatRn.getDistance(p, j) - distPJ) / distPJ))));
                    }
                }

                delta_pq = ((c * sumInDer1) / (Math.abs(c * sumInDer2)));
                projection[p][q] -= (MF * delta_pq);  // Ypq(m+1)
            }
        }

        for (int i = 0; i < dmatRn.getElementCount() - 1; i++) {
            for (int j = i + 1; j < dmatRn.getElementCount(); j++) {
                sumSquareMeanError += ((Math.pow((dmatRn.getDistance(i, j) - this.distR2(projection[i], projection[j])), 2)) /
                        dmatRn.getDistance(i, j));
            }
        }

        error = (1 / sumDistRn) * sumSquareMeanError;

        return error;
    }

    private float distR2(float[] pointA, float[] pointB) {
        float x1x2 = (pointA[0] - pointB[0]);
        float y1y2 = (pointA[1] - pointB[1]);
        float dist = (float) Math.sqrt(x1x2 * x1x2 + y1y2 * y1y2);

        if (dist > EPSILON) {
            return dist;
        } else {
            return EPSILON;
        }
    }

    private static final float EPSILON = 0.000001f;
    private float MF = 0.3f;   // Magic Factor
}
