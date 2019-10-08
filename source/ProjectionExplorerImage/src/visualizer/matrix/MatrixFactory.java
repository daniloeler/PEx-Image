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

package visualizer.matrix;

import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.projection.*;
import java.io.IOException;
import java.util.ArrayList;
import visualizer.corpus.Corpus;
import visualizer.corpus.CorpusFactory;
import visualizer.textprocessing.transformation.MatrixTransformation;
import visualizer.textprocessing.transformation.MatrixTransformationFactory;
import visualizer.textprocessing.Preprocessor;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 *
 */
public class MatrixFactory {

    public static Matrix getInstance(ProjectionView view, ProjectionData pdata) throws IOException {
        Matrix matrix = null;

        if (pdata.getSourceType() == SourceType.CORPUS) {
            if (view != null) {
                view.setStatus("Pre-processing the corpus...", 5);
            }

            Corpus cp = CorpusFactory.getInstance(pdata.getSourceFile(), pdata);
            int size = cp.getIds().size();

            if (view != null) {
                view.setStatus("Pre-processing the corpus... documents: " + size, 35);
            }

            Preprocessor pp = new Preprocessor(cp);
            matrix = pp.getMatrix(pdata.getLunhLowerCut(), pdata.getLunhUpperCut(),
                    pdata.getNumberGrams(), pdata.getStemmer(), pdata.isUseStopword());

            MatrixTransformation transf = MatrixTransformationFactory.getInstance(pdata.getMatrixTransformationType());
            matrix = transf.tranform(matrix, null);

        } else if (pdata.getSourceType() == SourceType.POINTS) {
            if (view != null) {
                view.setStatus("Reading the points file..." + pdata.getSourceFile(), 5);
            }

            BufferedReader in = new BufferedReader(new java.io.FileReader(pdata.getSourceFile()));

            //read the header
            char[] header = in.readLine().trim().toCharArray();

            //checking
            if (header.length != 2) {
                throw new IOException("Wrong format of header information.");
            }

            in.close();

            if (header[0] == 'D') {
                matrix = new DenseMatrix();
            } else if (header[0] == 'S') {
                matrix = new SparseMatrix();
            } else {
                throw new IOException("Unknow file format!");
            }

            matrix.load(pdata.getSourceFile());
        } else {
            throw new IOException("Unknow file format!");
        }

        return matrix;
    }

    public static Matrix getInstance(Class type) throws IOException {
        Matrix matrix = null;

        try {
            matrix = (Matrix) type.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(MatrixFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MatrixFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return matrix;
    }
    
    public static Matrix getInstance(String filename, ArrayList<String> ids) throws IOException {
        Matrix matrix = null;

        BufferedReader in = new BufferedReader(new java.io.FileReader(filename));

        //read the header
        char[] header = in.readLine().trim().toCharArray();

        //checking
        if (header.length != 2) {
            throw new IOException("Wrong format of header information.");
        }

        in.close();

        if (header[0] == 'D') {
            matrix = new DenseMatrix(ids);
        } else if (header[0] == 'S') {
            matrix = new SparseMatrix(ids);
        } else {
            throw new IOException("Unknow file format!");
        }

        matrix.load(filename);

        return matrix;
    }

    public static Matrix getInstance(String filename) throws IOException {
        Matrix matrix = null;

        BufferedReader in = new BufferedReader(new java.io.FileReader(filename));

        //read the header
        char[] header = in.readLine().trim().toCharArray();

        //checking
        if (header.length != 2) {
            throw new IOException("Wrong format of header information.");
        }

        in.close();

        if (header[0] == 'D') {
            matrix = new DenseMatrix();
        } else if (header[0] == 'S') {
            matrix = new SparseMatrix();
        } else {
            throw new IOException("Unknow file format!");
        }

        matrix.load(filename);

        return matrix;
    }

    public static void main(String[] args) {
        try {
            String filename = "D:\\My Documents\\FERNANDO\\Tese\\datasets\\cbrilpirson.data";
            SparseMatrix matrix = new SparseMatrix();
            matrix.load(filename);

            DenseMatrix dmatrix = new DenseMatrix();
            dmatrix.setAttributes(matrix.getAttributes());

            for (int i = 0; i < matrix.getRowCount(); i++) {
                dmatrix.addRow(new DenseVector(matrix.getRow(i).toArray(), matrix.getRow(i).getId(), matrix.getRow(i).getKlass()));
            }

            dmatrix.save(filename + "_dense.data");
        } catch (IOException ex) {
            Logger.getLogger(MatrixFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
