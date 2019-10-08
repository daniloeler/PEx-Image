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

package visualizer.projection.distance;

import java.io.IOException;
import visualizer.corpus.Corpus;
import visualizer.corpus.CorpusFactory;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.projection.ProjectionData;
import visualizer.projection.SourceType;
import visualizer.projection.distance.kolmogorov.NcdDistanceMatrixFactory;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class DistanceMatrixFactory {

    public static DistanceMatrix getInstance(ProjectionView view, ProjectionData pdata) throws IOException {
        DistanceMatrix dmat = null;

        if (pdata.getDissimilarityType() == DissimilarityType.KOLMOGOROV) {
            //Distances computed using kolmogorov
            Corpus cp = CorpusFactory.getInstance(pdata.getSourceFile(), pdata);

            if (view != null) {
                view.setStatus("Calculating the distance matrix...", 0);
            }

            dmat = NcdDistanceMatrixFactory.getInstance(view, pdata.getCompressorType(), cp);

        } else if (pdata.getSourceType() == SourceType.DISTANCE_MATRIX) {
            //reading distances from file
            if (view != null) {
                view.setStatus("Reading the distances from file...", 5);
            }

            dmat = new DistanceMatrix(pdata.getSourceFile());
        }

        return dmat;
    }

    public static DistanceMatrix getInstance(float[][] points, Dissimilarity diss) throws IOException {
        Matrix matrix = new DenseMatrix();
        for (int i = 0; i < points.length; i++) {
            matrix.addRow(new DenseVector(points[i]));
        }

        return new DistanceMatrix(matrix, diss);
    }

}
