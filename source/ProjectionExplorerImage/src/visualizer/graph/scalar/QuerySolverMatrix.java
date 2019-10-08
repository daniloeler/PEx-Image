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
 * of the original code is Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * QuerySolverStem.java
 *
 * Created on 7 de Setembro de 2006, 22:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package visualizer.graph.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import visualizer.graph.*;
import visualizer.corpus.Corpus;

/**
 *
 * @author rpinho
 */
public class QuerySolverMatrix extends QuerySolver {

    HashMap<String, Float> freqTable = new HashMap<String, Float>();
    HashMap<String, String> stemTable = new HashMap<String, String>();
    HashMap<String, Integer> corporaNgrams;
    float[][] points;
    HashMap<String, Integer> pointLine = new HashMap<String, Integer>();
    HashMap<String, Integer> wordLine = new HashMap<String, Integer>();
    /** Creates a new instance of QuerySolverStem */
    public QuerySolverMatrix(Corpus corpora, ArrayList<Vertex> vertex, float[][] points, HashMap<String, Integer> corporaNgrams) {
        super(corpora, vertex);
        this.corporaNgrams = corporaNgrams;
        this.points = points;
        for (int i = 0; i < corpora.getIds().size(); i++) {
            pointLine.put(corpora.getIds().get(i), new Integer(i));
        }
        Object[] ngrams = corporaNgrams.keySet().toArray();
        for (int i = 0; i < ngrams.length; i++) {
            this.wordLine.put((String) ngrams[i], new Integer(i));
        }
    }

    @Override
    protected float getWordFrequencyFromFile(String word, String filename) throws java.io.IOException {
        float frequency = 0.0f;

        if (wordLine.get(word + "<>") == null || pointLine.get(filename) == null) {
            return frequency;
        }

        int i, j;

        i = pointLine.get(filename).intValue();
        j = wordLine.get(word + "<>").intValue();
        frequency = points[i][j];


        return frequency;
    }

    @Override
    protected float[] oper(String opr, float[] cdata1, float[] cdata2) {
        float[] cdata = new float[vertex.size()];
        for (int i = 0; i < cdata1.length; i++) {
            if (opr.equals("and")) {
                if (cdata1[i] == 0 || cdata2[i] == 0) {
                    cdata[i] = 0;
                } else {
                    cdata[i] = 1;
                }
            } else {
                cdata[i] = (cdata1[i] + cdata2[i]);
            }

        }
        return cdata;
    }

}
