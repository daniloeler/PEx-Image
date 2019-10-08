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

package visualizer.projection.idmap;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.Matrix;
import visualizer.projection.ForceScheme;
import visualizer.projection.Projection;
import visualizer.projection.ProjectionData;
import visualizer.projection.Projector;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.util.KNN;
import visualizer.projection.ProjectorFactory;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.Dissimilarity;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class IDMAPProjection extends Projection {

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
            Logger.getLogger(IDMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public float[][] project(DistanceMatrix dmat, ProjectionData pdata, ProjectionView view) {
        try {
            this.dmat = dmat;

            KNN knn = new KNN(pdata.getKnnNumberNeighbors());
            this.knnneighbors = knn.execute(dmat);

            //Based on the projection type, create the projection using JAVA REFLECTION
            if (view != null) {
                view.setStatus("Projecting...", 40);
            }

            Projector proj = ProjectorFactory.getInstance(pdata.getProjectorType());
            float[][] projection = proj.project(dmat);

            if (projection != null) {
                if (view != null) {
                    view.setStatus("Improving the projection...", 45);
                }

                ForceScheme force = new ForceScheme(pdata.getFractionDelta(), projection.length);

                float error = Float.MAX_VALUE;
                for (int i = 0; i < pdata.getNumberIterations(); i++) {
                    error = force.iteration(dmat, projection);

                    String msg = "Iteration " + i + " - error: " + error;
                    if (view != null) {
                        view.setStatus(msg, (int) (45 + (i * 50.0f / pdata.getNumberIterations())));
                    }
                }
            }

            return projection;
        } catch (IOException ex) {
            Logger.getLogger(IDMAPProjection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public ProjectionView getProjectionView(ProjectionData pdata) {
        return new IDMAPProjectionView(pdata);
    }

}
