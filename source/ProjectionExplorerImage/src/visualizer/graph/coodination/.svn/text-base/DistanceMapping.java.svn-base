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

/*
 * DistanceMapping.java
 *
 * Created on 21 de Agosto de 2007, 15:21
 */
package visualizer.graph.coodination;

import java.io.IOException;
import visualizer.graph.Vertex;
import visualizer.graph.coodination.Mapping.Map;
import visualizer.matrix.Matrix;
import visualizer.matrix.SparseVector;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.DissimilarityType;
import visualizer.view.Viewer;

/**
 *
 * @author Gabriel de Faria Andery
 */
public class DistanceMapping extends Mapping {

    /** Creates a new instance of DistanceMapping */
    public DistanceMapping(Matrix localMatrix, Matrix outerMatrix,
            int nrNeighbors, DissimilarityType metrictype, Viewer local, Viewer outer) throws java.io.IOException {
        this.localMatrix = localMatrix;
        this.outerMatrix = outerMatrix;
        this.nrNeighbors = nrNeighbors;
        this.metrictype = metrictype;
        this.outer = outer;

        this.createMapping(local, outer);
    }

    public String getName() {
        return "distance";
    }

    public void setLocalMatrix(Matrix localMatrix) {
        this.localMatrix = localMatrix;
    }

    public void setOuterMatrix(Matrix outerMatrix) {
        this.outerMatrix = outerMatrix;
    }

    public void setNrNeighbors(int nrNeighbors) {
        this.nrNeighbors = nrNeighbors;
    }

    protected void createMapping(Viewer local, Viewer outer) throws java.io.IOException {
        if (local != null && outer != null) {
            try {
                float[][] local_points=localMatrix.toMatrix();
                float[][] outer_points=outerMatrix.toMatrix();
                
                java.util.ArrayList<String> common_atts = null;
                
                if (!(localMatrix.getAttributes().isEmpty()) && !(outerMatrix.getAttributes().isEmpty()))
                    common_atts = this.findCommonAtts(localMatrix.getAttributes(), outerMatrix.getAttributes());

                if (common_atts != null) {
                    if (common_atts.size() < 1) {
                        throw new java.io.IOException("There are not common attributes " +
                                "between the projections!");
                    }
                }
                else if (localMatrix.getDimensions() == outerMatrix.getDimensions()) {
                    common_atts = new java.util.ArrayList<String>();
                    for (int i = 1; i <= localMatrix.getDimensions(); i++){
                        common_atts.add("attr" + i);
                    }
                    localMatrix.setAttributes(common_atts);
                    outerMatrix.setAttributes(common_atts);
                }
                else {
                    throw new java.io.IOException("There are not common attributes " +
                                "between the projections!");
                }

                System.out.println("Number of common attributes: " + common_atts.size());

                float[][] new_local_points = this.transformMatrix(localMatrix.getAttributes(), common_atts, local_points);
                float[][] new_outer_points = this.transformMatrix(outerMatrix.getAttributes(), common_atts, outer_points);
                
                //Deciding the distance type
                Dissimilarity diss = DissimilarityFactory.getInstance(this.metrictype);

                //for each outer point
                for (int i = 0; i < new_outer_points.length; i++) {  
                    //System.out.println("Vertex: " + i);
                    //creating the first pair list
                    Pair[] pairList = new Pair[this.nrNeighbors];

                    for (int j = 0; j < this.nrNeighbors; j++) {
                        //float distance = diss.calculate(new SparseVector(new_outer_points[i]), new SparseVector(new_local_points[j]));
                        //float distance = diss.calculate(new_outer_points[i], new_local_points[j]);
                        float distance = diss.calculate(outerMatrix.getRow(i), localMatrix.getRow(j));                                
                        pairList[j] = new Pair(j, distance);
                    }

                    //find the nearest points between the local points
                    for (int j = this.nrNeighbors; j < new_local_points.length; j++) {
                        //float distance = diss.calculate(new SparseVector(new_outer_points[i]), new SparseVector(new_local_points[j]));
                        //float distance = diss.calculate(new_outer_points[i], new_local_points[j]);
                        float distance = diss.calculate(outerMatrix.getRow(i), localMatrix.getRow(j));
                        this.addDistance(pairList, new Pair(j, distance));
                    }

                    //creating the mappings                    
                    Vertex v_outer = outer.getGraph().getVertex().get(i);

                    Map m = new Map();
                    m.outer = v_outer;
                    
                    for (int j = 0; j < pairList.length; j++) {
                        Vertex v_local = local.getGraph().getVertex().get(pairList[j].id);
                        m.local.add(v_local);
                    }

                    this.mapping.add(m);                    
                }
            } catch (IOException ex) {
                throw new java.io.IOException(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        System.out.println("End of Distance Coordination Mapping");
        }
    }

    public void addDistance(Pair[] pairList, Pair newPair) {
        if (pairList[0].distance > newPair.distance) {
            int i = 0;
            while (i < pairList.length && pairList[i].distance > newPair.distance) {
                if (i < pairList.length - 1) {
                    pairList[i] = pairList[i + 1];
                }
                i++;
            }
            pairList[i - 1] = newPair;
        }
    }

    public float[][] transformMatrix(java.util.ArrayList<String> old_atts,
            java.util.ArrayList<String> new_atts, float[][] old_matrix) {
        //creating the new matrix
        float[][] new_matrix = new float[old_matrix.length][];
        for (int i = 0; i < new_matrix.length; i++) {
            new_matrix[i] = new float[new_atts.size()];
        }

        //for each new ngram
        for (int i = 0; i < new_atts.size(); i++) {
            //find the old index
            int old_index = 0;
            for (int j = 0; j < old_atts.size(); j++) {
                if (old_atts.get(j).equals(new_atts.get(i))) {
                    old_index = j;
                    break;
                }
            }

            //copy the old values to the new matrix
            for (int j = 0; j < new_matrix.length; j++) {
                new_matrix[j][i] = old_matrix[j][old_index];
            }
        }

        return new_matrix;
    }

    public java.util.ArrayList<String> findCommonAtts(java.util.ArrayList<String> atts_1,
            java.util.ArrayList<String> atts_2) {
        java.util.ArrayList<String> final_atts = new java.util.ArrayList<String>();

        for (int i = 0; i < atts_1.size(); i++) {
            for (int j = 0; j < atts_2.size(); j++) {
                if (atts_1.get(i).equals(atts_2.get(j))) {
                    final_atts.add(atts_1.get(i));
                    break;
                }
            }
        }

        return final_atts;
    }
    
    public static class Pair {

        public Pair(int id, float distance) {
            this.id = id;
            this.distance = distance;
        }

        public int id;
        public float distance;
    }

    private Matrix localMatrix;
    private Matrix outerMatrix;
    private int nrNeighbors = 5;
    private DissimilarityType metrictype;
}
