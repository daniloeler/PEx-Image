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

package visualizer.projection.distance.kolmogorov;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.corpus.zip.ZipCorpus;
import visualizer.wizard.ProjectionView;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class NcdDistanceMatrixFactory {

    public static DistanceMatrix getInstance(ProjectionView view, CompressorType compType, Corpus corpus) {
        if (view != null) {
            view.setStatus("Calculating distances using NCD..." + corpus.getUrl(), 6);
        }

        int size = corpus.getIds().size();
        NormalCompressDistance ncd = new NormalCompressDistance(compType, corpus);
        DistanceMatrix dmat = new DistanceMatrix(size);

        dmat.setIds(corpus.getIds());
        dmat.setClassData(corpus.getClassData());

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                dmat.setDistance(i, j, ncd.calculateNewNCD(i, j));
            }

            if (view != null) {
                view.setStatus("Calculating distances using NCD...", 
                        (int) ((30.0f / size) * i + 6));
            }
        }

        return dmat;
    }

    public static void main(String[] args) {
        try {
            String filename = "cbr-ilp-ir.dmat";
            String url = "G:\\User\\users\\Documents\\FERNANDO\\Corpora\\cbr-ilp-ir.zip";
            Corpus corpus = new ZipCorpus(url, 1);

            DistanceMatrix dmat = NcdDistanceMatrixFactory.getInstance(null, CompressorType.BZIP2, corpus);
            dmat.save(filename);

        } catch (IOException ex) {
            Logger.getLogger(NcdDistanceMatrixFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
