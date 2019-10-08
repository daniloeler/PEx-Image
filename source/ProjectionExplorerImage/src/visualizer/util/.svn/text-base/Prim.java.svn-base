/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx), based on the code presented 
 * in:
 * 
 * http://snippets.dzone.com/user/scvalex/tag/prim
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

package visualizer.util;

import java.util.ArrayList;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Prim {

    public Prim(DistanceMatrix dmat) {
        this.n = dmat.getElementCount();
        this.weight = new float[this.n][];
        this.inTree = new boolean[this.n];
        this.d = new float[this.n];
        this.whoTo = new int[this.n];

        //creating the edges
        for (int i = 0; i < dmat.getElementCount(); i++) {
            this.weight[i] = new float[this.n];
            for (int j = 0; j < dmat.getElementCount(); j++) {
                float dist = dmat.getDistance(i, j);
                if (dist < 0.0f) {
                    this.weight[i][j] = 0.0f;
                } else {
                    this.weight[i][j] = dist;
                }
            }
        }
    }

    public ArrayList<Prim.Edge> prim() {
        ArrayList<Prim.Edge> edges = new ArrayList<Prim.Edge>();

        /* Initialise d with infinity */
        for (int i = 0; i < n; ++i) {
            d[i] = Float.MAX_VALUE;
        }

        /* Mark all nodes as NOT beeing in the minimum spanning tree */
        for (int i = 0; i < n; ++i) {
            inTree[i] = false;
        }

        /* Add the first node to the tree */
        inTree[0] = true;
        this.updateDistances(0);

        for (int treeSize = 1; treeSize < n; ++treeSize) {
            /* Find the node with the smallest distance to the tree */
            int min = -1;
            for (int i = 0; i < n; ++i) {
                if (!inTree[i]) {
                    if ((min == -1) || (d[min] > d[i])) {
                        min = i;
                    }
                }
            }

            /* And add it */
            edges.add(new Edge(whoTo[min], min, d[min]));
            inTree[min] = true;

            this.updateDistances(min);
        }

        return edges;
    }

    public static class Edge {

        public Edge(int node1, int node2, float len) {
            this.node1 = node1;
            this.node2 = node2;
            this.len = len;
        }

        public int node1;
        public int node2;
        public float len;
    }

    /* updateDistances(int target)
    should be called immediately after target is added to the tree;
    updates d so that the values are correct (goes through target's
    neighbours making sure that the distances between them and the tree
    are indeed minimum)
     */
    private void updateDistances(int target) {
        for (int i = 0; i < n; ++i) {
            if ((weight[target][i] != 0.0f) && (d[i] > weight[target][i])) {
                d[i] = weight[target][i];
                whoTo[i] = target;
            }
        }
    }

    private int n; /* The number of nodes in the graph */

    private float weight[][]; /* weight[i][j] is the distance between node i and node j;
    if there is no path between i and j, weight[i][j] should
    be 0 */

    private boolean inTree[]; /* inTree[i] is 1 if the node i is already in the minimum
    spanning tree; 0 otherwise*/

    private float d[]; /* d[i] is the distance between node i and the minimum spanning
    tree; this is initially infinity (100000); if i is already in
    the tree, then d[i] is undefined;
    this is just a temporary variable. It's not necessary but speeds
    up execution considerably (by a factor of n) */

    private int whoTo[]; /* whoTo[i] holds the index of the node i would have to be
    linked to in order to get a distance of d[i] */

//    public Prim(DistanceMatrix dmat) {
//        this.weight = 0;
//        this.edgeMap = new ArrayList<Prim.Edge>();
//        this.nodeMap = new TreeMap<Integer, Prim.Node>();
//
//        for (int i = 0; i < dmat.getElementCount() - 1; i++) {
//            for (int j = (dmat.getElementCount() - 1); j > i; j--) {
//                this.addEdge(i, j, dmat.getDistance(i, j));
//            }
//        }
//
//        java.util.Collections.sort(this.edgeMap);
//    }
//
//    private void addEdge(int node1, int node2, float cost) {
//        Prim.Edge newEdge = new Prim.Edge(node1, node2, cost);
//
//        this.weight += newEdge.len;
//        edgeMap.add(newEdge);
//
//        Prim.Node n1 = new Prim.Node();
//        n1.ID = newEdge.node1;
//        n1.setID = newEdge.node1;
//
//        Prim.Node n2 = new Prim.Node();
//        n2.ID = newEdge.node2;
//        n2.setID = newEdge.node2;
//
//        if (!nodeMap.containsKey(node1)) {
//            nodeMap.put(node1, n1);
//        }
//
//        if (!nodeMap.containsKey(node2)) {
//            nodeMap.put(node2, n2);
//        }
//    }
//
//    public ArrayList<Prim.Edge> prim() {
//        //implements Prim's MST algorithm--useful for dense trees
//        this.weight = 0;
//        ArrayList<Prim.Edge> newEdgeMap = new ArrayList<Prim.Edge>();
//
//        //keep track of MST by putting all nodes in set 1 as we come to them
//        //and find the shortest path inside the MST so far to outside of it
//
//        long numNodes = 1;//count how many nodes we've visited
//        while (numNodes < nodeMap.size()) {
//            int i = 0;
//            Prim.Edge e = this.edgeMap.get(i++);
//            Prim.Node n1 = this.nodeMap.get(e.node1);
//            Prim.Node n2 = this.nodeMap.get(e.node2);
//
//            //find shortest path from current MST to outside of it
//            while ((n1.setID == 1 && n2.setID == 1) ||
//                    (n2.setID != 1 && n1.setID != 1) && i < this.edgeMap.size()) {
//                e = this.edgeMap.get(i++);
//                n1 = this.nodeMap.get(e.node1);
//                n2 = this.nodeMap.get(e.node2);
//            }
//
//            //if we found an edge, add it, put nodes in set 1
//            if (i < this.edgeMap.size()) {
//                newEdgeMap.add(e);
//
//                this.edgeMap.remove(e);
//                n1.setID = 1;
//                n2.setID = 1;
//                weight += e.len;
//            }
//            numNodes++;
//        }
//
//        java.util.Collections.sort(newEdgeMap);
//
//        return newEdgeMap;
//    }
//
//    public static class Node {
//
//        @Override
//        public String toString() {
//            return ID + " " + setID;
//        }
//
//        public int ID;
//        public int setID;
//    }
//
//    public static class Edge implements Comparable {
//
//        public Edge(int node1, int node2, float cost) {
//            if (node1 < node2) {
//                this.node1 = node1;
//                this.node2 = node2;
//            } else {
//                this.node1 = node2;
//                this.node2 = node1;
//            }
//            this.len = cost;
//        }
//
//        public int compareTo(Object o) {
//            if ((this.len - ((Edge) o).len) < 0) {
//                return -1;
//            } else if ((this.len - ((Edge) o).len) > 0) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//
//        @Override
//        public String toString() {
//            return node1 + " " + node2 + " " + len;
//        }
//
//        public int node1;
//        public int node2;
//        public float len;
//    }
//
//    private int weight = 0;
//    private ArrayList<Prim.Edge> edgeMap;
//    private TreeMap<Integer, Prim.Node> nodeMap;
//}
}
