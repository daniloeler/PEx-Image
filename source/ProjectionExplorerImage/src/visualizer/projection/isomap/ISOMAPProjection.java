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

package visualizer.projection.isomap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.Projection;
import visualizer.projection.ProjectionData;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.DissimilarityType;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.lsp.MeshGenerator;
import visualizer.projection.mds.ClassicalScalingProjection;
import visualizer.util.Dijkstra;
import visualizer.util.KNN;
import visualizer.util.Pair;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ISOMAPProjection extends Projection {

    @Override
    public float[][] project(Matrix matrix, ProjectionData pdata, ProjectionView view) {
        try {
            Dissimilarity diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());
            DistanceMatrix dmat_aux = new DistanceMatrix(matrix, diss);
            dmat_aux.setIds(matrix.getIds());
            dmat_aux.setClassData(matrix.getClassData());

            return this.project(dmat_aux, pdata, view);
        } catch (IOException ex) {
            Logger.getLogger(ISOMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        try {
            if (view != null) {
                view.setStatus("Creating the new distance matrix...", 40);
            }

            //the new distance matrix
            DistanceMatrix new_dmat = new DistanceMatrix(dmat.getElementCount());

            //creating a graph with its nearest neighbors
            KNN knn = new KNN(pdata.getNumberNeighborsConnection());
            Pair[][] neighborhood = knn.execute(dmat);

            if (view != null) {
                view.setStatus("Creating the new distance matrix...", 50);
            }

            //assuring the connectivity (????)
            MeshGenerator meshgen = new MeshGenerator();
            neighborhood = meshgen.execute(neighborhood, dmat);

            if (view != null) {
                view.setStatus("Creating the new distance matrix...", 60);
            }

            Dijkstra d = new Dijkstra(neighborhood, dmat.getElementCount());

            for (int i = 0; i < dmat.getElementCount(); i++) {
                float[] dist = d.execute(i);

                for (int j = 0; j < dist.length; j++) {
                    new_dmat.setDistance(i, j, dist[j]);

                    assert (dist[j] == dist[j]);
                }
            }

            //projecting using the classical scaling
            ClassicalScalingProjection cmds = new ClassicalScalingProjection();
            return cmds.project(new_dmat, pdata, view);

        } catch (IOException ex) {
            Logger.getLogger(ISOMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
        return new ISOMAPProjectionView(pdata);
    }

    public static void main(String[] args) {
        try {
            String filename = "D:\\My Documents\\FERNANDO\\Tese\\datasets\\cbrilpirson.data";
            Matrix matrix = MatrixFactory.getInstance(filename);

            for (int i = 10; i <= 25; i += 1) {
                BufferedWriter out = null;

                try {
                    out = new BufferedWriter(new FileWriter(filename + "_ISOMAP_" + i + ".prj"));

                    ProjectionData pdata = new ProjectionData();
                    pdata.setDissimilarityType(DissimilarityType.COSINE_BASED);
                    pdata.setNumberNeighborsConnection(i);

                    ISOMAPProjection lle = new ISOMAPProjection();
                    float[][] projection = lle.project(matrix, pdata, null);

                    out.write("x;y\r\n");

                    for (int j = 0; j < projection.length; j++) {
                        out.write(matrix.getRow(j).getId());
                        out.write(";");
                        out.write(Float.toString(projection[j][0]));
                        out.write(";");
                        out.write(Float.toString(projection[j][1]));
                        out.write(";");
                        out.write(Float.toString(matrix.getRow(j).getKlass()));
                        out.write("\r\n");
                    }

                } catch (IOException e) {
                    throw new IOException(e.getMessage());
                } finally {
                    //fechar o arquivo
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ISOMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ISOMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
