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
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class MSTEdges {

    public MSTEdges(ArrayList<Vertex> vertex, ArrayList<String> names) {
        this.vertex = vertex;
        this.names = names;
    }

    public Pair[][] execute(Matrix matrix, Dissimilarity diss) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
        return this.execute(dmat);
    }

    public Pair[][] execute(DistanceMatrix dmat) {
        Prim prim = new Prim(dmat);
        ArrayList<Prim.Edge> edges = prim.prim();

        this.validate(edges);

        //creating the neighborhood
        ArrayList<ArrayList<Pair>> neigh_aux = new ArrayList<ArrayList<Pair>>();

        for (int i = 0; i < dmat.getElementCount(); i++) {
            neigh_aux.add(new ArrayList<Pair>());
        }

        for (Prim.Edge e : edges) {
            neigh_aux.get(e.node1).add(new Pair(e.node2, e.len));
//            neigh_aux.get(e.node2).add(new Pair(e.node1, e.len));
        }

        Pair[][] neighborhood = new Pair[dmat.getElementCount()][];

        for (int i = 0; i < neigh_aux.size(); i++) {
            neighborhood[i] = new Pair[neigh_aux.get(i).size()];

            for (int j = 0; j < neigh_aux.get(i).size(); j++) {
                neighborhood[i][j] = neigh_aux.get(i).get(j);
            }
        }

        return neighborhood;
    }

    /** validates the indexex from the distance matrix to be the same
    of the vertexes indexes. */
    private void validate(ArrayList<Prim.Edge> edges) {
        int[] indexes = new int[this.names.size()];

        //mapping the indexes from the vertex according to the names
        for (int i = 0; i < this.vertex.size(); i++) {
            int index = this.names.indexOf(this.vertex.get(i).getUrl());
            indexes[index] = i;
        }

        for (Prim.Edge e : edges) {
            e.node1 = indexes[e.node1];
            e.node2 = indexes[e.node2];
        }
    }

    private ArrayList<Vertex> vertex;
    private ArrayList<String> names;
}
