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
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.graph.Connectivity;
import visualizer.graph.Vertex;
import visualizer.view.Viewer;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ForceDirectLayout implements Runnable {

    /** Creates a new instance of ForceDirectLayout
     * 
     * @param graph
     * @param gv 
     */
    public ForceDirectLayout(Graph graph, Viewer gv) {
        this.graph = graph;
        this.gv = gv;
    }

    public void run() {
        Thread me = Thread.currentThread();

        int i = 0;
        while (relaxer == me) {
            this.relax();

            if ((i++) % 5 == 0) {
                try {
                    this.gv.updateImage();
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    synchronized void relax() {
        if (this.edges != null && this.vertex != null) {
            for (int i = 0; i < edges.size(); i++) {
                Edge e = edges.get(i);
                float vx = e.getTarget().getX() - e.getSource().getX();
                float vy = e.getTarget().getY() - e.getSource().getY();
                float len = (float) Math.sqrt(vx * vx + vy * vy);

                float edgeLen = e.getLength();

                if (edgeLen != Edge.NO_SIZE && this.normalize) {
                    edgeLen = (((edgeLen - this.min_length) / (this.max_length - this.min_length)) *
                            (ForceDirectLayout.MAX_EDGE_SIZE - ForceDirectLayout.MIN_EDGE_SIZE)) +
                            ForceDirectLayout.MIN_EDGE_SIZE;
                } else {
                    edgeLen = ForceDirectLayout.MAX_EDGE_SIZE;
                }

                len = (len == 0) ? 0.0001f : len;
                float f = (edgeLen - len) / (len * 3.0f);
                float dx = f * vx;
                float dy = f * vy;

                e.getTarget().fdata.dx += dx;
                e.getTarget().fdata.dy += dy;
                e.getSource().fdata.dx += -dx;
                e.getSource().fdata.dy += -dy;
            }

            for (int i = 0; i < vertex.size(); i++) {
                Vertex v1 = vertex.get(i);
                float dx = 0;
                float dy = 0;

                for (int j = 0; j < vertex.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    Vertex v2 = vertex.get(j);
                    float vx = v1.getX() - v2.getX();
                    float vy = v1.getY() - v2.getY();
                    float len = vx * vx + vy * vy;
                    if (len == 0) {
                        dx += Math.random();
                        dy += Math.random();
                    } else if (len < 100 * 100) {
                        dx += vx / len;
                        dy += vy / len;
                    }
                }
                float dlen = dx * dx + dy * dy;
                if (dlen > 0) {
                    dlen = (float) Math.sqrt(dlen) / 2;
                    v1.fdata.dx += dx / dlen;
                    v1.fdata.dy += dy / dlen;
                }
            }

            for (int i = 0; i < vertex.size(); i++) {
                Vertex v = vertex.get(i);

                float x = v.getX() + Math.max(-5.0f, Math.min(5.0f, v.fdata.dx));
                float y = v.getY() + Math.max(-5.0f, Math.min(5.0f, v.fdata.dy));

//                if (x < Vertex.getRayBase()*2) {
//                    x = Vertex.getRayBase()*2;
//                } else if (x > this.graphpanel.getSize().width-Vertex.getRayBase()*2) {
//                    x = this.graphpanel.getSize().width-Vertex.getRayBase()*2;
//                }
//
//                if (y < Vertex.getRayBase()*2) {
//                    y = Vertex.getRayBase()*2;
//                } else if (y > this.graphpanel.getSize().height-Vertex.getRayBase()*2) {
//                    y = this.graphpanel.getSize().height-Vertex.getRayBase()*2;
//                }

                v.setX(x);
                v.setY(y);

                v.fdata.dx /= 2;
                v.fdata.dy /= 2;
            }
        }
    }

    public void start(Connectivity connectivity) {
        if (connectivity != null) {
            this.edges = connectivity.getEdges();
            this.vertex = this.graph.getVertex();

            //finding the min and max edge lengths
            this.max_length = Float.MIN_VALUE;
            this.min_length = Float.MAX_VALUE;
            for (Edge e : this.edges) {
                if (e.getLength() > this.max_length) {
                    this.max_length = e.getLength();
                }

                if (e.getLength() < this.min_length) {
                    this.min_length = e.getLength();
                }
            }

            //indicates if the edge length can be normalized
            this.normalize = (this.max_length > this.min_length);

            relaxer = new Thread(this);
            relaxer.start();
        }
    }

    public void stop() {
        relaxer = null;

        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension d = tk.getScreenSize();
        this.graph.normalizeVertex(this.graph.getVertex().get(0).getRayBase() * 5 + 10, ((float) (d.getHeight())) / 1.65f);

        this.gv.updateImage();
    }

    private static final float MAX_EDGE_SIZE = 20.0f;
    private static final float MIN_EDGE_SIZE = 5.0f;
    private boolean normalize = true;
    private float max_length;
    private float min_length;
    private ArrayList<Edge> edges;
    private ArrayList<Vertex> vertex;
    private Graph graph;
    private Viewer gv;
    private Thread relaxer;
}
