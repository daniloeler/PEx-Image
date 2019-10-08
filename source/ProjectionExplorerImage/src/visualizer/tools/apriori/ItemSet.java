
/**
 * @(#)ItemSet.java
 *
 * This class represents a single candidate itemset and its support.
 *
 * @author Adrian Bright
 */

// Modified by Roberto Pinho
package visualizer.tools.apriori;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.graph.Vertex;
import visualizer.topic.TopicData;

public class ItemSet implements Comparable<ItemSet>, TopicInterface {
    /* The list of items contained in the itemset */

    private List<Integer> items;
    /* The support count of the itemset */
    private int support;
    private Float[] confidence = null;
    String[] ngrams;
    private float sumTfIdf;
    private float headTfIdf;
    int tamCluster = 1;
    int cover = -1;
    int globalCover = -1;
    public boolean[] localCoveredPoints;
    String fileName = "";
    Integer maxTfIdfItem = -1;
    //Global measures
    int globalSupport = -1;
    int globalHeadSupport = -1;
    int globalBodySupport = -1;
    private List<Integer> globalCoveredAllPoints = null;
    private Integer head = null;
    //Different styles for labels
    private String phrase = null;
    private String ruleTopic = "";
    private String shortPhrase = null;
    private String headTopic;
    private TopicData ldata;
    
    private ArrayList<Vertex> relatedVertices;
    
    /**
     * ItemSet Initialises the ItemSet.
     */
    public ItemSet() {
        items = new ArrayList<Integer>();
        support = 0;
    }

    /**
     * ItemSet Initialises the ItemSet with a list of new items.
     * @param newItems List of new items.
     */
    public ItemSet(List<Integer> newItems) {
        items = newItems;
        support = 0;
    }

    
    /**
     * setOneItem Adds an item to the itemset.
     * @param attribute The new item to be added.
     */
    public void setOneItem(Integer attribute) {
        items.add(attribute);
    }

    /**
     * setItems Adds multiple items to the itemset.
     * @param attributes The new items to be added.
     */
    public void setItems(List<Integer> attributes) {
        items.addAll(attributes);
    }

    /**
     * incrementSupport Increments the support count of the itemset.
     */
    public void incrementSupport() {
        support++;
    }

    /**
     * getItem Returns the item at the specified point in the itemset.
     * @param index Identifies the position in the itemset.
     * @return The item at the specified position in the itemset.
     */
    public Integer getItem(int index) {
        return items.get(index);
    }

    /**
     * getItems Returns all the items in the itemset.
     * @return The items in the itemset.
     */
    public List<Integer> getItems() {
        return items;
    }

    /**
     * getSupport Returns the support count of the itemset.
     * @return The support count of the itemset.
     */
    public int getSupport() {
        return support;
    }

    public float getSupportRelative() {

        return (float) this.support / tamCluster;
    }

    /**
     * size Returns the size of the itemset.
     * @return The size of the itemset.
     */
    public int size() {
        return items.size();
    }

    /**
     * printItems Prints the itemset to screen.
     */
    public void printItems() {
        ListIterator it = items.listIterator(0);
        System.out.print("|itemset size" + items.size() + ": ");
        while (it.hasNext()) {
            System.out.print((String) it.next());
        }
        System.out.println(", support " + getSupport() + "|");
    }

    /**
     * itemsString Returns the itemset in its name form (not id form) in
     * the format of {a,b,c}.
     * @param itemsRef The identifiers of each item.
     * @return The itemset in name form.
     */
    @Override
    public String toString() {
        return ruleTopic; //getTopic(ldata);
    }

    public List<String> getTerms() {
        List<String> terms = new ArrayList<String>();
        for (Integer item : items) {
            terms.add(ngrams[item.intValue()]);

        }
        return terms;
    }

    public String extractPhrase(String fullText, List<String> ngramList) {
        String[] phrases = NLPTools.simplePhraseSpliter(fullText);
        for (String phrase : phrases) {
            boolean contains = true;
            for (String ngram : ngramList) {
                contains = contains && (phrase.toLowerCase().indexOf(ngram.toLowerCase()) != -1);
            }
            if (contains) {
                return phrase;
            }
        }
        return null;
    }

    public String extractShortestPhraseFuzzy(String fullText, List<String> ngramList) {
        String[] phrases = NLPTools.simplePhraseSpliter(fullText);
        String shortestPhrase = null;
        Integer shortestLength = Integer.MAX_VALUE;
        int hitCount = -1;
        for (String phrase : phrases) {
            int contains = 0;
            for (String ngram : ngramList) {
                contains = contains + ((phrase.toLowerCase().indexOf(ngram.toLowerCase()) != -1) ? 1 : 0);
            }
            if (contains > 0 && contains >= hitCount) {
                if (contains > hitCount || phrase.length() < shortestLength) {
                    shortestPhrase = phrase;
                    shortestLength = phrase.length();
                    hitCount = contains;
                }
            }
        }
        return shortestPhrase;
    }

    public String extractShortestPhrase(String fullText, List<String> ngramList) {
        String[] phrases = NLPTools.simplePhraseSpliter(fullText);
        String shortestPhrase = null;
        Integer shortestLength = Integer.MAX_VALUE;
        for (String phrase : phrases) {
            boolean contains = true;
            for (String ngram : ngramList) {
                contains = contains && (phrase.toLowerCase().indexOf(ngram.toLowerCase()) != -1);
            }
            if (contains && phrase.length() < shortestLength) {
                shortestPhrase = phrase;
                shortestLength = phrase.length();
            }
        }
        return shortestPhrase;
    }

    public List<String> ngramList(String[] allNgrams) {
        List<String> ngramList = new ArrayList<String>();
        for (Integer item : items) {
            ngramList.add(allNgrams[item.intValue()]);

        }
        return ngramList;
    }

    public String computeShortPhrase(float[][] allPoints, TopicData ldata, String[] ngrams, float[] singleItemTfIdf, int tamCluster) {

        Corpus datasource = ldata.getCorpus();
        List<String> ngramList = ngramList(ngrams);

        if (this.head == null) {
            return null;
        }
        int sR, sRb;
        String phrase = null;
        String shortestPhrase = null;
        Integer shortestLength = Integer.MAX_VALUE;
        Integer minHitCount = Integer.MIN_VALUE;

        for (int i = 0; i < allPoints.length; i++) {
            sR = 0;
            sRb = 1;
            for (int k = 0; k < items.size(); k++) {

                sR = sR + ((allPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
                sRb = sRb * ((allPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
            }

            if (sR > 0) { //Current line supports the Item Set //if(sRb!=0) {
                //find Phrase
                String fullText = "error";
                Integer hitCount = 0;
                try {
                    fullText = datasource.getFullContent(datasource.getIds().get(i));
                    phrase = extractShortestPhraseFuzzy(fullText, ngramList);
                    hitCount = phraseWeight(phrase, ngramList);

                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }


                if (phrase != null && hitCount >= minHitCount) {
                    if (hitCount > minHitCount || phrase.length() < shortestLength) {
                        shortestPhrase = phrase;
                        shortestLength = phrase.length();
                        hitCount = sR;
                    }
                }

            }
        }

        if (shortestPhrase != null) {
            this.shortPhrase = shortestPhrase;
            return shortestPhrase;

        } else {
            this.shortPhrase = "";
            if (this.ruleTopic == null) {
                return this.computeRuleTopic(ngrams, singleItemTfIdf, tamCluster);
            } else {
                return this.ruleTopic;
            }

        }
    }

    public int phraseWeight(String phrase, List<String> ngramList) {
        int contains = 0;
        for (String ngram : ngramList) {
            contains += (phrase.toLowerCase().indexOf(ngram.toLowerCase()) != -1) ? 1 : 0;
        }
        return contains;
    }

    public String computePhrase(float[][] allPoints, TopicData ldata, String[] ngrams, float[] singleItemTfIdf, int tamCluster) {

        Corpus datasource = ldata.getCorpus();
        List<String> ngramList = ngramList(ngrams);

        if (this.head == null) {
            return null;
        }
        int sR;
        String phrase = null;
        for (int i = 0; i < allPoints.length; i++) {
            sR = 1;
            for (int k = 0; k < items.size(); k++) {

                sR = sR * ((allPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
            }
            if (sR != 0) { //Current line supports the Item Set
                //find Phrase
                String fullText = "error";
                try {
                    fullText = datasource.getFullContent(datasource.getIds().get(i));
                    phrase = extractPhrase(fullText, ngramList);


                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }

                if (phrase != null) {
                    System.out.println(phrase + "\t" + fullText);
                    break;
                }
            }
        }

        if (phrase != null) {
            this.phrase = phrase;
            return phrase;

        } else {
            this.phrase = "";
            if (this.ruleTopic == null) {
                return this.computeRuleTopic(ngrams, singleItemTfIdf, tamCluster);
            } else {
                return this.ruleTopic;
            }

        }
    }

    public String computeRuleTopic(String[] ngrams, float[] singleItemTfIdf, int tamCluster) {
        this.ngrams = ngrams;
        String itemset = "";
        fileName = "";
        NumberFormat formatter3 = new DecimalFormat("0.000");
        NumberFormat formatter2 = new DecimalFormat("0.00");
        NumberFormat formatter0 = new DecimalFormat("#");

        if (this.confidence == null) {

            for (Integer item : items) {
                itemset += "([" +
                        ngrams[item.intValue()] + "];" + formatter3.format(singleItemTfIdf[item.intValue()]) + "),";
            }
            itemset = itemset.substring(0, itemset.length() - 1);
            itemset += "}";
            this.ruleTopic = itemset;
            return itemset;
        } else {
            int kHead = 0;
            itemset = "[" + ngrams[this.head.intValue()] + "]" + "<-";
            this.headTopic = ngrams[this.head.intValue()];
            fileName = "" + ngrams[this.head.intValue()].trim() + " <- ";
            for (int k = 0; k < items.size(); k++) {
                Integer item = items.get(k);
                if (item.intValue() == this.head.intValue()) {
                    kHead = k;
                    continue;
                }
                itemset += "[" +
                        ngrams[item.intValue()] + "],";
                fileName += "" +
                        ngrams[item.intValue()] + " ";
            }
            itemset += "(" + formatter0.format(this.support) + "/" +
                    formatter0.format(100 * (float) this.support / tamCluster) + "%;" + formatter0.format(this.confidence[kHead] * 100) + "%)";
            fileName += "[" + formatter0.format(this.support) + "_" +
                    formatter2.format(100 * (float) this.support / tamCluster) + ";" + formatter0.format(this.confidence[kHead] * 100) + "]";
            this.ruleTopic = itemset;
            return itemset;
        }


    }

    public String getRuleFileName() {
        return fileName;
    }

    public String toStringDump(String[] ngrams, float[] singleItemTfIdf, int tamCluster, float[] singleItemSupport) {
        String itemset = "";
        NumberFormat formatter3 = new DecimalFormat("0.000");
        NumberFormat formatter2 = new DecimalFormat("0.00");
        NumberFormat formatter0 = new DecimalFormat("#");
        itemset += formatter0.format(this.support) + "/" +
                formatter0.format(100 * (float) this.support / tamCluster) + "%:{";
        if (this.confidence == null) {

            for (Integer item : items) {
                itemset += "([" +
                        ngrams[item.intValue()] + "];" + formatter3.format(singleItemTfIdf[item.intValue()]) + "),";
            }
            itemset = itemset.substring(0, itemset.length() - 1);
            itemset += "}";
            return itemset;
        } else {

            for (int k = 0; k < items.size(); k++) {
                Integer item = items.get(k);
                itemset += "([" +
                        ngrams[item.intValue()] + "];w:" + formatter3.format(singleItemTfIdf[item.intValue()]) + ";c:" + formatter2.format(this.confidence[k]) + ";s:" + formatter2.format(singleItemSupport[item.intValue()]) + ";ic:" + this.cover + "),";
            }
            itemset = itemset.substring(0, itemset.length() - 1);
            itemset += "}";
            return itemset;
        }


    }

    public String getTopic(TopicData tdata) {
        String topic = "";

        if (tdata.isShortPhrase()) {
            topic += this.shortPhrase + " ";
        }

        if (tdata.isPhrase()) {
            topic += this.phrase + " ";
        }
        if (tdata.isRuleTopic()) {
            topic += this.ruleTopic + " ";
        }
        return topic;
    }

    public String toStringGlobal(String[] ngrams, double tamSet) {

        NumberFormat formatter3 = new DecimalFormat("0.000");
        NumberFormat formatter2 = new DecimalFormat("0.00");
        NumberFormat formatter0 = new DecimalFormat("#");
        String itemset = "";

        int kHead = 0;
        itemset = "[" + ngrams[this.head.intValue()] + "]" + ";";
        this.headTopic = ngrams[this.head.intValue()];

        for (int k = 0; k < items.size(); k++) {
            Integer item = items.get(k);
            if (item.intValue() == this.head.intValue()) {
                kHead = k;
                continue;
            }
            itemset += "[" +
                    ngrams[item.intValue()] + "];";

        }
        for (int k = items.size(); k < 7; k++) {
            itemset += " ; ";
        }



        itemset +=
                formatter3.format(this.globalHeadSupport / tamSet * 100) + ";" +
                formatter0.format(this.globalHeadSupport) + ";" +
                formatter3.format(this.globalSupport / tamSet * 100) + ";" +
                formatter0.format(this.globalSupport) + ";" +
                formatter3.format(this.globalBodySupport / tamSet * 100) + ";" +
                formatter0.format(this.globalBodySupport) + ";" +
                formatter3.format(this.globalSupport / (double) this.globalBodySupport * 100);

        //this.label = itemset;
        return itemset;



    }

    public void compute(float[][] selectedPoints, float[] singleItemTfIdf, float[] singleItemSupport) {
        this.tamCluster = selectedPoints.length;
        this.getHead(singleItemTfIdf, singleItemSupport);


    }

    public void computeConfidence(float[][] selectedPoints) {
        int support[] = new int[items.size()];
        int head = 1;
        for (int s : support) {
            s = 0;
        }
        int s;
        for (int i = 0; i < selectedPoints.length; i++) {
            for (int j = 0; j < support.length; j++) {
                s = 1;
                for (int k = 0; k < items.size(); k++) {
                    if (k != j) {
                        s = s * ((selectedPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
                    }
                }
                support[j] += s;
            }

        }
        Float confidence[] = new Float[support.length];
        Float maxConfidence = Float.NEGATIVE_INFINITY;

        for (int k = 0; k < items.size(); k++) {
            confidence[k] = ((float) this.support) / support[k];
            if (confidence[k] > maxConfidence) {
                maxConfidence = confidence[k];
                head = k;
            }
        }
        this.confidence = confidence;
        this.head = items.get(head);


    }

    public void computeGlobal(float[][] allPoints) {
        if (this.head == null) {
            return;
        }
        int headSupport = 0;
        int bodySupport = 0;
        int ruleSupport = 0;
        int head = this.head.intValue();
        int sB, sH;

        globalCoveredAllPoints = new ArrayList<Integer>();

        for (int i = 0; i < allPoints.length; i++) {
            sB = 1;
            for (int k = 0; k < items.size(); k++) {
                if (k != head) {
                    sB = sB * ((allPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
                }
            }
            bodySupport += sB;


            sH = ((allPoints[i][head] > 0) ? 1 : 0);
            headSupport += sH;
            ruleSupport += (sB * sH);
            if ((sB * sH) == 1) {
                globalCoveredAllPoints.add(i);
            }
        }
        this.globalBodySupport = bodySupport;
        this.globalHeadSupport = headSupport;
        this.globalSupport = ruleSupport;
    }

    public Integer getHead(float[] singleItemTfIdf, float[] singleItemSupport) {
        if (this.head == null) {
            int headInt = -1;
            float maxTfIdf = Float.NEGATIVE_INFINITY;
            float minSupport = Float.POSITIVE_INFINITY;
            float sumTfIdf = 0.0f;


            for (Integer item : items) {
                sumTfIdf += singleItemTfIdf[item.intValue()];
                if (singleItemTfIdf[item.intValue()] > maxTfIdf) {
                    maxTfIdf = singleItemTfIdf[item.intValue()];
                    this.maxTfIdfItem = item;

                }
                if (singleItemSupport[item.intValue()] < minSupport) {
                    minSupport = singleItemTfIdf[item.intValue()];
                    head = item.intValue();
                }

            }

            this.sumTfIdf = sumTfIdf;
            this.head = new Integer(headInt);
            this.headTfIdf = maxTfIdf;
        }
        return this.head;
    }

    public float getSumTfIdf() {
        return sumTfIdf;
    }

    public float getHeadTfIdf() {
        return headTfIdf;
    }

    public String getMaxTfIdf() {
        return ngrams[maxTfIdfItem.intValue()];
    }

    public Float[] getConfidence() {
        return confidence;
    }

    public Float getMaxConfidence() {
        Float maxConfidence = Float.MIN_VALUE;
        for (Float f : confidence) {
            if (f > maxConfidence) {
                maxConfidence = f;
            }
        }
        return maxConfidence;
    }

    public void setConfidence(Float[] confidence) {
        this.confidence = confidence;
    }

    public int compareTo(ItemSet o) {
        return new Float(o.getSumTfIdf()).compareTo(new Float(getSumTfIdf()));

    }

    @Override
    public boolean equals(Object obj) {
        ItemSet iS = (ItemSet) obj;
        if (this.items.containsAll(iS.getItems()) &&
                iS.getItems().containsAll(this.items)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.items.size(); i++) {
            hash += this.items.get(i).intValue();
        }
        return hash;
    }

    int setCovered(float[][] selectedPoints, boolean[] coveredPoints, boolean local) {
        int s;
        if (local) {
            this.cover = 0;
        } else {
            this.globalCover = 0;
        }
        if (local) {
            this.localCoveredPoints = new boolean[selectedPoints.length];
        }

        for (int i = 0; i < selectedPoints.length; i++) {
            if (local) {
                localCoveredPoints[i] = false;
            }

            s = 1;
            for (int k = 0; k < items.size(); k++) {
                s = s * ((selectedPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
            }

            if (s == 1) {
                if (!coveredPoints[i]) {
                    if (local) {
                        this.cover++;
                    } else {
                        this.globalCover++;
                    }
                    if (local) {
                        localCoveredPoints[i] = true;
                    }
                }
                coveredPoints[i] = true;
            }


        }
        if (local) {
            return this.cover;
        } else {
            return this.globalCover;
        }
    }

    int countCovered(float[][] selectedPoints, boolean[] coveredPoints) {
        int s;
        this.cover = 0;
        for (int i = 0; i < selectedPoints.length; i++) {

            s = 1;
            for (int k = 0; k < items.size(); k++) {
                s = s * ((selectedPoints[i][items.get(k).intValue()] > 0) ? 1 : 0);
            }
            if (s == 1) {
                if (!coveredPoints[i]) {
                    this.cover++;
                }

            }


        }
        return this.cover;
    }

    int countOverlap(float[][] Points, boolean[] coveredPoints) {
        int s;
        int overlap = 0;
        for (int i = 0; i < Points.length; i++) {
            if (coveredPoints[i]) {
                s = 1;
                for (int k = 0; k < items.size(); k++) {
                    s = s * ((Points[i][items.get(k).intValue()] > 0) ? 1 : 0);
                }
                if (s == 1) {
                    overlap++;

                }
            }

        }
        return overlap;
    }

    public List<Integer> getGlobalCoveredAllPoints() {
        return globalCoveredAllPoints;
    }

    public String getHeadTopic() {
        return headTopic;
    }

    public void setLdata(TopicData ldata) {
        this.ldata = ldata;
    }

    public boolean isPart(TopicInterface topic) {
        return (topic.getTerms().containsAll(this.getTerms()));
    }

    public ArrayList<Vertex> getRelatedVertices() {
        return relatedVertices;
    }

    public void setRelatedVertices(ArrayList<Vertex> relatedVertices) {
        this.relatedVertices = relatedVertices;
    }

}
