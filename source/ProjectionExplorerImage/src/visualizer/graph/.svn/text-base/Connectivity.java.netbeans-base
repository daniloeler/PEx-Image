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

package visualizer.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import visualizer.util.Pair;

/**
 * This class represents the graph connectivity.
 * 
 * @author Fernando Vieira Paulovich
 */
public class Connectivity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /** Creates a new instance of Connectivity
     * 
     * @param name The connectivity's name
     */
    public Connectivity(String name) {
        this.setName(name);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        if (edges != null) {
            this.edges = edges;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Connectivity create(ArrayList<Vertex> vertex, Pair[][] neighborhood) {
        if (neighborhood != null) {
            HashMap<Long, Vertex> index = new HashMap<Long, Vertex>();
            for (Vertex v : vertex) {
                index.put(v.getId(), v);
            }

            for (int i = 0; i < neighborhood.length; i++) {
                for (int j = 0; j < neighborhood[i].length; j++) {
                    edges.add(new Edge(neighborhood[i][j].value, index.get((long) i),
                            index.get((long) neighborhood[i][j].index)));
                }
            }

            edges = Connectivity.compress(edges);
            this.setEdges(edges);
        }

        return this;
    }

    public static ArrayList<Edge> compress(ArrayList<Edge> edges) {
        if (edges.size() > 0) {
            Collections.sort(edges);
            ArrayList<Edge> edges_aux = edges;
            edges = new ArrayList<Edge>();

            int n = 0;
            edges.add(edges_aux.get(0));
            for (int i = 1; i < edges_aux.size(); i++) {
                if (!edges_aux.get(n).equals(edges_aux.get(i))) {
                    edges.add(edges_aux.get(i));
                    n = i;
                }
            }
        }

        return edges;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Connectivity) {
            return this.name.equals(((Connectivity) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3 + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.name;
    }

    private String name = "none"; //The connectivity name    
    private ArrayList<Edge> edges = new ArrayList<Edge>(); //The edges which composes the connectivity
}
