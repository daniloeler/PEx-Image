/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx), based on the code presented 
 * in:
 * 
 * http://prefuse.org/
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
 * of the original code is Pedro Vilela.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.forcelayout;

import visualizer.graph.Vertex;

/**
 *
 * @author Pedro Vilela
 */
public class SpringForce extends Force {

    private float[] params;
    private static String[] pnames = new String[]{"SpringCoefficient", "DefaultSpringLength"};
    public static final float DEFAULT_SPRING_COEFF = 1E-4f;
    public static final float DEFAULT_SPRING_LENGTH = 100;
    public static final int SPRING_COEFF = 0;
    public static final int SPRING_LENGTH = 1;
    private ForceSimulator fsim;
    public SpringForce(float springCoeff, float defaultLength) {
        params = new float[]{springCoeff, defaultLength};
    } //

    /**
     * Constructs a new SpringForce instance.
     */
    public SpringForce() {
        this(DEFAULT_SPRING_COEFF, DEFAULT_SPRING_LENGTH);
    }

    @Override
    public boolean isSpringForce() {
        return true;
    }

    public String[] getParameterNames() {
        return pnames;
    }

    /**
     * Initialize this force function.
     * Subclasses should override this method with any needed initialization.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        this.fsim = fsim;
    } //    

    /**
     * Calculates the force ArrayList acting on the items due to the given spring.
     * @param s the Spring for which to compute the force
     */
    @Override
    public void getForce(Spring s) {
        Vertex item1 = s.item1;
        Vertex item2 = s.item2;
        float length = (s.length < 0 ? params[SPRING_LENGTH] : s.length);
        float x1 = item1.fdata.location[0], y1 = item1.fdata.location[1];
        float x2 = item2.fdata.location[0], y2 = item2.fdata.location[1];
        float dx = x2 - x1, dy = y2 - y1;
        float r = (float) Math.sqrt(dx * dx + dy * dy);
        if (r == 0.0) {
            dx = ((float) Math.random() - 0.5f) / 50.0f;
            dy = ((float) Math.random() - 0.5f) / 50.0f;
            r = (float) Math.sqrt(dx * dx + dy * dy);
        }
        float d = r - length;
        float coeff = (s.coeff < 0 ? params[SPRING_COEFF] : s.coeff) * d / r;
        item1.fdata.force[0] += coeff * dx;
        item1.fdata.force[1] += coeff * dy;
        item2.fdata.force[0] += -coeff * dx;
        item2.fdata.force[1] += -coeff * dy;
    }

}
