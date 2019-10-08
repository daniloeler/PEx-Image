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
public abstract class Force {

    private float[] params;
    /**
     * Initialize this force function.
     * @param fsim the encompassing ForceSimulator
     */
    public abstract void init(ForceSimulator fsim);

    /**
     * Returns the number of parameters (e.g., gravitational constant or
     * spring force coefficient) affecting this force function. 
     * @return the number of parameters
     */
    public int getParameterCount() {
        return (params == null ? 0 : params.length);
    }

    /**
     * Returns the specified, numbered parameter.
     * @param i the index of the parameter to return
     * @return the parameter value
     */
    public float getParameter(int i) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return params[i];
        }
    }

    /**
     * Gets the text name of the requested parameter.
     * @param i the index of the parameter
     * @return a String containing the name of this parameter
     */
    public String getParameterName(int i) {
        String[] pnames = getParameterNames();
        if (i < 0 || pnames == null || i >= pnames.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return pnames[i];
        }
    }

    /**
     * Sets the specified parameter value.
     * @param i the index of the parameter
     * @param val the new value of the parameter
     */
    public void setParameter(int i, float val) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            params[i] = val;
        }
    }

    public abstract String[] getParameterNames();

    /**
     * Indicates if this force function will compute forces
     * on Spring instances.
     * @return true if this force function processes Spring instances 
     */
    public boolean isSpringForce() {
        return false;
    }

    /**
     * Indicates if this force function will compute forces
     * on ForceItem instances
     * @return true if this force function processes Force instances 
     */
    public boolean isItemForce() {
        return false;
    }

    /**
     * Updates the force calculation on the given ForceItem
     * @param item the ForceItem on which to compute updated forces
     */
    public void getForce(Vertex vertex) {

    }

    /**
     * Updates the force calculation on the given Spring. The ForceItems
     * attached to Spring will have their force values updated appropriately.
     * @param spring the Spring on which to compute updated forces
     */
    public void getForce(Spring spring) {

    }

}
