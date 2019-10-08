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
 * of the original code is Ana Maria Cuadros Valdivia,
 * Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.projection.nj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.graph.Connectivity;
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;
import visualizer.projection.Projection;
import visualizer.projection.ProjectionData;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.util.KNN;
import visualizer.util.Pair;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.Dissimilarity;
import visualizer.util.PExConstants;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class NJProjection extends Projection {

    @Override
    public float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view) {
        try {
            Dissimilarity diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());
            DistanceMatrix dmat_aux = new DistanceMatrix(matrix, diss);
            dmat_aux.setIds(matrix.getIds());
            dmat_aux.setClassData(matrix.getClassData());

            float[][] projection = this.project(dmat_aux, pdata, view);
            return projection;
        } catch (IOException ex) {
            Logger.getLogger(NJProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        try {
            this.dmat = dmat;

            KNN knn = new KNN(pdata.getKnnNumberNeighbors());
            this.knnneighbors = knn.execute(dmat);

            if (view != null) {
                view.setStatus("Calculating N-J...", 40);
            }

            int numberPoints = dmat.getElementCount();
            int p = numberPoints + (numberPoints - 2);

            this.neighbor = new NeighborJoining(numberPoints);
            this.neighbor.doNeighbor(this.dmat);

            float[][] projection = new float[p][];

            for (int i = 0; i < p; i++) {
                projection[i] = new float[2];
                projection[i][0] = neighbor.getX(i);
                projection[i][1] = neighbor.getY(i);
            }

            return projection;
        } catch (IOException ex) {
            Logger.getLogger(NJProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public void createConnectivities(Graph graph, ProjectionData pdata, ProjectionView view) {
        if (this.neighbor != null) {
            if (view != null) {
                view.setStatus("Creating N-J connectivity...", 95);
            }

            Connectivity njcon = new Connectivity(PExConstants.NJ);
            ArrayList<Edge> edges = new ArrayList<Edge>();

            //Armazena os vértices, mas fornece um índice por ser uma HashMap
            HashMap<java.lang.Long, Vertex> vert_aux = new HashMap<java.lang.Long, Vertex>();
            ArrayList<Vertex> vertex = graph.getVertex();
            for (Vertex v : vertex) {
                vert_aux.put(v.getId(), v);
            }

            for (int j = 0; j < this.neighbor.get_size(); j++) {
                float dis = this.neighbor.get_dis_are(j);
                int sour = this.neighbor.get_source_are(j);
                int targ = this.neighbor.get_target_are(j);
                edges.add(new Edge(dis, vert_aux.get((long) sour), vert_aux.get((long) targ)));
            }

            njcon.setEdges(edges);
            graph.addConnectivity(njcon);
        }

        if (this.dmat != null) {
            try {
                if (view != null) {
                    view.setStatus("Creating KNN connectivity...", 90);
                }

                String conname = "KNN-RN-" + pdata.getKnnNumberNeighbors();
                Connectivity knnCon = new Connectivity(conname);
                KNN knn = new KNN(pdata.getKnnNumberNeighbors());
                Pair[][] neighborhood = knn.execute(this.dmat);
                knnCon.create(graph.getVertex(), neighborhood);
                graph.addConnectivity(knnCon);
            } catch (IOException ex) {
                Logger.getLogger(NJProjection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void postProcessing(Graph graph) {
        if (this.dmat != null) {
            for (int i = 0; i < graph.getVertex().size(); i++) {
                if (i >= (graph.getVertex().size() / 2) + 1) {
                    graph.getVertex().get(i).setValid(false);
                }
            }
        }
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
       return new NJProjectionView(pdata);
    }

    private NeighborJoining neighbor;
}
