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

import java.util.Iterator;
import visualizer.graph.Vertex;

/**
 * Updates velocity and position data using the 4th-Order Runge-Kutta method.
 * It is slower but more accurate than other techniques such as Euler's Method.
 * The technique requires re-evaluating forces 4 times for a given timestep.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class RungeKuttaIntegrator {

    public void integrate(ForceSimulator sim, long timestep) {
        float speedLimit = sim.getSpeedLimit();
        float vx, vy, v, coeff;
        float[][] k, l;

        Iterator iter = sim.getItems();
        while (iter.hasNext()) {
            Vertex item = (Vertex) iter.next();
            coeff = timestep / item.fdata.mass;
            k = item.fdata.k;
            l = item.fdata.l;
            item.fdata.plocation[0] = item.fdata.location[0];
            item.fdata.plocation[1] = item.fdata.location[1];
            k[0][0] = timestep * item.fdata.velocity[0];
            k[0][1] = timestep * item.fdata.velocity[1];
            l[0][0] = coeff * item.fdata.force[0];
            l[0][1] = coeff * item.fdata.force[1];

            // Set the position to the new predicted position
            item.fdata.location[0] += 0.5f * k[0][0];
            item.fdata.location[1] += 0.5f * k[0][1];
        }

        // recalculate forces
        sim.accumulate();

        iter = sim.getItems();
        while (iter.hasNext()) {
            Vertex item = (Vertex) iter.next();
            coeff = timestep / item.fdata.mass;
            k = item.fdata.k;
            l = item.fdata.l;
            vx = item.fdata.velocity[0] + .5f * l[0][0];
            vy = item.fdata.velocity[1] + .5f * l[0][1];
            v = (float) Math.sqrt(vx * vx + vy * vy);
            if (v > speedLimit) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[1][0] = timestep * vx;
            k[1][1] = timestep * vy;
            l[1][0] = coeff * item.fdata.force[0];
            l[1][1] = coeff * item.fdata.force[1];

            // Set the position to the new predicted position
            item.fdata.location[0] = item.fdata.plocation[0] + 0.5f * k[1][0];
            item.fdata.location[1] = item.fdata.plocation[1] + 0.5f * k[1][1];
        }

        // recalculate forces
        sim.accumulate();

        iter = sim.getItems();
        while (iter.hasNext()) {
            Vertex item = (Vertex) iter.next();
            coeff = timestep / item.fdata.mass;
            k = item.fdata.k;
            l = item.fdata.l;
            vx = item.fdata.velocity[0] + .5f * l[1][0];
            vy = item.fdata.velocity[1] + .5f * l[1][1];
            v = (float) Math.sqrt(vx * vx + vy * vy);
            if (v > speedLimit) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[2][0] = timestep * vx;
            k[2][1] = timestep * vy;
            l[2][0] = coeff * item.fdata.force[0];
            l[2][1] = coeff * item.fdata.force[1];

            // Set the position to the new predicted position
            item.fdata.location[0] = item.fdata.plocation[0] + 0.5f * k[2][0];
            item.fdata.location[1] = item.fdata.plocation[1] + 0.5f * k[2][1];
        }

        // recalculate forces
        sim.accumulate();

        iter = sim.getItems();
        while (iter.hasNext()) {
            Vertex item = (Vertex) iter.next();
            coeff = timestep / item.fdata.mass;
            k = item.fdata.k;
            l = item.fdata.l;
            float[] p = item.fdata.plocation;
            vx = item.fdata.velocity[0] + l[2][0];
            vy = item.fdata.velocity[1] + l[2][1];
            v = (float) Math.sqrt(vx * vx + vy * vy);
            if (v > speedLimit) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[3][0] = timestep * vx;
            k[3][1] = timestep * vy;
            l[3][0] = coeff * item.fdata.force[0];
            l[3][1] = coeff * item.fdata.force[1];
            item.fdata.location[0] = p[0] + (k[0][0] + k[3][0]) / 6.0f + (k[1][0] + k[2][0]) / 3.0f;
            item.fdata.location[1] = p[1] + (k[0][1] + k[3][1]) / 6.0f + (k[1][1] + k[2][1]) / 3.0f;

            vx = (l[0][0] + l[3][0]) / 6.0f + (l[1][0] + l[2][0]) / 3.0f;
            vy = (l[0][1] + l[3][1]) / 6.0f + (l[1][1] + l[2][1]) / 3.0f;
            v = (float) Math.sqrt(vx * vx + vy * vy);
            if (v > speedLimit) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            item.fdata.velocity[0] += vx;
            item.fdata.velocity[1] += vy;
        }
    }

}
