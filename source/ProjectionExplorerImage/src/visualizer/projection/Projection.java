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

package visualizer.projection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.Graph;
import visualizer.graph.Connectivity;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Euclidean;
import visualizer.util.ApproxKNN;
import visualizer.util.Delaunay;
import visualizer.util.Pair;
import visualizer.util.PExConstants;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class Projection {

    public abstract float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view);

    public abstract float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view);

    public abstract ProjectionView getProjectionView(ProjectionData pdata);

    public void postProcessing(Graph graph) {
    }

    public void createConnectivities(Graph graph, ProjectionData pdata, ProjectionView view) {
        try {
            //Creating a Delaunay triangulation
            if (view != null) {
                view.setStatus("Creating the Delaunay triangulation...", 95);
            }

            float[][] projection = new float[graph.getVertex().size()][];
            for (int i = 0; i < projection.length; i++) {
                projection[i] = new float[2];
                projection[i][0] = graph.getVertex().get(i).getX();
                projection[i][1] = graph.getVertex().get(i).getY();
            }

            if (pdata.isCreateDelaunay()) {
                //perturbing equal vertices
                graph.perturb();

                try {
                    Delaunay d = new Delaunay();
                    Pair[][] neighborhood = d.execute(projection);
                    Connectivity con = new Connectivity(PExConstants.DELAUNAY);
                    con.create(graph.getVertex(), neighborhood);
                    graph.addConnectivity(con);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Projection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pdata.getKnnNumberNeighbors() > 0) {
                if (view != null) {
                    view.setStatus("Creating the KNN-R2 connectivity...", 95);
                }

                DenseMatrix dproj = new DenseMatrix();
                for (int i = 0; i < projection.length; i++) {
                    dproj.addRow(new DenseVector(projection[i]));
                }

                String conname = "KNN-R2-" + pdata.getKnnNumberNeighbors();
                Connectivity knnr2Con = new Connectivity(conname);
                ApproxKNN appknn = new ApproxKNN(pdata.getKnnNumberNeighbors());
                Pair[][] neighborhood = appknn.execute(dproj, new Euclidean());
                knnr2Con.create(graph.getVertex(), neighborhood);
                graph.addConnectivity(knnr2Con);

                //creating KNN-RN connectivity
                if (this.knnneighbors != null) {
                    if (view != null) {
                        view.setStatus("Creating KNN connectivity...", 90);
                    }

                    conname = "KNN-RN-" + pdata.getKnnNumberNeighbors();
                    Connectivity knnrnCon = new Connectivity(conname);
                    knnrnCon.create(graph.getVertex(), this.knnneighbors);
                    graph.addConnectivity(knnrnCon);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Projection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DistanceMatrix getDistanceMatrix() {
        return dmat;
    }

    protected Pair[][] knnneighbors;
    protected DistanceMatrix dmat;
}
