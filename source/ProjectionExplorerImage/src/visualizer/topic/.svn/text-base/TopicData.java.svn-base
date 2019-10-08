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

package visualizer.topic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import visualizer.corpus.Corpus;
import visualizer.graph.Graph;
import visualizer.matrix.Matrix;
import visualizer.textprocessing.transformation.MatrixTransformationType;
import visualizer.textprocessing.Ngram;
import visualizer.textprocessing.stemmer.StemmerType;

/**
 *
 * @author Fernando Vieira Paulovich, Roberto Pinho
 */
public class TopicData {

    public enum SupportType {

        RELATIVE, ABSOLUTE;
    }

    public enum TopicType {

        COVARIANCE, RULE
    }

    public enum WeightType {

        LOCAL, F_MEASURE
    }

    public enum ClusteringType {

        KMEANS, DBSCAN
    }

    public TopicData(Graph graph) {
        this.graph = graph;
    }

    public float getPercentageTopics() {
        return percentageTopics;
    }

    public void setPercentageTopics(float percentageTopics) {
        this.percentageTopics = percentageTopics;
    }

    public float getPercentageTerms() {
        return percentageTerms;
    }

    public void setPercentageTerms(float percentageTerms) {
        this.percentageTerms = percentageTerms;
    }

    public void setMinSup(float minSup) {
        this.minSup = minSup;
    }

    public int getMinSupAbs() {
        return minSupAbs;
    }

    public void setMinSupAbs(int minSupAbs) {
        this.minSupAbs = minSupAbs;
    }

    public SupportType getSupportType() {
        return supportType;
    }

    public void setSupportType(SupportType supportType) {
        this.supportType = supportType;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getLunhLowerCut() {
        return lowerCut;
    }

    public void setLunhLowerCut(int lowerCut) {
        if (this.lowerCut != lowerCut) {
            this.setMatrix(null);
        }
        this.lowerCut = lowerCut;
    }

    public int getLunhUpperCut() {
        return upperCut;
    }

    public void setLunhUpperCut(int upperCut) {
        if (this.upperCut != upperCut) {
            this.setMatrix(null);
        }
        this.upperCut = upperCut;
    }

    public int getNumberGrams() {
        return numberGrams;
    }

    public void setNumberGrams(int numberGrams) {
        if (this.numberGrams != numberGrams) {
            this.setMatrix(null);
        }
        this.numberGrams = numberGrams;
    }

    public MatrixTransformationType getMatrixTransformationType() {
        return matrixType;
    }

    public void setMatrixTransformationType(MatrixTransformationType matrixType) {
        if (this.matrixType != matrixType) {
            this.setMatrix(null);
        }
        this.matrixType = matrixType;
    }

    public ArrayList<Ngram> getCorporaNgrams() {
        return corporaNgrams;
    }

    public void setCorporaNgrams(ArrayList<Ngram> corporaNgrams) {
        this.corporaNgrams = corporaNgrams;
    }

    public WeightType getWeightType() {
        return weightType;
    }

    public void setWeightType(WeightType weightType) {
        this.weightType = weightType;
    }

    public Float getWeightBeta() {
        return weightBeta;
    }

    public void setWeightBeta(Float weightBeta) {
        this.weightBeta = weightBeta;
    }

    public TopicType getTopicType() {
        return topicType;
    }

    public void setTopicType(TopicType topicType) {
        this.topicType = topicType;
    }

    public StemmerType getStemmer() {
        return stemmer;
    }

    public void setStemmer(StemmerType stemmer) {
        if (this.stemmer != stemmer) {
            this.setMatrix(null);
        }

        this.stemmer = stemmer;
    }

    public void setTermSetAccum(Set<String> termSet) {
        this.termSetAccum = termSet;
    }

    public Set<String> getTermSetAccum() {
        return termSetAccum;
    }

    public boolean isUseStopword() {
        return useStopword;
    }

    public void setUseStopword(boolean useStopword) {
        if (this.useStopword != useStopword) {
            this.setMatrix(null);
        }
        this.useStopword = useStopword;
    }

    public boolean isGroupTopics() {
        return groupTopics;
    }

    public void setGroupTopics(boolean groupTopics) {
        this.groupTopics = groupTopics;
    }

    public float getMinSup() {
        return this.minSup;
    }

    public ClusteringType getClusteringType() {
        return clusteringType;
    }

    public void setClusteringType(ClusteringType clusteringType) {
        this.clusteringType = clusteringType;
    }

    public int getCurrentRun() {
        return currentRun;
    }

    public void setCurrentRun(int currentRun) {
        this.currentRun = currentRun;
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(int currentSelection) {
        this.currentSelection = currentSelection;
    }

    public String getCsvFileName() {
        if (csvFileName == null) {
            return "ruleDump.csv";
        } else {
            return csvFileName;
        }
    }

    public void setCsvFileName(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    public Set<String> getTermSetRun() {
        return termSetRun;
    }

    public void setTermSetRun(Set<String> termSetRun) {
        this.termSetRun = termSetRun;
    }

    public Set<String> getTermSetAccumW() {
        return termSetAccumW;
    }

    public void setTermSetAccumW(Set<String> termSetAccumW) {
        this.termSetAccumW = termSetAccumW;
    }

    public Set<String> getTermSetRunW() {
        return termSetRunW;
    }

    public void setTermSetRunW(Set<String> termSetRunW) {
        this.termSetRunW = termSetRunW;
    }

    void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    public Corpus getCorpus() {
        return this.corpus;
    }

    public boolean isRuleTopic() {
        return ruleTopic;
    }

    public boolean isPhrase() {
        return phrase;
    }

    public void setRuleTopic(boolean ruleTopic) {
        this.ruleTopic = ruleTopic;
    }

    public void setPhrase(boolean phrase) {
        this.phrase = phrase;
    }

    void setShortPhrase(boolean shortPhrase) {
        this.shortPhrase = shortPhrase;
    }

    public boolean isShortPhrase() {
        return shortPhrase;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TopicData clone = new TopicData(this.graph);

        clone.lowerCut = this.lowerCut;
        clone.upperCut = this.upperCut;
        clone.numberGrams = this.numberGrams;
        clone.matrixType = this.matrixType;
        clone.setMatrix(this.getMatrix());
        clone.corporaNgrams = this.corporaNgrams;
        clone.weightBeta = this.weightBeta;
        clone.stemmer = this.stemmer;
        clone.useStopword = this.useStopword;
        clone.groupTopics = this.groupTopics;
        clone.weightType = this.weightType;
        clone.topicType = this.topicType;
        clone.clusteringType = this.clusteringType;
        clone.percentageTerms = this.percentageTerms;
        clone.percentageTopics = this.percentageTopics;

        return clone;
    }

    //Settings for rule topic creation
    private float percentageTopics = 0.75f;
    private float percentageTerms = 0.5f;
    private int lowerCut = 2;
    private int upperCut = 150;
    private int numberGrams = 1;
    private MatrixTransformationType matrixType = MatrixTransformationType.TF_IDF;
    private Matrix matrix;
    private ArrayList<Ngram> corporaNgrams = null;
    private Float weightBeta = 1.0f;
    private StemmerType stemmer = StemmerType.NONE;
    private boolean useStopword = true;
    private boolean groupTopics = false;
    private float minSup = 40.0f;
    private int minSupAbs = 5;
    private SupportType supportType = SupportType.RELATIVE;
    //Multiple re-start statistics data
    private int currentRun = 0,  currentSelection = 0;
    //type of wieght to use on topics
    private WeightType weightType = WeightType.LOCAL;
    //The type of topic to create
    private TopicType topicType = TopicType.COVARIANCE;
    //Type of clustering used to generate vs
    private ClusteringType clusteringType = ClusteringType.KMEANS;
    //TermSet from rules for Session
    private Set<String> termSetAccum = new HashSet<String>();
    //TermSet from rules for last Iteraction run
    private Set<String> termSetRun = new HashSet<String>();
    //TermSet from rules for Session with Weight
    private Set<String> termSetAccumW = new HashSet<String>();
    //TermSet from rules for last Iteraction run with Weight
    private Set<String> termSetRunW = new HashSet<String>();
    private Corpus corpus = null;
    private boolean ruleTopic = true;
    private boolean phrase = false;
    private boolean shortPhrase = false;
    private String csvFileName = null;
    private Graph graph;
}
