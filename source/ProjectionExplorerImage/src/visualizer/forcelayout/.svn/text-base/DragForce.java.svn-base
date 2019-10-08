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
public class DragForce extends Force {

    private float[] params;
    private static String[] pnames = new String[]{"DragCoefficient"};
    public static final float DEFAULT_DRAG_COEFF = -0.01f;
    public static final int DRAG_COEFF = 0;
    public DragForce(float dragCoeff) {
        params = new float[]{dragCoeff};
    }

    public DragForce() {
        this(DEFAULT_DRAG_COEFF);
    }

    public void init(ForceSimulator fsim) {
    //do nothing
    }

    @Override
    public boolean isItemForce() {
        return true;
    }

    public String[] getParameterNames() {
        return pnames;
    }

    /**
     * Calculates the force ArrayList acting on the given item.
     * @param item the ForceItem for which to compute the force
     */
    @Override
    public void getForce(Vertex item) {
        item.fdata.force[0] += params[DRAG_COEFF] * item.fdata.velocity[0];
        item.fdata.force[1] += params[DRAG_COEFF] * item.fdata.velocity[1];
    }

}
