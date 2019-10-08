/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx), based on the code presented 
 * in:
 * 
 * http://snippets.dzone.com/tag/dijkstra
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Dijkstra {

    public Dijkstra(Pair[][] neighborhood, int nrNodes) {
        this.n = nrNodes;
        this.d = new float[this.n];

        //creating the adjascent matrix
        this.dist = new float[this.n][];

        for (int i = 0; i < neighborhood.length; i++) {
            this.dist[i] = new float[this.n];
            Arrays.fill(this.dist[i], 0.0f);
            for (int j = 0; j < neighborhood[i].length; j++) {
                float distance = neighborhood[i][j].value;

                if (distance > 0.0f) {
                    this.dist[i][neighborhood[i][j].index] = distance;
                }
            }
        }
    }

    public float[] execute(int source) {
        boolean visited[] = new boolean[this.n];

        for (int i = 0; i < this.n; ++i) {
            d[i] = Float.POSITIVE_INFINITY;
            visited[i] = false; // the i-th element has not yet been visited
        }

        this.d[source] = 0.0f;

        for (int k = 0; k < n; ++k) {
            int mini = -1;
            for (int i = 0; i < n; ++i) {
                if (!visited[i] && ((mini == -1) || (d[i] < d[mini]))) {
                    mini = i;
                }
            }

            visited[mini] = true;

            for (int i = 0; i < n; ++i) {
                if (dist[mini][i] > 0.0f) {
                    if (d[mini] + dist[mini][i] < d[i]) {
                        d[i] = d[mini] + dist[mini][i];
                    }
                }
            }
        }

        return this.d;
    }

    public static void main(String[] args) {
        try {
            String filename = "C:\\Documents and Settings\\paulovich\\Mijn documenten\\cbr-ilp-ir_gzip.dmat";
            DistanceMatrix dmat = new DistanceMatrix(filename);
            DistanceMatrix new_dmat = new DistanceMatrix(dmat.getElementCount());

            //creating a graph with its nearest neighbors
            Prim prim = new Prim(dmat);
            ArrayList<Prim.Edge> edges = prim.prim();

            ArrayList<ArrayList<Pair>> neigh_aux = new ArrayList<ArrayList<Pair>>();

            for (int i = 0; i < dmat.getElementCount(); i++) {
                neigh_aux.add(new ArrayList<Pair>());
            }

            for (Prim.Edge e : edges) {
                neigh_aux.get(e.node1).add(new Pair(e.node2, e.len));
                neigh_aux.get(e.node2).add(new Pair(e.node1, e.len));
            }

            Pair[][] neighborhood = new Pair[dmat.getElementCount()][];

            for (int i = 0; i < neigh_aux.size(); i++) {
                neighborhood[i] = new Pair[neigh_aux.get(i).size()];

                for (int j = 0; j < neigh_aux.get(i).size(); j++) {
                    neighborhood[i][j] = neigh_aux.get(i).get(j);
                }
            }

            Dijkstra d = new Dijkstra(neighborhood, dmat.getElementCount());

            for (int i = 0; i < dmat.getElementCount(); i++) {
                float[] dist = d.execute(i);

                for (int j = 0; j < dist.length; j++) {
                    new_dmat.setDistance(i, j, dist[j]);
                }
            }

            new_dmat.save(filename + "_new.dmat");
        } catch (IOException ex) {
            Logger.getLogger(Dijkstra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int n; // The number of nodes in the graph
    private float dist[][]; // dist[i][j] is the distance between node i and j; or 0 if there is no direct connection
    private float d[]; // d[i] is the length of the shortest path between the source (s) and node i
}
