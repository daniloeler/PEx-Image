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

import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class Projector {

    public abstract float[][] project(DistanceMatrix dmat);

    public void normalize(float[][] result) {
        int lvdimensions = result[0].length;
        int lvinstances = result.length;

        //for normalization
        float[] lvlowrange = new float[lvdimensions];
        float[] lvhighrange = new float[lvdimensions];

        //for each instance
        for (int lvins = 0; lvins < lvinstances; lvins++) {
            //for each attribute
            for (int lvfield = 0; lvfield < lvdimensions; lvfield++) {
                //if it is the first instance then assign the first value
                if (lvins == 0) {
                    lvlowrange[lvfield] = result[lvins][lvfield];
                    lvhighrange[lvfield] = result[lvins][lvfield];
                } //otherwise compare
                else {
                    lvlowrange[lvfield] = lvlowrange[lvfield] > result[lvins][lvfield] ? result[lvins][lvfield] : lvlowrange[lvfield];
                    lvhighrange[lvfield] = lvhighrange[lvfield] < result[lvins][lvfield] ? result[lvins][lvfield] : lvhighrange[lvfield];
                }
            }
        }

        //for each instance
        for (int lvins = 0; lvins < lvinstances; lvins++) {
            //for each attribute
            for (int lvfield = 0; lvfield < lvdimensions; lvfield++) {
                if ((lvhighrange[lvfield] - lvlowrange[lvfield]) > 0.0) {
                    result[lvins][lvfield] = (result[lvins][lvfield] - lvlowrange[lvfield]) /
                            (lvhighrange[lvfield] - lvlowrange[lvfield]);
                } else {
                    result[lvins][lvfield] = 0;
                }
            }
        }
    }

    public void normalize2D(float[][] projection) {
        //Os valores máximos e mínimos para cada coordenada
        float maxX = projection[0][0];
        float minX = projection[0][0];
        float maxY = projection[0][1];
        float minY = projection[0][1];

        //Os valores máximos e mínimos para X e Y
        for (int _ins = 1; _ins < projection.length; _ins++) {
            if (minX > projection[_ins][0]) {
                minX = projection[_ins][0];
            } else {
                if (maxX < projection[_ins][0]) {
                    maxX = projection[_ins][0];
                }
            }

            if (minY > projection[_ins][1]) {
                minY = projection[_ins][1];
            } else {
                if (maxY < projection[_ins][1]) {
                    maxY = projection[_ins][1];
                }
            }
        }

        //fazer Y proporcional a X
        float endY = (maxY - minY) / (maxX - minX);

        //for each position in the ArrayList ... normalize!
        for (int _ins = 0; _ins < projection.length; _ins++) {
            if (maxX - minX > 0.0) {
                projection[_ins][0] = (projection[_ins][0] - minX) / (maxX - minX);
            } else {
                projection[_ins][0] = 0;
            }
            if (maxY - minY > 0.0) {
                projection[_ins][1] = (projection[_ins][1] - minY) / ((maxY - minY) * endY);
            } else {
                projection[_ins][1] = 0;
            }
        }
    }

}
