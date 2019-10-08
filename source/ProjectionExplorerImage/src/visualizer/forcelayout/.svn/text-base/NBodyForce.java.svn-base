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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.Vertex;

/**
 *
 * @author Pedro Vilela
 */
public class NBodyForce extends Force {

    private float[] params;
    private static String[] pnames = new String[]{"GravitationalConstant",
        "MinimumDistance", "BarnesHutTheta"
    };
    public static final float DEFAULT_GRAV_CONSTANT = -0.4f;
    public static final float DEFAULT_MIN_DISTANCE = -1f;
    public static final float DEFAULT_THETA = 0.9f;
    public static final int GRAVITATIONAL_CONST = 0;
    public static final int MIN_DISTANCE = 1;
    public static final int BARNES_HUT_THETA = 1;
    private float xMin,  xMax,  yMin,  yMax;
    private QuadTreeNodeFactory factory = new QuadTreeNodeFactory();
    private QuadTreeNode root;
    private Random rand = new Random(12345678L); // deterministic randomness
    public NBodyForce() {
        this(DEFAULT_GRAV_CONSTANT, DEFAULT_MIN_DISTANCE, DEFAULT_THETA);
    }

    public NBodyForce(float gravConstant, float minDistance, float theta) {
        params = new float[]{gravConstant, minDistance, theta};
        root = factory.getQuadTreeNode();
    }

    @Override
    public boolean isItemForce() {
        return true;
    }

    public String[] getParameterNames() {
        return pnames;
    }

    /**
     * Set the bounds of the region for which to compute the n-body simulation
     * @param xMin the minimum x-coordinate
     * @param yMin the minimum y-coordinate
     * @param xMax the maximum x-coordinate
     * @param yMax the maximum y-coordinate
     */
    private void setBounds(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    /**
     * Clears the quadtree of all entries.
     */
    public void clear() {
        clearHelper(root);
        root = factory.getQuadTreeNode();
    } //

    private void clearHelper(QuadTreeNode n) {
        for (int i = 0; i < n.children.length; i++) {
            if (n.children[i] != null) {
                clearHelper(n.children[i]);
            }
        }
        factory.reclaim(n);
    }

    /**
     * Initialize the simulation with the provided enclosing simulation. After
     * this call has been made, the simulation can be queried for the
     * n-body force on a given item.
     * @param fsim the enclosing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        clear(); // clear internal state

        // compute and squarify bounds of quadtree
        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE;

        Iterator itemIter = fsim.getItems();
        while (itemIter.hasNext()) {
            Vertex vertex = (Vertex) itemIter.next();
            int x = (int) Math.round(vertex.fdata.location[0]);
            int y = (int) Math.round(vertex.fdata.location[1]);
            if (x < x1) {
                x1 = x;
            }
            if (y < y1) {
                y1 = y;
            }
            if (x > x2) {
                x2 = x;
            }
            if (y > y2) {
                y2 = y;
            }
        }

        int dx = x2 - x1, dy = y2 - y1;
        if (dx > dy) {
            y2 = y1 + dx;
        } else {
            x2 = x1 + dy;
        }

        setBounds(x1, y1, x2, y2);

        // insert items into quadtree
        itemIter = fsim.getItems();
        while (itemIter.hasNext()) {
            Vertex vertex = (Vertex) itemIter.next();
            insert(vertex);
        }

        // calculate magnitudes and centers of mass
        calcMass(root);
    } //

    /**
     * Inserts an item into the quadtree.
     * @param item the ForceItem to add.
     * @throws IllegalStateException if the current location of the item is
     *  outside the bounds of the quadtree
     */
    public void insert(Vertex vertex) {
        // insert item into the quadtrees
        try {
            insert(vertex, root, xMin, yMin, xMax, yMax);
        } catch (StackOverflowError ex) {
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insert(Vertex p, QuadTreeNode n, float x1, float y1, float x2, float y2) {
        // try to insert particle p at node n in the quadtree
        // by construction, each leaf will contain either 1 or 0 particles
        if (n.hasChildren) {
            // n contains more than 1 particle
            insertHelper(p, n, x1, y1, x2, y2);
        } else if (n.value != null) {
            // n contains 1 particle
            if (isSameLocation(n.value, p)) {
                insertHelper(p, n, x1, y1, x2, y2);
            } else {
                Vertex v = n.value;
                n.value = null;
                insertHelper(v, n, x1, y1, x2, y2);
                insertHelper(p, n, x1, y1, x2, y2);
            }
        } else {
            // n is empty, so is a leaf
            n.value = p;
        }
    }

    private static boolean isSameLocation(Vertex f1, Vertex f2) {
        float dx = Math.abs(f1.fdata.location[0] - f2.fdata.location[0]);
        float dy = Math.abs(f1.fdata.location[1] - f2.fdata.location[1]);
        return (dx < 0.01 && dy < 0.01);
    }

    private void insertHelper(Vertex p, QuadTreeNode n,
            float x1, float y1, float x2, float y2) {
        float x = p.fdata.location[0], y = p.fdata.location[1];
        float splitx = x1 + ((x2 - x1) / 2);
        float splity = y1 + ((y2 - y1) / 2);
        int i = (x >= splitx ? 1 : 0) + (y >= splity ? 2 : 0);
        // create new child node, if necessary
        if (n.children[i] == null) {
            n.children[i] = factory.getQuadTreeNode();
            n.hasChildren = true;
        }
        // update bounds
        if (i == 1 || i == 3) {
            x1 = splitx;
        } else {
            x2 = splitx;
        }
        if (i > 1) {
            y1 = splity;
        } else {
            y2 = splity;
        }
        // recurse
        insert(p, n.children[i], x1, y1, x2, y2);
    }

    private void calcMass(QuadTreeNode n) {
        float xcom = 0, ycom = 0;
        n.mass = 0;
        if (n.hasChildren) {
            for (int i = 0; i < n.children.length; i++) {
                if (n.children[i] != null) {
                    calcMass(n.children[i]);
                    n.mass += n.children[i].mass;
                    xcom += n.children[i].mass * n.children[i].com[0];
                    ycom += n.children[i].mass * n.children[i].com[1];
                }
            }
        }
        if (n.value != null) {
            n.mass += n.value.fdata.mass;
            xcom += n.value.fdata.mass * n.value.fdata.location[0];
            ycom += n.value.fdata.mass * n.value.fdata.location[1];
        }
        n.com[0] = xcom / n.mass;
        n.com[1] = ycom / n.mass;
    }

    public static long time = 0L;
    public static long timeant = 0L;
    /**
     * Calculates the force ArrayList acting on the given item.
     * @param item the ForceItem for which to compute the force
     */
    @Override
    public void getForce(Vertex vertex) {
        try {
            //time = System.nanoTime();
            //System.out.println(time - timeant);
            //timeant =  time;
            forceHelper(vertex, root, xMin, yMin, xMax, yMax);
        } catch (StackOverflowError ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void forceHelper(Vertex vertex, QuadTreeNode n,
            float x1, float y1, float x2, float y2) {
        float dx = n.com[0] - vertex.fdata.location[0];
        float dy = n.com[1] - vertex.fdata.location[1];
        float r = (float) Math.sqrt(dx * dx + dy * dy);
        boolean same = false;
        if (r == 0.0) {
            // if items are in the exact same place, add some noise
            dx = (rand.nextFloat() - 0.5f) / 50.0f;
            dy = (rand.nextFloat() - 0.5f) / 50.0f;
            r = (float) Math.sqrt(dx * dx + dy * dy);
            same = true;
        }
        boolean minDist = params[MIN_DISTANCE] > 0f && r > params[MIN_DISTANCE];

        // the Barnes-Hut approximation criteria is if the ratio of the
        // size of the quadtree box to the distance between the point and
        // the box's center of mass is beneath some threshold theta.
        if ((!n.hasChildren && n.value != vertex) ||
                (!same && (x2 - x1) / r < params[BARNES_HUT_THETA])) {
            if (minDist) {
                return;
            }
            // either only 1 particle or we meet criteria
            // for Barnes-Hut approximation, so calc force
            float v = params[GRAVITATIONAL_CONST] * vertex.fdata.mass * n.mass / (r * r * r);
            vertex.fdata.force[0] += v * dx;
            vertex.fdata.force[1] += v * dy;
        } else if (n.hasChildren) {
            // recurse for more accurate calculation
            float splitx = x1 + ((x2 - x1) / 2);
            float splity = y1 + ((y2 - y1) / 2);
            for (int i = 0; i < n.children.length; i++) {
                if (n.children[i] != null) {
                    forceHelper(vertex, n.children[i],
                            (i == 1 || i == 3 ? splitx : x1), (i > 1 ? splity : y1),
                            (i == 1 || i == 3 ? x2 : splitx), (i > 1 ? y2 : splity));
                }
            }
            if (minDist) {
                return;
            }
            if (n.value != null && n.value != vertex) {
                float v = params[GRAVITATIONAL_CONST] * vertex.fdata.mass * n.value.fdata.mass / (r * r * r);
                vertex.fdata.force[0] += v * dx;
                vertex.fdata.force[1] += v * dy;
            }
        }
    }

    /**
     * Represents a node in the quadtree.
     */
    public final class QuadTreeNode {

        boolean hasChildren = false;
        float mass; // total mass held by this node
        float[] com; // center of mass of this node
        Vertex value; // ForceItem in this node, null if node has children
        QuadTreeNode[] children; // children nodes
        public QuadTreeNode() {
            com = new float[]{0.0f, 0.0f};
            children = new QuadTreeNode[4]; //creates an array of QuadTreeNode
        }

    }

    /**
     * Helper class to minimize number of object creations across multiple
     * uses of the quadtree.
     */
    public final class QuadTreeNodeFactory {

        private int maxNodes = 10000;
        private ArrayList nodes = new ArrayList();
        public QuadTreeNode getQuadTreeNode() {
            if (nodes.size() > 0) {
                return (QuadTreeNode) nodes.remove(nodes.size() - 1);
            } else {
                return new QuadTreeNode();
            }
        }

        public void reclaim(QuadTreeNode n) {
            n.mass = 0;
            n.com[0] = 0.0f;
            n.com[1] = 0.0f;
            n.value = null;
            n.hasChildren = false;
            Arrays.fill(n.children, null);
            if (nodes.size() < maxNodes) {
                nodes.add(n);
            }
        }

    }

}
