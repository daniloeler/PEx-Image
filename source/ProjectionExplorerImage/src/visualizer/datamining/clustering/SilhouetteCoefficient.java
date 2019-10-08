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

package visualizer.datamining.clustering;

import java.io.IOException;
import java.util.ArrayList;
import visualizer.matrix.Matrix;
import visualizer.matrix.Vector;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class SilhouetteCoefficient {

    public float average(float[] silhouette) throws IOException {
        float s = 0.0f;
        for (int i = 0; i < silhouette.length; i++) {
            s += silhouette[i];
        }

        return s / silhouette.length;
    }
    
    public static ArrayList<Float> getClusters(float[] cdata) {
        
        ArrayList<Float> cdata_index = new ArrayList<>();
        for (int i = 0; i < cdata.length; i++) {
            if (!cdata_index.contains(cdata[i])) {
                cdata_index.add(cdata[i]);
            }
        }
        
        return cdata_index;
    }

    public float[] execute(Matrix matrix, Dissimilarity diss) throws IOException {
        float[] cdata = matrix.getClassData();
        ArrayList<Float> cdata_index = SilhouetteCoefficient.getClusters(cdata);
        
        if( cdata_index.size() <= 1 ) {
            throw new IOException("Only one cluster found. It is not possible to " +
                    "calculate the Silhouette coefficient.");
        }

        //store the clusters (the vector ids)
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<>();
        for (int i = 0; i < cdata_index.size(); i++) {
            clusters.add(new ArrayList<>());
        }

        //store the clusters instance belongs to
        int[] cluster_id = new int[cdata.length];
        for (int i = 0; i < cdata.length; i++) {
            int index = cdata_index.indexOf(cdata[i]);
            clusters.get(index).add(i);
            cluster_id[i] = index;
        }

        float[] s = new float[cdata.length];

        for (int i = 0; i < s.length; i++) {
            
            
            //testing if the cluster is a singleton
            if( clusters.get(cluster_id[i]).size() > 1 ) {
                float a = average(matrix, clusters.get(cluster_id[i]), matrix.getRow(i), diss);

                float b = Float.POSITIVE_INFINITY;
                for (int j = 0; j < clusters.size(); j++) {
                    if (j == cluster_id[i]) {
                        continue;
                    }

                    b = Math.min(b, average(matrix, clusters.get(j), matrix.getRow(i), diss));
                }

                s[i] = (b - a) / (Math.max(a, b));
            } else {
                //if it is a singleton, s <- 0
                s[i] = 0.0f;
            }
        }

        return s;
    }

    private float average(Matrix matrix, ArrayList<Integer> cluster, Vector vector, Dissimilarity diss) {
        float a = 0.0f;

        for (Integer cluster1 : cluster) {
            a += diss.calculate(vector, matrix.getRow(cluster1));
        }

        return a / cluster.size();
    }

}
