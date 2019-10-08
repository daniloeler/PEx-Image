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

package visualizer.topic;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.graph.Graph;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;
import visualizer.textprocessing.Ngram;
import visualizer.textprocessing.Preprocessor;
import visualizer.textprocessing.stemmer.StemmerType;
import visualizer.util.PExConstants;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class CovarianceTopic extends Topic {

    /**
     * Creates a new instance of CovarianceTopic
     * @param graph
     * @param vertex
     * @param corpus
     * @param tdata 
     */
    public CovarianceTopic(Graph graph, ArrayList<Vertex> vertex, Corpus corpus, TopicData tdata) {
        super(vertex);
        this.graph = graph;
        this.tdata = tdata;
        this.createTopic(vertex, corpus);
    }

    @Override
    public String getQuery() {
        if (this.boxes.size() > 0) {
            //String query = this.boxes.get(this.boxes.size() - 1).getMsg();
            String query = this.boxes.get(0).getMsg(); //topics whose the terms have the higher covariance
            query = query.substring(1, query.length() - 1);
            query = query.replaceAll(",", " and ");
            return query;
        }

        return null;
    }

    @Override
    protected void createTopic(ArrayList<Vertex> vertex, Corpus corpus) {
        try {
            int lowercut = 2, uppercut = -1, ngrams = 1;

            if (vertex.size() > 50 && vertex.size() < 100) {
                lowercut = 10;
            } else if (vertex.size() > 100 && vertex.size() < 300) {
                lowercut = 15;
            } else if (vertex.size() > 300) {
                lowercut = 20;
            }

            Preprocessor pp = new Preprocessor(corpus);
            Matrix matrix = pp.getMatrixSelected(lowercut, uppercut, ngrams,
                    StemmerType.NONE, true, vertex);
            ArrayList<Ngram> cpNgrams = pp.getNgrams();

            //Reducing the points and creating an index
            if (matrix.getRowCount() > 0 && matrix.getDimensions() > 0) {
                ArrayList<String> attributes = new ArrayList<String>();
                float[][] points = this.cutDimensions(matrix, cpNgrams, attributes);

                this.createTopic(points, attributes, vertex);
            }
        } catch (IOException ex) {
            Logger.getLogger(CovarianceTopic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createTopic(float[][] points, ArrayList<String> attributes,
            ArrayList<Vertex> vertex) throws IOException {
        //Extracting the mean of the columns
        float[] mean = new float[points[0].length];
        Arrays.fill(mean, 0.0f);

        for (int i = 0; i < points.length; i++) {
            //calculating
            for (int j = 0; j < points[i].length; j++) {
                mean[j] += points[i][j];
            }
        }

        for (int i = 0; i < mean.length; i++) {
            mean[i] /= points.length;
        }

        //extracting the mean
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j] -= mean[j];
            }
        }

        firstTopic = true;
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        StringBox box = this.createStringBoxes(points, attributes, indexes);

        if (box != null) {
            this.boxes.add(box);

            for (int i = 0; i < 10; i++) {
                StringBox box_aux = this.createStringBoxes(points, attributes, indexes);

                if (box_aux != null) {
                    this.boxes.add(box_aux);
                } else {
                    break;
                }
            }

            //returning the mean
            for (int i = 0; i < points.length; i++) {
                for (int j = 0; j < points[i].length; j++) {
                    points[i][j] += mean[j];
                }
            }

            this.colorVertex(points, vertex, indexes);
        }
    }

    private StringBox createStringBoxes(float[][] points,
            ArrayList<String> attributes, ArrayList<Integer> indexes) {

        //Get the two attributes with largest covariance
        float gcov1 = Float.MIN_VALUE;
        int icov = 0;
        int jcov = 0;
        for (int i = 0; i < points[0].length - 1; i++) {
            for (int j = points[0].length - 1; j > i; j--) {
                if (!indexes.contains(i) && !indexes.contains(j)) {
                    float aux = this.covariance(points, i, j);
                    if (gcov1 < aux) {
                        gcov1 = aux;
                        icov = i;
                        jcov = j;
                    }
                }
            }
        }

        indexes.add(icov);
        indexes.add(jcov);

        if (attributes.size() > 0) {
            String msg = "(" + attributes.get(icov).replaceAll("<>", "") + "," +
                    attributes.get(jcov).replaceAll("<>", "") + ",";

            for (int i = 0; i < points[0].length - 1; i++) {
                if (!indexes.contains(i)) {
                    float aux = (this.covariance(points, icov, i) +
                            this.covariance(points, jcov, i)) / 2;

                    if (aux / gcov1 > tdata.getPercentageTerms()) {
                        msg += attributes.get(i).replaceAll("<>", "") + ",";
                        indexes.add(i);
                    }
                }
            }

            msg = msg.substring(0, msg.length() - 1) + ")[";

            NumberFormat form = NumberFormat.getInstance();
            form.setMaximumFractionDigits(2);
            form.setMinimumFractionDigits(2);

            msg += form.format(gcov1).replaceAll(",", ".") + "]";

            if (firstTopic) {
                maxcov = gcov1;
                firstTopic = false;
                return new StringBox(msg);
            } else if (gcov1 > maxcov * tdata.getPercentageTopics()) {
                return new StringBox(msg);
            }
        }

        return null;
    }

    private float[][] cutDimensions(Matrix matrix, ArrayList<Ngram> cpNgrams,
            ArrayList<String> indexGrams) {
        //keep on the new points matrix no more than 200 dimensions
        float[][] newpoints = new float[matrix.getRowCount()][];

        for (int i = 0; i < newpoints.length; i++) {
            newpoints[i] = new float[(matrix.getDimensions() < 200) ? matrix.getDimensions() : 200];
            float[] point = matrix.getRow(i).toArray();
            for (int j = 0; j < newpoints[i].length; j++) {
                newpoints[i][j] = point[j];
            }
        }

        indexGrams.clear();
        for (int i = 0; i < newpoints[0].length; i++) {
            indexGrams.add(cpNgrams.get(i).ngram);
        }

        return newpoints;
    }

    //calculate the covariance between columns a and b
    private float covariance(float[][] points, int a, int b) {
        float covariance = 0.0f;
        for (int i = 0; i < points.length; i++) {
            covariance += points[i][a] * points[i][b];
        }

        covariance /= points.length;
        return covariance;
    }

    private void colorVertex(float[][] points, ArrayList<Vertex> vertex,
            ArrayList<Integer> indexes) throws IOException {

        Scalar s = this.graph.getScalarByName(PExConstants.TOPICS);

        for (int i = 0; i < vertex.size(); i++) {
            float value = 1.0f;

            for (int j = 0; j < indexes.size(); j++) {
                value *= (points[i][indexes.get(j)] > 0.0f) ? 1.0f : 0.0f;
            }

            vertex.get(i).setScalar(s, value);
        }
    }

    private boolean firstTopic;
    private float maxcov;
    private TopicData tdata;
    private Graph graph;
}

