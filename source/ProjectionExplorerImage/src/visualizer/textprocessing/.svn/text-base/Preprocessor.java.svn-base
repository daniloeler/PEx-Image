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
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>, 
 * Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.textprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;
import visualizer.matrix.SparseMatrix;
import visualizer.matrix.SparseVector;
import visualizer.textprocessing.stemmer.StemmerFactory;
import visualizer.textprocessing.stemmer.StemmerType;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Preprocessor {

    public Preprocessor(Corpus corpus) {
        this.corpus = corpus;
    }

    public Matrix getMatrix(int lowerCut, int upperCut, int numberGrams,
            StemmerType stemmer, boolean useStopword) throws IOException {

        this.lowerCut = lowerCut;
        this.upperCut = upperCut;
        this.numberGrams = numberGrams;
        this.stemmer = stemmer;
        this.useStopword = useStopword;

        //store the ngrams present on the corpus
        this.ngrams = this.getCorpusNgrams();

        return this.getMatrix(this.corpus.getIds());
    }

    public Matrix getMatrixSelected(int lowerCut, int upperCut, int numberGrams,
            StemmerType stemmer, boolean useStopword, ArrayList<Vertex> selected) throws IOException {

        this.lowerCut = lowerCut;
        this.upperCut = upperCut;
        this.numberGrams = numberGrams;
        this.stemmer = stemmer;
        this.useStopword = useStopword;

        ArrayList<String> urls = new ArrayList<String>();
        for (Vertex v : selected) {
            if (v.isValid()) {
                urls.add(v.getUrl());
            }
        }

        //store the ngrams present on the selected corpus
        this.ngrams = this.getCorpusNgrams(urls);

        return this.getMatrix(urls);
    }

    public ArrayList<Ngram> getNgrams() {
        return ngrams;
    }

    //If upperCut == -1 ths Luhn's upper cut-off will be ignored
    public ArrayList<Ngram> getNgramsAccordingTo(int lowerCut, int upperCut, int numberGrams,
            StemmerType stemmer, boolean useStopword) throws IOException {

        this.lowerCut = lowerCut;
        this.upperCut = upperCut;
        this.numberGrams = numberGrams;
        this.stemmer = stemmer;
        this.useStopword = useStopword;

        return this.getCorpusNgrams();
    }

    //If upperCut == -1 ths Luhn's upper cut-off will be ignored
    private Matrix getMatrix(ArrayList<String> urls) throws IOException {
        long start = System.currentTimeMillis();

        Matrix matrix = new SparseMatrix();

        //For each file
        for (int i = 0; i < urls.size(); i++) {
            float[] vector = new float[this.ngrams.size()];

            //get the ngrams of the file
            HashMap<String, Integer> docNgrams = this.getNgrams(urls.get(i));

            //For each ngram in the corpus which occurs more than lowerCut
            int j = 0;
            for (Ngram n : this.ngrams) {
                if (docNgrams.containsKey(n.ngram)) {
                    vector[j] = docNgrams.get(n.ngram);
                } else {
                    vector[j] = 0.0f;
                }

                j++;
            }

            SparseVector spv = new SparseVector(vector, urls.get(i), corpus.getClassData()[i]);
            matrix.addRow(spv);
        }

        //setting the attibutes
        ArrayList<String> attr = new ArrayList<String>();
        for (Ngram n : this.ngrams) {
            attr.add(n.ngram);
        }

        matrix.setAttributes(attr);

        long finish = System.currentTimeMillis();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Document collection processing time: " + (finish - start) / 1000.0f + "s");

        return matrix;
    }

    private ArrayList<Ngram> getCorpusNgrams(ArrayList<String> urls) throws IOException {
        HashMap<String, Integer> corpusNgrams_aux = new HashMap<String, Integer>();

        for (String url : urls) {
            HashMap<String, Integer> docNgrams = this.getNgrams(url);

            for (String key : docNgrams.keySet()) {
                if (corpusNgrams_aux.containsKey(key)) {
                    corpusNgrams_aux.put(key, corpusNgrams_aux.get(key) + docNgrams.get(key));
                } else {
                    corpusNgrams_aux.put(key, docNgrams.get(key));
                }
            }
        }

        ArrayList<Ngram> ngrams_aux = new ArrayList<Ngram>();

        for (String key : corpusNgrams_aux.keySet()) {
            int freq = corpusNgrams_aux.get(key);

            if (upperCut >= 0) {
                if (freq >= lowerCut && freq <= upperCut) {
                    ngrams_aux.add(new Ngram(key, freq));
                }
            } else {
                if (freq >= lowerCut) {
                    ngrams_aux.add(new Ngram(key, freq));
                }
            }
        }

        Collections.sort(ngrams_aux);

        return ngrams_aux;
    }

    private ArrayList<Ngram> getCorpusNgrams() throws IOException {
        HashMap<String, Integer> corpusNgrams_aux = new HashMap<String, Integer>();

        Stopword stp = null;
        Startword sta = null;

        if (useStopword) {
            stp = Stopword.getInstance();
        } else {
            sta = Startword.getInstance();
        }

        //For each ngram in the corpus
        for (Ngram n : this.corpus.getCorpusNgrams()) {
            String token = n.ngram;

            if ((useStopword && !stp.isStopWord(token)) ||
                    (!useStopword && sta.isStartWord(token))) {

                token = StemmerFactory.getInstance(stemmer).stem(token);

                if (token.trim().length() > 0) {
                    if (corpusNgrams_aux.containsKey(token)) {
                        corpusNgrams_aux.put(token, corpusNgrams_aux.get(token) + n.frequency);
                    } else {
                        corpusNgrams_aux.put(token, n.frequency);
                    }
                }
            }
        }

        ArrayList<Ngram> ngrams_aux = new ArrayList<Ngram>();

        for (String key : corpusNgrams_aux.keySet()) {
            int freq = corpusNgrams_aux.get(key);

            if (upperCut >= 0) {
                if (freq >= lowerCut && freq <= upperCut) {
                    ngrams_aux.add(new Ngram(key, freq));
                }
            } else {
                if (freq >= lowerCut) {
                    ngrams_aux.add(new Ngram(key, freq));
                }
            }
        }

        Collections.sort(ngrams_aux);

        return ngrams_aux;
    }

    private HashMap<String, Integer> getNgrams(String url) throws IOException {
        HashMap<String, Integer> ngrams_aux = new HashMap<String, Integer>();

        Stopword stp = null;
        Startword sta = null;

        if (useStopword) {
            stp = Stopword.getInstance();
        } else {
            sta = Startword.getInstance();
        }

        ArrayList<Ngram> fngrams = this.corpus.getNgrams(url);

        if (fngrams != null) {
            for (Ngram n : fngrams) {
                String token = n.ngram;

                if ((useStopword && !stp.isStopWord(token)) ||
                        (!useStopword && sta.isStartWord(token))) {
                    token = StemmerFactory.getInstance(stemmer).stem(token);

                    if (token.trim().length() > 0) {
                        if (ngrams_aux.containsKey(token)) {
                            ngrams_aux.put(token, ngrams_aux.get(token) + n.frequency);
                        } else {
                            ngrams_aux.put(token, n.frequency);
                        }
                    }
                }
            }
        }

        return ngrams_aux;
    }

    private Corpus corpus;
    private ArrayList<Ngram> ngrams;
    private StemmerType stemmer;
    private boolean useStopword;
    private int numberGrams;
    private int lowerCut;
    private int upperCut;
}
