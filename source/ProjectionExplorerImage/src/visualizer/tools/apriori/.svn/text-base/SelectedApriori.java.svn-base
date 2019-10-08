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

/*
 * subSetApriori.java
 *
 * Created on 8 de Setembro de 2006, 14:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package visualizer.tools.apriori;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.topic.TopicData;
import visualizer.topic.TopicData.WeightType;

/**
 *
 * @author robertopinho
 */
public class SelectedApriori {

    private float minSup;
    private float minConf;
    private float beta = 1.0f;
    private float[][] selectedPoints;
    float[][] allPoints;
    public boolean[] coveredPoints;
    private String[] ngrams;
    public String topic;
    private List<ItemSet> itemSets = null;
    private List<ItemSet> selectedItemSets = null;
    public String ruleDump = "";
    //type of wieght to use on labels
    private WeightType weightType = WeightType.LOCAL;
    //For grouping Labels
    private List<RuleSet> ruleSetList;
    //TermSet for exporting
    private Set<String> termSet = new HashSet<String>();
    private Set<String> termSetW = new HashSet<String>();
    private TopicData tdata;
    /** Creates a new instance of subSetApriori */
    public SelectedApriori(float[][] selectedPoints, float[][] allPoints, ///
            Object[] ngrams) {
        this.selectedPoints = selectedPoints;
        this.allPoints = allPoints;

        this.ngrams = new String[ngrams.length];
        for (int i = 0; i < ngrams.length; i++) {
            this.ngrams[i] = (String) ngrams[i];
        }




    }

    /*Steps:
     *          graphPanel.getVertex()
     * 1 - find rule with local weight R1
     * 2 - find rule with biggest support R2
     * 3 - find selected points which support R1 or R2
     * 4 - build new vertex list
     */
    public void run() {
        List<ItemSet> currentItemSets;
        List<ItemSet> frequentOneItemSets, topOneItemSets;
        //LinkedList transactionList;
        //LinkedList itemsRef;
        HashTree tree;
        int maxTransactionLength;
        /* minSup as it has to be used for these calculations */
        float practicalMinSup, practicalMinNewWeight;
        long practicalMinSupInt;
        int nTop = 10; //new Float(Math.sqrt(selectedPoints[0].length)).intValue();

        ruleDump = "";

        //Export header for csv rule dump
        outItemGlobal("head;	item;	item;	item;	item;	item	;item	; sup(head)	;abs(head)	;sup(rule)	;abs(rule)	;sup(body)	;abs(body)	;conf;Run;Selection");

        ///Load Itens in List - input -> itens only. - needed ??

        //Create itemSets with one item in each and find suport


        float[] singleItemSupport = new float[selectedPoints[0].length];
        float[] singleItemTfIdf = new float[selectedPoints[0].length];
        float[] singleItemTfIdfSum = new float[selectedPoints[0].length];
        float[] newWeight = new float[selectedPoints[0].length];
        coveredPoints = new boolean[selectedPoints.length];
        for (int j = 0; j < selectedPoints.length; j++) {
            coveredPoints[j] = false;
        }

        //Init weights
        for (int i = 0; i < allPoints[0].length; i++) {
            singleItemTfIdfSum[i] = 0.0f;
            for (int j = 0; j < allPoints.length; j++) {
                singleItemTfIdfSum[i] += allPoints[j][i];
            }
        }
        for (int i = 0; i < selectedPoints[0].length; i++) {
            singleItemSupport[i] = 0.0f;
            singleItemTfIdf[i] = 0.0f;

            for (int j = 0; j < selectedPoints.length; j++) {
                singleItemSupport[i] += (selectedPoints[j][i] > 0 ? 1 : 0);
                singleItemTfIdf[i] += selectedPoints[j][i];
            }
            singleItemTfIdf[i] = singleItemTfIdf[i] / singleItemTfIdfSum[i];
            newWeight[i] = singleItemTfIdf[i] * singleItemSupport[i] / selectedPoints.length;
        }

        //Set thresholds
        if (tdata.getSupportType() == TopicData.SupportType.RELATIVE) {

            practicalMinSup = (tdata.getMinSup() * selectedPoints.length) / 100.f;


        } else {
            practicalMinSup = tdata.getMinSupAbs();

        }
        practicalMinSupInt = Math.round(practicalMinSup);
        if (practicalMinSupInt < 3) {
            practicalMinSupInt = 3;
        }
        List<ItemSet> itemSets = null;

        //Run with lowering support
        do {
            //Find Seeds
            float top[] = new float[nTop + 1];
            for (int i = 0; i < selectedPoints[0].length; i++) {
                for (int l = 0; l < nTop; l++) {
                    if (newWeight[i] > top[l]) {
                        for (int m = nTop - 1; m >= l; m--) {
                            top[m + 1] = top[m];
                        }
                        top[l] = newWeight[i];
                        break;
                    }
                }
            }

            practicalMinNewWeight = top[nTop - 1];
            float threshold = 1.0f * selectedPoints.length / allPoints.length;
            ruleDump = "Selection size:\t" + selectedPoints.length + "/" + (threshold * 100.0) + "%\n";
            ruleDump += "Top Locally Weighted Terms >=" + practicalMinNewWeight + "\n";
            frequentOneItemSets = new ArrayList<ItemSet>();
            topOneItemSets = new ArrayList<ItemSet>();

            //Find frequent and Top 1-item sets
            for (int i = 0; i < singleItemSupport.length; i++) {

                ItemSet iS = new ItemSet();
                iS.setOneItem(new Integer(i));
//                if(ngrams[i].contains("iraq")){
//                    System.out.println("oi");
//                }
                if (singleItemSupport[i] > practicalMinSupInt &&
                        singleItemTfIdf[i] >= threshold) { //1.0*singleItemSupport[i]/selectedPoints.length>threshold   ){ //singleItemTfIdf[i]>0.2 ) { //
                    frequentOneItemSets.add(iS);
                }
                if (newWeight[i] >= practicalMinNewWeight) {
                    topOneItemSets.add(iS);
                    ruleDump += ngrams[i] + ", "; ///
                }

            }

            //Start Apriori
            tree = new HashTree(selectedPoints[0].length, this.ngrams); ///
            tree.addCandidates(frequentOneItemSets);
            currentItemSets = frequentOneItemSets;

            //Count = 1 -> gerando 2
            currentItemSets = generateNItemsets(topOneItemSets, currentItemSets, 1); // gets k-itemsets
            tree.addCandidates(currentItemSets);
            tree.passTransactions(2, selectedPoints);
            tree.removeInfrequentItemsets(practicalMinSupInt);
            currentItemSets = tree.getFrequentNItemsets(2);

            //Count 2 -> end
            for (int count = 2; currentItemSets.size() > 0 && currentItemSets.size() < 5000; count++) {//for (int count = 2; count < 5; count++) {
                //for (int count = 2; count < 5; count++) {
                currentItemSets = generateNItemsets(frequentOneItemSets, currentItemSets, count); // gets k-itemsets
                tree.addCandidates(currentItemSets);
                tree.passTransactions(count + 1, selectedPoints);
                tree.removeInfrequentItemsets(practicalMinSupInt);
                currentItemSets = tree.getFrequentNItemsets(count + 1);

            }
            itemSets = tree.getAllFrequentItemsets();
            selectedItemSets = new ArrayList<ItemSet>();
            System.out.println("Current Support:" + practicalMinSupInt);
            System.out.println("Item Set size:" + itemSets.size());

            //Adjust support, if no item sets
            if (itemSets.size() == 0) {
                float delta = practicalMinSup * 0.50f;//*(practicalMinSup/20.0>1?1:practicalMinSup/20.0);
                System.out.println("delta:" + delta);

                if (delta < 1.0) {
                    delta = 1.0f;
                }
                practicalMinSup = practicalMinSup - delta;

                practicalMinSupInt = Math.round(practicalMinSup);
                System.out.println("Novo Suporte:" + practicalMinSupInt);
                nTop++;
            }
        } while (itemSets.size() == 0 && practicalMinSupInt >= 3);

        //Select Top rules according to weight
        if (itemSets.size() > 0) {




            //set up
            int nTopic = 1;
            float weight;
            float maxWeight[] = new float[nTopic + 1];
            for (float d : maxWeight) {
                d = Float.NEGATIVE_INFINITY;
            }
            ItemSet maxItemSet[] = new ItemSet[nTopic + 1];
            ItemSet iS;

            //int RULE_COUNT_LIMIT = 1000;


            for (int i = 0; i < itemSets.size(); i++) {
                iS = itemSets.get(i);
                iS.compute(selectedPoints, singleItemTfIdf, singleItemSupport);
            }
            Collections.sort(itemSets);

            topic = "";


            //Select multiple labels according to coverage
            int coverAnt = 0;
            ruleDump += "\n";
            for (int j = 0; j < itemSets.size(); j++) {
                int cover = 0;
                //Find covered points



                cover = itemSets.get(j).countCovered(selectedPoints, coveredPoints);



                float delta;
                //delta = coverAnt/10.0;
                //if(delta <2) delta = 2;
                delta = 0;
                //Build label string
                if (cover > delta) {

                    itemSets.get(j).computeConfidence(selectedPoints);
                    itemSets.get(j).computeGlobal(allPoints);
                    itemSets.get(j).setLdata(tdata);
                    //if(ldata.isRuleLabel())
                    itemSets.get(j).computeRuleTopic(ngrams, singleItemTfIdf, ///
                            selectedPoints.length);

                    //if(ldata.isPhrase())
                    itemSets.get(j).computePhrase(allPoints, tdata, ngrams, singleItemTfIdf, ///
                            selectedPoints.length);

                    //if(ldata.isShortPhrase())
                    itemSets.get(j).computeShortPhrase(allPoints, tdata, ngrams, singleItemTfIdf, ///
                            selectedPoints.length);


//                    //Usual Label
//                    label += itemSets.get(j).toString(ngrams, singleItemTfIdf, ///
//                            selectedPoints.length) +"\n";
//
//                    //Phrase Label
//                    label += itemSets.get(j).toPhrase(allPoints, ldata, ngrams, singleItemTfIdf, ///
//                            selectedPoints.length) +"\n";
//
                    cover = itemSets.get(j).setCovered(selectedPoints, coveredPoints, true);
                    /*ruleDump += "Selected:" + itemSets.get(j).toStringDump(ngrams, singleItemTfIdf, ///
                    selectedPoints.length, singleItemSupport) +"\n";
                     */
                    System.out.println("*****Selected:" + itemSets.get(j).toStringDump(ngrams, singleItemTfIdf, ///
                            selectedPoints.length, singleItemSupport));
                    //outItem(itemSets.get(j));
                    String outGlobal = itemSets.get(j).toStringGlobal(ngrams, ///
                            (double) allPoints.length);
                    outItemGlobal(outGlobal + ";" + tdata.getCurrentRun() + ";" + tdata.getCurrentSelection());
                    System.out.println(outGlobal);
                    coverAnt = cover;


                    //Selects itemSet
                    selectedItemSets.add(itemSets.get(j));

                    //Updates TermSet
                    for (String term : itemSets.get(j).getTerms()) {
                        this.termSet.add(term);

                    }

                    //Updates TermSet with Weight
                    for (Integer item : itemSets.get(j).getItems()) {
                        this.termSetW.add(ngrams[item.intValue()] + " ; " + singleItemTfIdf[item.intValue()]);
                    }



                    //Test for RuleSets
                    if (ruleSetList != null) {
                        int overlap;
                        boolean join = false;
                        boolean joinedOne = false;
                        for (RuleSet rS : ruleSetList) {
                            join = false;
                            overlap = itemSets.get(j).countOverlap(allPoints, rS.coveredPoints);
                            int support = Math.min(itemSets.get(j).getSupport(), rS.getSupportGlobal());

                            //sub sets
                            //if(rS.getTermSet().containsAll(itemSets.get(j).getTerms())||
                            //        itemSets.get(j).getTerms().containsAll(rS.getTermSet())){
                            int termOverlap = 0;

                            for (String term : itemSets.get(j).getTerms()) {
                                if (rS.getTermSet().contains(term)) {
                                    termOverlap++;
                                }
                            }

                            double relTermOverlap = ((double) termOverlap) /
                                    Math.min(itemSets.get(j).getTerms().size(),
                                    rS.getTermSet().size());

                            double relOverlap = ((double) overlap) / support;

                            if (relTermOverlap > 0 && relOverlap > 0.5) {
                                System.out.println("Joined:" + rS.toString() + ":D=" + relOverlap + ":T=" + relTermOverlap);
                                rS.add(itemSets.get(j));
                                joinedOne = true;
                            } else {
                                System.out.println("Not Joined:" + rS.toString() + ":D=" + relOverlap + ":T=" + relTermOverlap);
                            }
                        }
                        System.out.println();
                        if (!joinedOne) {
                            RuleSet rS = new RuleSet(allPoints);
                            rS.add(itemSets.get(j));
                            ruleSetList.add(rS);
                        }
                    }
                }




            }
            //Wrap up
            ruleDump += "\nRules:\t" + itemSets.size() + "\n";
            ruleDump += "Minimum Support:\t" + (practicalMinSupInt * 1.0 / selectedPoints.length) + "\n";
            ruleDump += "Top K Words:\t" + nTop + "\n";

//            for(int i=0; i<itemSets.size() ;i++){
//                //itemSets.get(i).computeConfidence(selectedPoints);
//                ruleDump += itemSets.get(i).toStringDump(ngrams, singleItemTfIdf, ///
//                        selectedPoints.length, singleItemSupport) +"\n";
//            }


        } else {
            topic = "";
        }

        this.itemSets = itemSets;
    //tree.printAllFrequentItemsets(selectedPoints.length,minSup);




    }

//    Preprocessor pp=new Preprocessor(pdata.getCorpora());
//    points=pp.getMatrix(corporaNgrams, pdata.getLunhLowerCut(), pdata.getLunhUpperCut(), pdata.getNumberGrams(), pdata.getMatrixTransformationType());
    public float getMinSup() {
        return minSup;
    }

    public void setMinSup(float minSup) {
        this.minSup = minSup;
    }

    public float getMinConf() {
        return minConf;
    }

    public void setMinConf(float minConf) {
        this.minConf = minConf;
    }

    private List<ItemSet> generateNItemsets(List<ItemSet> oneItemsets, List<ItemSet> nMinusOneItemsets, int k) {
        List<ItemSet> nItemSets = new ArrayList<ItemSet>();


        ItemSet newItemset;
        List<Integer> newItemsetElements;
        List<Integer> oldItemSet;


        for (ItemSet itemset : nMinusOneItemsets) {


            for (ItemSet oneItemset : oneItemsets) {
                newItemsetElements = new ArrayList<Integer>();

                oldItemSet = (List<Integer>) itemset.getItems();
                for (Integer oldItem : oldItemSet) {
                    newItemsetElements.add(oldItem);
                }
                if (oneItemset.getItem(0) > newItemsetElements.get(newItemsetElements.size() - 1)) {
                    /* one-itemset element added */
                    newItemsetElements.add(oneItemset.getItem(0));
                    nItemSets.add(new ItemSet(newItemsetElements));
                }
            }
        }


        return nItemSets;
    }

    private List<ItemSet> generateTopNItemsets(List<ItemSet> oneItemsets, List<ItemSet> nMinusOneItemsets, int k) {
        List<ItemSet> nItemSets = new ArrayList<ItemSet>();


        ItemSet newItemset;
        List<Integer> newItemsetElements;
        List<Integer> oldItemSet;


        for (ItemSet itemset : nMinusOneItemsets) {


            for (ItemSet oneItemset : oneItemsets) {


                newItemsetElements = new ArrayList<Integer>();

                oldItemSet = (List<Integer>) itemset.getItems();
                for (Integer oldItem : oldItemSet) {
                    newItemsetElements.add(oldItem);
                }
                if (oneItemset.getItem(0) > newItemsetElements.get(newItemsetElements.size() - 1)) {
                    /* one-itemset element added */
                    newItemsetElements.add(oneItemset.getItem(0));
                    nItemSets.add(new ItemSet(newItemsetElements));
                }
            }
        }


        return nItemSets;
    }

    public List<ItemSet> getItemSets() {
        return itemSets;
    }

    public float getBeta() {
        return beta;
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }

    private float getWeight(ItemSet iS) {
        if (weightType == WeightType.LOCAL) {
            return beta * iS.getSumTfIdf() + (1.0f - beta) * iS.getSupportRelative();
        } else {
            return (iS.getSumTfIdf() * iS.getSupportRelative()) / (iS.getSumTfIdf() + iS.getSupportRelative());
        }
    }

    private float getWeight(ItemSet iS, int n) {
        if (n == 0) {
            return iS.getSumTfIdf();
        } else if (n == 1) {
            return iS.getSupportRelative();
        } else {
            return getWeight(iS);
        }
    }

    public List<ItemSet> getSelectedItemSets() {
        return selectedItemSets;
    }

    public void setSelectedItemSets(List<ItemSet> selectedItemSets) {
        this.selectedItemSets = selectedItemSets;
    }

    private void outItem(ItemSet iS) {
        try {
            PrintWriter out = new PrintWriter(
                    new FileWriter("rulesDump.txt", true));
            int n = iS.getItems().size();
            int i;
            for (i = 0; i < n; i++) {

                out.print(iS.getItem(i) + "\t");

            }
            for (; i < 20; i++) {
                out.print(0 + "\t");
            }
            out.println();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void outItemGlobal(String itemTopic) {
        try {
            PrintWriter out = new PrintWriter(
                    new FileWriter(tdata.getCsvFileName(), true));
            out.println(itemTopic);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRuleSetList(List ruleSetList) {
        this.ruleSetList = ruleSetList;
    }

    public TopicData getLdata() {
        return tdata;
    }

    public void setLdata(TopicData ldata) {
        this.tdata = ldata;
    }

    public Set<String> getTermSet() {
        return termSet;
    }

    public void setTermSet(Set<String> termSet) {
        this.termSet = termSet;
    }

    public Set<String> getTermSetW() {
        return termSetW;
    }

    public void setTermSetW(Set<String> termSetW) {
        this.termSetW = termSetW;
    }

}
