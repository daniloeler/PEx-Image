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
import java.util.HashMap;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Gabriel de Faria Andery
 */
public class ScalarNeighbors {

    public ScalarNeighbors(ArrayList<Vertex> vertices, Scalar currentScalar, int numberNeighbors) {
        this.numberNeighbors = numberNeighbors;
        this.vertices = vertices;
        this.currentScalar = currentScalar;
    }

    public Pair[][] execute() throws IOException {
        Pair[][] neighbors = null;
        
        HashMap<Float, ArrayList<Integer>> sc = new HashMap<Float, ArrayList<Integer>>();
        ArrayList<ArrayList<Pair>> neigh_aux = new ArrayList<ArrayList<Pair>>();
        int validVertices = 0;
        
        for (Vertex vertex: vertices) {
            if (vertex.isValid()) {
                validVertices++;
                neigh_aux.add(new ArrayList<Pair>());
                Float key = vertex.getScalar(currentScalar);

                if (!sc.containsKey(key)) {
                    ArrayList<Integer> values = new ArrayList<Integer>();
                    values.add((int)vertex.getId());
                    sc.put(key, values);
                }
                else {
                    sc.get(key).add((int)vertex.getId());
                }
            }
        }
        
        for (ArrayList<Integer> values : sc.values()) {
            for (int i = 0; i < values.size(); i++) {
                Integer from = values.get(i);
                
                for (int j = i + 1; j < i+6; j++) {
                    Integer to = values.get((int)(Math.random() * values.size()) % values.size());
                    neigh_aux.get(from).add(new Pair(to, 1));
                    neigh_aux.get(to).add(new Pair(from, 1));
                    
                    to = values.get(j % values.size());
                    neigh_aux.get(from).add(new Pair(to, 1));
                    neigh_aux.get(to).add(new Pair(from, 1));
                }
            }
        }
        
        neighbors = new Pair[validVertices][];

        for (int i = 0; i < neigh_aux.size(); i++) {
            neighbors[i] = new Pair[neigh_aux.get(i).size()];

            for (int j = 0; j < neigh_aux.get(i).size(); j++) {
                neighbors[i][j] = neigh_aux.get(i).get(j);
            }
        }
        
        return neighbors;
    }
    
    public Pair[][] execute(DistanceMatrix dmat) throws IOException {
        Pair[][] neighbors = null;
        
        HashMap<Float, ArrayList<Integer>> sc = new HashMap<Float, ArrayList<Integer>>();
        ArrayList<ArrayList<Pair>> neigh_aux = new ArrayList<ArrayList<Pair>>();
        int validVertices = 0;
        
        for (Vertex vertex: vertices) {
            if (vertex.isValid()) {
                validVertices++;
                neigh_aux.add(new ArrayList<Pair>());
                Float key = vertex.getScalar(currentScalar);

                if (!sc.containsKey(key)) {
                    ArrayList<Integer> values = new ArrayList<Integer>();
                    values.add((int)vertex.getId());
                    sc.put(key, values);
                }
                else {
                    sc.get(key).add((int)vertex.getId());
                }
            }
        }
        
        for (ArrayList<Integer> values : sc.values()) {
            for (int i = 0; i < values.size(); i++) {
                Integer from = values.get(i);
                
                for (int j = 0; j < values.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    
                    Integer to = values.get(j % values.size());
                    
                    float dist = dmat.getDistance(from, to);
                    
                    int pos = neigh_aux.get(from).size();
                    while ((pos > 0) && (dist < neigh_aux.get(from).get(pos - 1).value))
                        pos--;
                    neigh_aux.get(from).add(pos, new Pair(to, dist));
                    
                    if (neigh_aux.get(from).size() > this.numberNeighbors)
                        neigh_aux.get(from).remove(neigh_aux.get(from).size()-1);
                }
            }
        }
        
        neighbors = new Pair[validVertices][];

        for (int i = 0; i < neigh_aux.size(); i++) {
            neighbors[i] = new Pair[neigh_aux.get(i).size()];

            for (int j = 0; j < neigh_aux.get(i).size(); j++) {
                neighbors[i][j] = neigh_aux.get(i).get(j);
            }
        }
        
        return neighbors;
    }
    
    private ArrayList<Vertex> vertices;
    private Scalar currentScalar;
    private int numberNeighbors;
}
