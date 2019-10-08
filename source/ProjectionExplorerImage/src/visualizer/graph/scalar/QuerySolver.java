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
 * Contributor(s): Fernando Vieira Paulovich <fpaulovich@gmail.com>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.graph.scalar;

import visualizer.graph.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import visualizer.corpus.Corpus;
import visualizer.textprocessing.Ngram;

/**
 *
 * @author rpinho, Fernando Vieira Paulovich
 */
public class QuerySolver {

    /** Creates a new instance of QuerySolver */
    public QuerySolver(Corpus corpus, ArrayList<Vertex> vertex) {
        this.corpus = corpus;
        this.vertex = vertex;
    }

    public boolean isoperand(String symb) {
        symb.replaceAll(" ", "");
        if (!symb.equals("and") && !symb.equals("or") && !symb.equals("(") && !symb.equals(")")) {
            return true;
        }
        return false;
    }

    public boolean prdc(String one, String two) {
        if (one.equals("(")) {
            return false;
        }
        if (!one.equals(")") && two.equals("(")) {
            return false;
        }
        if (!one.equals("(") && two.equals(")")) {
            return true;
        }
        if (one.equals("and") && two.equals("or")) {
            return true;
        }
        if (one.equals("or") && two.equals("and")) {
            return false;
        }
        if (one.equals(two)) {
            return true;
        }
        return true;
    }

    public static String replaceCharAt(String s, int pos, char c) {
        return s.substring(0, pos) + c + s.substring(pos + 1);
    }

    public String postfix(String query) {

        int position;
        boolean und;
        int outpos = 0;
        String topsymb = "or";
        String symb;
        StringBuffer postr = new StringBuffer();

        Stack<String> opstk = new Stack<String>();

        query = query.trim();

        query = query.replaceAll("\\(", " \\( ");
        query = query.replaceAll("\\)", " \\) ");


        boolean inSet = false;
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == '"') {
                inSet = !inSet;
            } else if (!inSet && query.charAt(i) == ' ') {
                query = replaceCharAt(query, i, splitSymbolChar);
            }
        }

        String[] symbs = query.split(splitSymbol);


        for (int i = 0; i < symbs.length; i++) {
            symbs[i] = symbs[i].replaceAll("\"", "");
            if (isoperand(symbs[i])) {
                postr.append(symbs[i] + splitSymbol);

            } else {
                if (!opstk.empty()) {
                    und = false;
                    topsymb = opstk.pop();
                } else {
                    und = true;
                }
                while (!und && prdc(topsymb, symbs[i])) {
                    postr.append(topsymb + splitSymbol);
                    if (!opstk.empty()) {
                        und = false;
                        topsymb = opstk.pop();
                    } else {
                        und = true;
                    }
                }
                if (!und) {
                    opstk.push(topsymb);
                }
                if (und || !symbs[i].equals(")")) {
                    opstk.push(symbs[i]);
                } else {
                    topsymb = opstk.pop();
                }
            }
        }
        while (!opstk.empty()) {
            postr.append(opstk.pop() + splitSymbol);
        }
        return postr.toString();
    }

    protected float[] oper(String opr, float[] cdata1, float[] cdata2) {
        float[] cdata = new float[vertex.size()];
        for (int i = 0; i < cdata1.length; i++) {
            cdata[i] = (cdata1[i] + cdata2[i]) / 2.0f;
            if (opr.equals("and")) {
                if (cdata1[i] == 0 || cdata2[i] == 0) {
                    cdata[i] = 0;
                }
            }
        }
        return cdata;
    }

    float[] value(String word) throws java.io.IOException {
        float[] cdata = new float[this.vertex.size()];
        Arrays.fill(cdata, 0.0f);

        //Getting the Cdata
        for (int i = 0; i < vertex.size(); i++) {
            if (this.vertex.get(i).isValid()) {
                cdata[i] = this.getWordFrequency(word, this.vertex.get(i).getUrl());
            }
        }

        return cdata;
    }

    float[] eval(String postFix) throws java.io.IOException {
        int c, position;
        float[] opnd1, opnd2;
        float[] value;

        Stack<float[]> opndstk = new Stack<float[]>();

        String[] symbs = postFix.split(splitSymbol);

        for (int i = 0; i < symbs.length; i++) {
            if (symbs[i].equals("")) {
                continue;
            }
            if (isoperand(symbs[i])) {
                opndstk.push(value(symbs[i]));
            } else {
                opnd2 = opndstk.pop();
                opnd1 = opndstk.pop();
                value = oper(symbs[i], opnd1, opnd2);
                opndstk.push(value);

            }
        }
        return opndstk.pop();

    }

    public void createCdata(String query, Scalar scalar) throws java.io.IOException {
        if (this.corpus == null) {
            throw new java.io.IOException("The corpus must be loaded!");
        }

        if (this.corpus != null) {
            this.entry = false;
        } else {
            this.entry = true;
        }

        //QuerySolver qS = new QuerySolver(corpora,vertex);
        float[] cdata = eval(postfix(query));

        //Getting the Cdata
        float max = 0.0f, min = 10E10f, numberOcurrences = 0.0f;
        for (int i = 0; i < vertex.size(); i++) {

            if (cdata[i] > max) {
                max = cdata[i];
            } else if (cdata[i] < min) {
                min = cdata[i];
            }

            if (cdata[i] > 0) {
                numberOcurrences++;
            }
        }

        //Normalizing
        for (int i = 0; i < cdata.length; i++) {
            if ((max - min) > 0.0 && (cdata[i] >= (numberOcurrences / vertex.size()))) {
                cdata[i] = (cdata[i] - min) / (max - min);
            } else {
                cdata[i] = 0.0f;
            }
        }

        for (int i = 0; i < vertex.size(); i++) {
            vertex.get(i).setScalar(scalar, cdata[i]);
        }

    }

    private float getWordFrequency(String word, String filename) throws IOException {
        return getWordFrequencyFromFile(word, filename);
    }

    protected float getWordFrequencyFromFile(String word, String filename) throws java.io.IOException {
        float frequency = 0.0f;

        ArrayList<Ngram> ngrams = this.corpus.getNgrams(filename);
        for (Ngram n : ngrams) {
            if (n.ngram.indexOf(word.toLowerCase()) != -1) {
                frequency += n.frequency;
            }
        }

        return frequency;
    }

    public static void main(String[] args) {
//        QuerySolver qS = new QuerySolver(null,null,null);
//        //qS.createCdata("(( A and B) or (A or B)) and (A or B and A)", "teste");
//        //qS.procP("(( A and B) or (A or B)) and (A or B and A)", null);
//        //System.out.println(qS.postfix("( ( A and B ) or ( C or D ) ) and ( E or F and G )"));
//        String query = " (( A and B) or (C or D E)) and (F or G and H) ";
//        query = query.replaceAll(" ", "&");
//
//        String[] symbs = query.split("&");
//        System.out.println(symbs[0]);

    }

    protected ArrayList<Vertex> vertex;
    private boolean entry = false;
    protected Corpus corpus;
    public static final String splitSymbol = "&";
    public static final char splitSymbolChar = '&';
}
