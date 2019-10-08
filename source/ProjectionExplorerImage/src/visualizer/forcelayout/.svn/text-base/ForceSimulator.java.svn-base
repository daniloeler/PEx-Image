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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import visualizer.graph.Vertex;

/**
 *
 * @author Pedro Vilela
 */
public class ForceSimulator {

    private Set items;
    private Set springs;
    private Force[] iforces;
    private Force[] sforces;
    private int iflen,  sflen;
    private RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();
    private float speedLimit = 1.0f;
    public ForceSimulator() {
        iforces = new Force[5];
        sforces = new Force[5];
        iflen = 0;
        sflen = 0;
        items = new HashSet();
        springs = new HashSet();
    }

    public float getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(float limit) {
        speedLimit = limit;
    }

    public void clear() {
        items.clear();
        Iterator siter = springs.iterator();
        Spring.SpringFactory f = Spring.getFactory();
        while (siter.hasNext()) {
            f.reclaim((Spring) siter.next());
        }
        springs.clear();
    }

    public void addForce(Force f) {
        if (f.isItemForce()) {
            if (iforces.length == iflen) {
                // resize necessary
                Force[] newf = new Force[iflen + 10];
                System.arraycopy(iforces, 0, newf, 0, iforces.length);
                iforces = newf;
            }
            iforces[iflen++] = f;  //store Force f
        }
        if (f.isSpringForce()) {
            if (sforces.length == sflen) {
                // resize necessary
                Force[] newf = new Force[sflen + 10];
                System.arraycopy(sforces, 0, newf, 0, sforces.length);
                sforces = newf;
            }
            sforces[sflen++] = f;  //store Force f
        }
    }

    public Force[] getForces() {
        Force[] rv = new Force[iflen + sflen];
        System.arraycopy(iforces, 0, rv, 0, iflen);
        System.arraycopy(sforces, 0, rv, iflen, sflen);
        return rv;
    }

    public void addItem(Vertex item) {
        items.add(item);
    }

    public boolean removeItem(Vertex item) {
        return items.remove(item);
    }

    public Iterator getItems() {
        return items.iterator();
    }

    public Spring addSpring(Vertex item1, Vertex item2) {
        return addSpring(item1, item2, -1.f, -1.f);
    }

    public Spring addSpring(Vertex item1, Vertex item2, float length) {
        return addSpring(item1, item2, -1.f, length);
    }

    public Spring addSpring(Vertex item1, Vertex item2, float coeff, float length) {
        if (item1 == null || item2 == null) {
            throw new IllegalArgumentException("ForceItems must be non-null");
        }
        Spring s = Spring.getFactory().getSpring(item1, item2, coeff, length);
        springs.add(s);
        return s;
    }

    public boolean removeSpring(Spring s) {
        return springs.remove(s);
    }

    public Iterator getSprings() {
        return springs.iterator();
    }

    public void runSimulator(long timestep) {
        accumulate();
        integrator.integrate(this, timestep);
    }

    /**
     * Accumulate all forces acting on the items in this simulation
     */
    public void accumulate() {
        for (int i = 0; i < iflen; i++) {
            iforces[i].init(this);
        }
        for (int i = 0; i < sflen; i++) {
            sforces[i].init(this);
        }
        Iterator itemIter = items.iterator();
        while (itemIter.hasNext()) {
            Vertex vertex = (Vertex) itemIter.next();
            vertex.fdata.force[0] = 0.0f;
            vertex.fdata.force[1] = 0.0f;
            for (int i = 0; i < iflen; i++) {
                iforces[i].getForce(vertex);
            }
        }
        Iterator springIter = springs.iterator();
        while (springIter.hasNext()) {
            Spring s = (Spring) springIter.next();
            for (int i = 0; i < sflen; i++) {
                sforces[i].getForce(s);
            }
        }
    }

}
