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
 * Contributor(s): 
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * RuleSet.java
 *
 * Created on 11 de Dezembro de 2006, 15:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package visualizer.tools.apriori;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.graph.Vertex;
import visualizer.topic.TopicData;

/**
 *
 * @author robertopinho
 */
public class RuleSet implements TopicInterface {

    public Set<ItemSet> rules;
    public boolean[] coveredPoints;
    float[][] allPoints;
    private Set<String> termSet;
    private int supportGlobal = 0;
    private String strongPhrase = null;
    private String shortStrongPhrase = null;
    private ArrayList<Vertex> relatedVertices;
    
    
    /** Creates a new instance of RuleSet */
    public RuleSet(float[][] allPoints) {
        this.allPoints = allPoints;
        int corpusSize = allPoints.length;
        this.coveredPoints = new boolean[corpusSize];
        for (int j = 0; j < coveredPoints.length; j++) {
            this.coveredPoints[j] = false;
        }
        this.rules = new HashSet<ItemSet>();
        termSet = new HashSet<String>();
    }

    void add(ItemSet iS) {
        supportGlobal += iS.setCovered(allPoints, this.coveredPoints, false);
        rules.add(iS);
        termSet.addAll(iS.getTerms());
    }

    public String extractStrongestPhrase(String fullText, List<String> ngramList, TopicData tdata, boolean isShortPhrase) {
        String[] phrases = NLPTools.simplePhraseSpliter(fullText);
        String strongPhrase = null;
        Float strongestWeight = Float.MIN_VALUE;
        for (String phrase : phrases) {
            float contains = phraseWeight(phrase, ngramList, tdata, isShortPhrase);
            if (contains > strongestWeight) {
                strongPhrase = phrase;
                strongestWeight = contains;
            }
        }
        return strongPhrase;
    }

    public float phraseWeight(String phrase, List<String> ngramList, TopicData tdata, boolean isShortPhrase) {
        float contains = 0f;
        for (String ngram : ngramList) {
            contains += (phrase.toLowerCase().indexOf(ngram.toLowerCase()) != -1) ? 1 : 0;
        }
        if (isShortPhrase) {
            int wordCount = phrase.split("\\s").length;
            return contains - (wordCount - contains + 1) / 1000f;
        } else {
            return contains;
        }
    }

    public String computeStrongPhrase(float[][] allPoints, TopicData tdata, boolean isShortPhrase) {

        Corpus datasource = tdata.getCorpus();
        String phrase = null;
        String strongPhrase = null;
        Float strongestWeight = Float.MIN_VALUE;

        for (int i = 0; i < allPoints.length; i++) {
            if (coveredPoints[i]) { //Current line supports 
                //find Phrase
                String fullText = "error";
                try {
                    fullText = datasource.getFullContent(datasource.getIds().get(i));
                    phrase = extractStrongestPhrase(fullText, new ArrayList<String>(this.termSet), tdata, isShortPhrase);


                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }

                float phraseWeight = phraseWeight(phrase, new ArrayList<String>(this.termSet), tdata, isShortPhrase);
                if (phrase != null && phraseWeight > strongestWeight) {
                    strongPhrase = phrase;
                    strongestWeight = phraseWeight;
                }

            }
        }

        if (strongPhrase != null) {
            if (isShortPhrase) {
                this.shortStrongPhrase = strongPhrase;
            } else {
                this.strongPhrase = strongPhrase;
            }
            return strongPhrase;

        } else {
            this.strongPhrase = "";
            return this.toString();

        }
    }

    public String getTopic(TopicData ldata) {
        String topic = "";

        if (ldata.isShortPhrase()) {

            topic += this.shortStrongPhrase + " ";
        }
        if (ldata.isPhrase()) {

            topic += this.strongPhrase + " ";
        }

        if (ldata.isRuleTopic()) {
            topic += this.toString() + " ";
        }
        return topic;
    }

    @Override
    public String toString() {
        ItemSet ruleArray[] = rules.toArray(new ItemSet[0]);
        Set<String> termSet = new HashSet<String>();
        Set<String> headTermSet = new HashSet<String>();
        int maxSupportIndex = -1;
        int maxSupport = 0;
        for (int i = 0; i < ruleArray.length; i++) {
            if (ruleArray[i].getSupport() > maxSupport) {
                maxSupport = ruleArray[i].getSupport();
                maxSupportIndex = i;
            }
            termSet.addAll(ruleArray[i].getTerms());
        }
        String topic = "";
        //label +=ruleArray[maxSupportIndex].getMaxTfIdf();
        topic += "::" + ruleArray[maxSupportIndex].toString();
        termSet.removeAll(ruleArray[maxSupportIndex].getTerms());
        topic += termSet.toString().replaceAll("\\[|\\]", ":");
        return topic;
    }

    public Set<String> getTermSet() {
        return termSet;
    }

    public void setTermSet(Set<String> termSet) {
        this.termSet = termSet;
    }

    public int getSupportGlobal() {
        return supportGlobal;
    }

    public float getMaxConfidence() {
        float maxConfidence = Float.MIN_VALUE;
        ItemSet ruleArray[] = rules.toArray(new ItemSet[0]);
        for (int i = 0; i < ruleArray.length; i++) {
            if (ruleArray[i].getMaxConfidence() > maxConfidence) {
                maxConfidence = ruleArray[i].getMaxConfidence();
            }
        }
        return maxConfidence;
    }

    public float getMinConfidence() {
        float minConfidence = Float.MAX_VALUE;
        ItemSet ruleArray[] = rules.toArray(new ItemSet[0]);
        for (int i = 0; i < ruleArray.length; i++) {
            if (ruleArray[i].getMaxConfidence() < minConfidence) {
                minConfidence = ruleArray[i].getMaxConfidence();
            }
        }
        return minConfidence;
    }

    public float getAvgWeight() {
        float weightSum = 0f;
        ItemSet ruleArray[] = rules.toArray(new ItemSet[0]);
        for (int i = 0; i < ruleArray.length; i++) {
            weightSum += ruleArray[i].getSumTfIdf();
        }
        return weightSum / rules.size();
    }

    public String getTermString() {
        List<String> terms = new ArrayList<String>(termSet);
        Collections.sort(terms);
        StringBuffer termString = new StringBuffer("");
        for (String st : terms) {
            termString.append(st);
        }
        return termString.toString();

    }

    public boolean isPart(TopicInterface topic) {
        return (topic.getTerms().containsAll(this.getTerms()));
    }
    
    public List<String> getTerms() {
        return new ArrayList<String>(this.getTermSet());
    }
    
    public ArrayList<Vertex> getRelatedVertices() {
        return relatedVertices;
    }

    public void setRelatedVertices(ArrayList<Vertex> relatedVertices) {
        this.relatedVertices = relatedVertices;
    }


}
