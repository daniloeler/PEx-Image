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

package visualizer.datamining.dataanalysis;

import java.io.IOException;
import java.util.List;
import visualizer.graph.Graph;
import visualizer.graph.Vertex;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Euclidean;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class KruskalStress extends Stress {

    @Override
    public float calculate(Matrix matrix, Dissimilarity diss, Graph graph) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
        return this.calculate(dmat, graph);
    }

    @Override
    public float calculate(DistanceMatrix dmat, Graph graph) throws IOException {
        DenseMatrix projection = new DenseMatrix();

        for (int i = 0; i < graph.getVertex().size(); i++) {
            float[] vect = new float[2];
            vect[0] = graph.getVertex().get(i).getX();
            vect[1] = graph.getVertex().get(i).getY();
            projection.addRow(new DenseVector(vect));
        }

        DistanceMatrix dmatprj = new DistanceMatrix(projection, new Euclidean());

        float num = 0.0f;
        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int j = i + 1; j < dmat.getElementCount(); j++) {
                float distrnnorm = (dmat.getDistance(i, j) - dmat.getMinDistance()) /
                        (dmat.getMaxDistance() - dmat.getMinDistance());

                float distr2norm = (dmatprj.getDistance(i, j) - dmatprj.getMinDistance()) /
                        (dmatprj.getMaxDistance() - dmatprj.getMinDistance());

                num += (distrnnorm - distr2norm) * (distrnnorm - distr2norm);
            }
        }

        float den = 0.0f;
        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int j = i + 1; j < dmat.getElementCount(); j++) {
                float distr2norm = (dmatprj.getDistance(i, j) - dmatprj.getMinDistance()) /
                        (dmatprj.getMaxDistance() - dmatprj.getMinDistance());

                den += distr2norm * distr2norm;
            }
        }

        return num / den;
    }

    @Override
    public float[] calculateGroup(DistanceMatrix dmat, DistanceMatrix dmatprj) throws IOException {
        float[] elements = new float[dmat.getElementCount()];
        
        float num = 0.0f;
        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int j = i + 1; j < dmat.getElementCount(); j++) {
                float distrnnorm = (dmat.getDistance(i, j) - dmat.getMinDistance()) /
                        (dmat.getMaxDistance() - dmat.getMinDistance());

                float distr2norm = (dmatprj.getDistance(i, j) - dmatprj.getMinDistance()) /
                        (dmatprj.getMaxDistance() - dmatprj.getMinDistance());

                num += (distrnnorm - distr2norm) * (distrnnorm - distr2norm);
                elements[i] = (distrnnorm - distr2norm) * (distrnnorm - distr2norm);
            }
        }

        float den = 0.0f;
        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int j = i + 1; j < dmat.getElementCount(); j++) {
                float distr2norm = (dmatprj.getDistance(i, j) - dmatprj.getMinDistance()) /
                        (dmatprj.getMaxDistance() - dmatprj.getMinDistance());

                den += distr2norm * distr2norm;
            }
        }

        return elements;
    }

}
