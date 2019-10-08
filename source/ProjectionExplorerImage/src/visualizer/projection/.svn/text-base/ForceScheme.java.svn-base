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

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Euclidean;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ForceScheme {

    public ForceScheme(float fractionDelta, int numberPoints) {
        this.fractionDelta = fractionDelta;

        //Create the indexes and shuffle them
        ArrayList<Integer> index_aux = new ArrayList<Integer>();
        for (int i = 0; i < numberPoints; i++) {
            index_aux.add(i);
        }

        this.index = new int[numberPoints];
        for (int ind = 0, j = 0; j < this.index.length; ind += index_aux.size() / 10, j++) {
            if (ind >= index_aux.size()) {
                ind = 0;
            }

            this.index[j] = index_aux.get(ind);
            index_aux.remove(ind);
        }
    }

    public float iteration(DistanceMatrix dmat, float[][] projection) {
        float error = 0.0f;

        if (projection[0].length == 2) {
            //for each instance
            for (int ins1 = 0; ins1 < projection.length; ins1++) {
                int instance = this.index[ins1];

                //for each other instance
                for (int ins2 = 0; ins2 < projection.length; ins2++) {
                    int instance2 = this.index[ins2];

                    if (instance == instance2) {
                        continue;
                    }

                    //distance between projected instances
                    float x1x2 = (projection[instance2][0] - projection[instance][0]);
                    float y1y2 = (projection[instance2][1] - projection[instance][1]);
                    float dr2 = (float) Math.sqrt(x1x2 * x1x2 + y1y2 * y1y2);

                    if (dr2 < EPSILON) {
                        dr2 = EPSILON;
                    }

                    float drn = dmat.getDistance(instance, instance2);
                    float normdrn = (drn - dmat.getMinDistance()) /
                            (dmat.getMaxDistance() - dmat.getMinDistance());

                    //Calculating the (fraction of) delta
                    float delta = normdrn - dr2;
                    delta *= Math.abs(delta);
//                delta = (float) Math.sqrt(Math.abs(delta));
                    delta /= this.fractionDelta;

                    error += Math.abs(delta);

                    //moving ins2 -> ins1
                    projection[instance2][0] += delta * (x1x2 / dr2);
                    projection[instance2][1] += delta * (y1y2 / dr2);
                }
            }

            error /= (projection.length * projection.length) - projection.length;
        } else if (projection[0].length == 3) {
            //for each instance
            for (int ins1 = 0; ins1 < projection.length; ins1++) {
                int instance = this.index[ins1];

                //for each other instance
                for (int ins2 = 0; ins2 < projection.length; ins2++) {
                    int instance2 = this.index[ins2];

                    if (instance == instance2) {
                        continue;
                    }

                    //distance between projected instances
                    float x1x2 = (projection[instance2][0] - projection[instance][0]);
                    float y1y2 = (projection[instance2][1] - projection[instance][1]);
                    float z1z2 = (projection[instance2][2] - projection[instance][2]);

                    float dr3 = (float) Math.sqrt(x1x2 * x1x2 + y1y2 * y1y2 + z1z2 * z1z2);

                    if (dr3 < EPSILON) {
                        dr3 = EPSILON;
                    }

                    float drn = dmat.getDistance(instance, instance2);
                    float normdrn = (drn - dmat.getMinDistance()) /
                            (dmat.getMaxDistance() - dmat.getMinDistance());

                    //Calculating the (fraction of) delta
                    float delta = normdrn - dr3;
                    delta *= Math.abs(delta);
//                delta = (float) Math.sqrt(Math.abs(delta));
                    delta /= this.fractionDelta;

                    error += Math.abs(delta);

                    //moving ins2 -> ins1
                    projection[instance2][0] += delta * (x1x2 / dr3);
                    projection[instance2][1] += delta * (y1y2 / dr3);
                    projection[instance2][2] += delta * (z1z2 / dr3);
                }
            }

            error /= (projection.length * projection.length) - projection.length;
        }

        return error;
    }

    public float iteration(Matrix matrix, Dissimilarity diss, float[][] projection) {
        float error = 0.0f;

        if (projection[0].length == 2) {
            //for each instance
            for (int ins1 = 0; ins1 < projection.length; ins1++) {
                int instance = this.index[ins1];

                //for each other instance
                for (int ins2 = 0; ins2 < projection.length; ins2++) {
                    int instance2 = this.index[ins2];

                    if (instance == instance2) {
                        continue;
                    }

                    //distance between projected instances
                    float x1x2 = (projection[instance2][0] - projection[instance][0]);
                    float y1y2 = (projection[instance2][1] - projection[instance][1]);
                    float dr2 = (float) Math.sqrt(x1x2 * x1x2 + y1y2 * y1y2);

                    if (dr2 < EPSILON) {
                        dr2 = EPSILON;
                    }

                    float drn = diss.calculate(matrix.getRow(instance), matrix.getRow(instance2));
                    float normdrn = drn / 5.0f;

                    //Calculating the (fraction of) delta
                    float delta = normdrn - dr2;
                    delta *= Math.abs(delta);
//                delta = (float) Math.sqrt(Math.abs(delta));
                    delta /= this.fractionDelta;

                    error += Math.abs(delta);

                    //moving ins2 -> ins1
                    projection[instance2][0] += delta * (x1x2 / dr2);
                    projection[instance2][1] += delta * (y1y2 / dr2);
                }
            }

            error /= (projection.length * projection.length) - projection.length;
        } else if (projection[0].length == 3) {
            //for each instance
            for (int ins1 = 0; ins1 < projection.length; ins1++) {
                int instance = this.index[ins1];

                //for each other instance
                for (int ins2 = 0; ins2 < projection.length; ins2++) {
                    int instance2 = this.index[ins2];

                    if (instance == instance2) {
                        continue;
                    }

                    //distance between projected instances
                    float x1x2 = (projection[instance2][0] - projection[instance][0]);
                    float y1y2 = (projection[instance2][1] - projection[instance][1]);
                    float z1z2 = (projection[instance2][2] - projection[instance][2]);

                    float dr3 = (float) Math.sqrt(x1x2 * x1x2 + y1y2 * y1y2 + z1z2 * z1z2);

                    if (dr3 < EPSILON) {
                        dr3 = EPSILON;
                    }

                    float drn = diss.calculate(matrix.getRow(instance), matrix.getRow(instance2));
                    float normdrn = drn / 5.0f;

                    //Calculating the (fraction of) delta
                    float delta = normdrn - dr3;
                    delta *= Math.abs(delta);
//                delta = (float) Math.sqrt(Math.abs(delta));
                    delta /= this.fractionDelta;

                    error += Math.abs(delta);

                    //moving ins2 -> ins1
                    projection[instance2][0] += delta * (x1x2 / dr3);
                    projection[instance2][1] += delta * (y1y2 / dr3);
                    projection[instance2][2] += delta * (z1z2 / dr3);
                }
            }
        }

        return error;
    }

    public static void main(String[] args) {
        try {
            String filename = "D:\\My Documents\\FERNANDO\\Tese\\datasets\\quadrapeds-mammals_nc.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            float[][] projection = new float[matrix.getRowCount()][];

            for (int i = 0; i < projection.length; i++) {
                projection[i] = new float[2];
                projection[i][0] = (float) Math.random();
                projection[i][1] = (float) Math.random();
            }

            ForceScheme force = new ForceScheme(8.0f, matrix.getRowCount());

            for (int i = 0; i < 1; i++) {
                float error = force.iteration(matrix, new Euclidean(), projection);
                System.out.println(error);
            }

        } catch (IOException ex) {
            Logger.getLogger(ForceScheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private float fractionDelta;
    private int[] index;
    private static final float EPSILON = 0.0000001f;
}
