
/**
 * @(#)HashTree.java
 *
 * This class represents the data structure in which the candidate itemsets
 * are stored.
 *
 * @author Adrian Bright
 */
package visualizer.tools.apriori;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class HashTree {
    /* Identifier used to identify the level in the tree, starting at the root node (0) */

    private static final int TOP_LEVEL = 0;
    /* The number of stems at each level.. */
    private static int stemNum = 3;
    /* The number of itemsets each bucket will hold. */
    private static int bucketSize = 3;
    /* Array which stores the definitions of each stem, ie. which keys hash into each stem. */
    StemDescription[] stemDescriptors;
    /* The root node of the hashtree. */
    HashMap tree;
    /* Stores the identifiers of each item, eg. '1' may represent "beer". */
    List<Integer> itemsRef;
    /* Stores the identifiers of each item, eg. '1' may represent "beer". */
    String[] ngrams;
    /**
     * HashTree Initialises the HashTree
     * @param itemsReference The list of identifiers for each item.
     */
    public HashTree(int n, String[] ngrams) {
        List<Integer> itemsReference = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            itemsReference.add(new Integer(i));

        }
        itemsRef = itemsReference;
        tree = new HashMap();
        this.ngrams = ngrams;
        defineStems(itemsRef);
    }

    public HashTree(List<Integer> itemsReference) {
        itemsRef = itemsReference;
        tree = new HashMap();
        defineStems(itemsRef);
    }

    /**
     * addCandidates Adds the candidate itemsets to the Hashtree.
     * @param currentItemsets The list of itemsets to add to the hashtree.
     */
    public void addCandidates(List<ItemSet> currentItemsets) {
        ListIterator itItemsets = currentItemsets.listIterator(0);

        List<Integer> currentItemsetItems;
        ListIterator itItemsetItems;

        MyTreeNode newBucket;
        Integer currentItem;

        int count;
        Character currentStemID;

        for (ItemSet currentItemset : currentItemsets) {
            /* search in the tree to see if there already exists a matching entry; 
             * if not then insert a new bucket */

            currentItemsetItems = currentItemset.getItems();
            currentItem = currentItemsetItems.get(0);
            for (count = 0; count < stemDescriptors.length && (!((StemDescription) stemDescriptors[count]).keyExists(currentItem)); count++) {
            }
            currentStemID = ((StemDescription) stemDescriptors[count]).getID();
            if (tree.containsKey(currentStemID)) {
                ((MyTreeNode) tree.get(currentStemID)).addItemset(currentItemset);
            } else {
                newBucket = new MyTreeNode(bucketSize, stemNum, itemsRef, stemDescriptors, TOP_LEVEL + 1);
                newBucket.addItemset(currentItemset);
                tree.put(currentStemID, newBucket);
            }
        }
    }

    /**
     * defineStems Defines the keys which will hash onto each stem.
     * @param itemsRef The list of identifiers for each item.
     */
    public void defineStems(List<Integer> itemsRef) {
        int count;
        char stemIDcount = 'A';
        ListIterator itItemsRef = itemsRef.listIterator(0);
        Character stemID;
        /* define stem descriptors */
        stemDescriptors = new StemDescription[stemNum]; // each stem will have a StemDescription
        for (count = 0; count < stemNum; count++) {
            stemID = new Character(stemIDcount);
            stemIDcount++;
            stemDescriptors[count] = new StemDescription(stemID);
        }

        for (int i = 0; i < itemsRef.size();) {
            for (count = 0; i < itemsRef.size() && count < stemNum; count++) {
                stemDescriptors[count].addKey(itemsRef.get(i));
                i++;
            }

        }

    /* print stem descriptors */
    /*for (count=0; count<stemDescriptors.length; count++)
    {
    System.out.println("Stem " + count + " id " + ((StemDescription)stemDescriptors[count]).getID() + ": " + ((StemDescription)stemDescriptors[count]).getKeys());
    }*/
    }

    /**
     * passTransactions Passes transactions over the candidate itemsets,
     * incrementing their support count as required.
     * @param level The level in the tree at which the transactions need to be inserted.
     * @param fileName The file in which the transactions are stored.
     */
    public void passTransactions(int level, float[][] selectedPoints) {
        //Transaction newTrans = new Transaction();
        ArrayList candidateItemsets;
        ListIterator itTransItems;

        MyTreeNode currentNode;
        Integer currentTransItem;

        int count;

        List items = new ArrayList();
        Integer currentTransactionID = new Integer(0);
        Integer newTransactionID;
        List transItems = new ArrayList();
        Character currentStemID;

        for (int i = 0; i < selectedPoints.length; i++) {
            //for(int j=0;j<selectedPoints[i].length;j++){
            //if(selectedPoints[i][j]>0.0){
            Transaction newTrans = new Transaction(new Integer(i).toString(), selectedPoints[i]);
            for (count = 0; count < stemDescriptors.length; count++) {
                currentStemID = ((StemDescription) stemDescriptors[count]).getID();
                if (tree.containsKey(currentStemID)) {
                    currentNode = (MyTreeNode) tree.get(currentStemID);
                    currentNode.passTransaction(newTrans, level);
                }
            }
//			if (tree.containsKey(new Integer(j))) {
//			currentNode = (MyTreeNode)tree.get(new Integer(j));
//			Transaction newTrans = new Transaction(new Integer(i).toString(),selectedPoints[i]);
//			currentNode.passTransaction(newTrans,level);
//			}
        //}
        //}
        }


    }

    /**
     * removeInfrequentItemsets Once the infrequent itemsets have been identified,
     * this removes them from the hashtree.
     * @param minSup The minimum support for the search.
     */
    public void removeInfrequentItemsets(float minSup) {

        MyTreeNode currentNode;

        int count;
        Character currentStemID;

        for (count = 0; count < stemDescriptors.length; count++) {
            currentStemID = ((StemDescription) stemDescriptors[count]).getID();
            if (tree.containsKey(currentStemID)) {
                currentNode = (MyTreeNode) tree.get(currentStemID);
                currentNode.removeInfrequentItemsets(minSup);
            }
        }
    }

    /**
     * printTree Prints the contents of the tree to the screen.
     */
    public void printTree() {
        ArrayList treeContents = new ArrayList(tree.values());
        MyTreeNode currentNode;
        ArrayList bucketItemsets;
        ItemSet currentItemset;
        LinkedList currentItemsetItems;
        ListIterator items;
        System.out.println("Printing tree\n******************************");
        System.out.println("tree size " + treeContents.size());
        for (int count = 0; count < treeContents.size(); count++) {
            currentNode = (MyTreeNode) treeContents.get(count);
            currentNode.printContents();
        }
    }

    /**
     * printTree Prints the contents of the tree to the screen.
     * @param n The number of items in the current itemset, eg. 1-itemset.
     * @return The list of frequent itemsets of a particular length.
     */
    public LinkedList getFrequentNItemsets(int n) {
        LinkedList fNItemsetsArray = new LinkedList();

        MyTreeNode currentNode;

        Character currentStemID;

        for (int count = 0; count < stemDescriptors.length; count++) {
            currentStemID = ((StemDescription) stemDescriptors[count]).getID();
            if (tree.containsKey(currentStemID)) {
                currentNode = (MyTreeNode) tree.get(currentStemID);
                fNItemsetsArray.addAll(currentNode.getItemsets(n));
            }
        }
        return fNItemsetsArray;
    }

    /**
     * getAllFrequentItemsets Returns all the frequent itemsets, regardless of length.
     * @return The list of frequent itemsets.
     */
    public ArrayList getAllFrequentItemsets() {
        ArrayList fNItemsetsArray = new ArrayList();

        MyTreeNode currentNode;

        Character currentStemID;

        for (int count = 0; count < stemDescriptors.length; count++) {
            currentStemID = ((StemDescription) stemDescriptors[count]).getID();
            if (tree.containsKey(currentStemID)) {
                currentNode = (MyTreeNode) tree.get(currentStemID);
                fNItemsetsArray.addAll(currentNode.getAllItemsets());
            }
        }
        return fNItemsetsArray;
    }

    /**
     * printAllFrequentItemsets Prints all the frequent itemsets to screen.
     * @param numberOfTransactions The total number of transactions.
     * @param minSupPercentage The minimum support express as a percentage.
     */
    public void printAllFrequentItemsets(int numberOfTransactions, float minSupPercentage) {
        System.out.println("Frequent itemsets:");
        ArrayList itemsets = getAllFrequentItemsets();
        ItemSet currentItemset;
        for (int count = 0; count < itemsets.size(); count++) {
            currentItemset = (ItemSet) itemsets.get(count);
        //System.out.println("Itemset: " + currentItemset.toString(this.ngrams) + ", support: " + ((currentItemset.getSupport()*100)/numberOfTransactions) + "%.");
        }
    }

    /**
     * printAllFrequentItemsets Prints all the frequent itemsets to file.
     * @param filename The file to which the frequent itemset list should be written.
     * @param numberOfTransactions The total number of transactions.
     * @param minSupPercentage The minimum support express as a percentage.
     */
    public void printAllFrequentItemsets(String filename, int numberOfTransactions, int minSupPercentage) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening " + filename);
            System.exit(0);
        }
        ArrayList itemsets = getAllFrequentItemsets();
        ItemSet currentItemset;
        for (int count = 0; count < itemsets.size(); count++) {
            currentItemset = (ItemSet) itemsets.get(count);
        //outputStream.println("Itemset: " + currentItemset.toString(this.ngrams) + ", support: " + ((currentItemset.getSupport()*100)/numberOfTransactions) + "%.");
        }
        outputStream.close();
        System.out.println("--Frequent itemsets written to file.");
    }

}
