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

import java.util.ArrayList;
import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.graph.Connectivity;
import visualizer.graph.Vertex;
import visualizer.view.Viewer;

/**
 *
 * @author Pedro Vilela
 */
public class ForceLayout implements Runnable {

    /** Creates a new instance of ForceLayout
     * @param graph
     * @param gv 
     */
    public ForceLayout(Graph graph, Viewer gv) {
        this.graph = graph;
        this.vertices = graph.getVertex();
        this.gv = gv;
        this.fsim = new ForceSimulator();
        fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
        fsim.addForce(new SpringForce(1E-4f, 75f));
        fsim.addForce(new DragForce(-0.005f));
    }

    public void run() {
        Thread me = Thread.currentThread();

        int i = 0;
        while (relaxer == me) {
            this.relax();

            if ((i++) % 5 == 0) {
                this.gv.updateImage();
            }
        }
    }

    synchronized void relax() {
        fsim.runSimulator(15);
        updateLocation();
        this.gv.updateImage();
    }

    public void start(Connectivity connectivity) {
        this.edges = connectivity.getEdges();

        for (Vertex v : vertices) {
            v.fdata.mass = MASS;
            v.fdata.location[0] = v.getX();
            v.fdata.location[1] = v.getY();
            fsim.addItem(v);
        }

        for (Edge e : edges) {
            Vertex v1 = e.getSource();
            Vertex v2 = e.getTarget();
            float coeff = COEF;
            float slen = LENGTH;
            fsim.addSpring(v1, v2, (coeff >= 0 ? coeff : -1.f), (slen >= 0 ? slen : -1.f));
        }

        relaxer = new Thread(this);
        relaxer.start();
    }

    public void updateLocation() {
        for (Vertex v : vertices) {
            v.setX(v.fdata.location[0]);
            v.setY(v.fdata.location[1]);
        }
    }

    public void stop() {
        relaxer.interrupt();
        relaxer = null;

        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension d = tk.getScreenSize();
        this.graph.normalizeVertex(this.graph.getVertex().get(0).getRayBase() * 5 + 10, ((float) (d.getHeight())) / 1.65f);

        this.gv.updateImage();
    }

    public static final float MASS = 1.0f;
    public static final float LENGTH = 1.0f;
    public static final float COEF = -1.0f;
    private Graph graph;
    private ArrayList<Edge> edges;
    private ArrayList<Vertex> vertices;
    private Viewer gv;
    private Thread relaxer;
    private ForceSimulator fsim;
}
