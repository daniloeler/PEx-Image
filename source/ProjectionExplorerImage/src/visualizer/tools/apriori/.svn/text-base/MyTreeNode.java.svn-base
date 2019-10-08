
/**
 * @(#)MyTreeNode.java
 *
 * Represents a node in the HashTree.
 *
 * @author Adrian Bright
 */
package visualizer.tools.apriori;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import visualizer.tools.*;

public class MyTreeNode {
    /* The list of all the ItemSet at this node */

    private ArrayList bucket;
    /* The links to child leaves */
    private HashMap children;
    /* Stores the identifiers of each item, eg. '1' may represent "beer". */
    private List<Integer> itemsRef;
    /* Array which stores the definitions of each stem, ie. which keys hash into each stem. */
    StemDescription[] stemDescriptors;
    /* Identifies the level in the tree. */
    private int treeLevel;
    /* The number of itemsets that can be stored in each bucket. */
    private int bucketSize;
    /* The number of stems protruding from this node. */
    private int stemNum;
    /**
     * MyTreeNode Initialises the node.
     * @param buckSize The number of itemsets that can be stored in each bucket.
     * @param stems The number of stems protruding from this node.
     * @param itemsReference Stores the identifiers of each item.
     * @param stemDescs Stores the definitions of each stem.
     * @param level Identifies the level in the tree.
     */
    public MyTreeNode(int buckSize, int stems, List<Integer> itemsReference, StemDescription[] stemDescs, int level) //public MyTreeNode(int buckSize,List<Integer> itemsReference, int level)
    {
        stemNum = stems;
        bucketSize = buckSize;
        stemDescriptors = stemDescs;
        children = new HashMap();
        bucket = new ArrayList(bucketSize);
        itemsRef = itemsReference;
        treeLevel = level;
    }

    /**
     * addItemset Adds an itemset, either to this node's bucket or one of its children.
     * @param itemset The itemset that needs adding.
     */
    public void addItemset(ItemSet itemset) {
        if (treeLevel < itemset.size()) {
            Integer currentItem;
            Character currentStemID;
            MyTreeNode newBucket;
            int count;

            currentItem = itemset.getItem(treeLevel);


            for (count = 0; count < stemDescriptors.length && (!((StemDescription) stemDescriptors[count]).keyExists(currentItem)); count++) {
            }

            currentStemID = ((StemDescription) stemDescriptors[count]).getID();
            if (children.containsKey(currentStemID)) {
                ((MyTreeNode) children.get(currentStemID)).addItemset(itemset);
            } else {
                newBucket = new MyTreeNode(bucketSize, stemNum, itemsRef, stemDescriptors, treeLevel + 1);
                newBucket.addItemset(itemset);
                children.put(currentStemID, newBucket);
            }
        } else {
            bucket.add(itemset);
        }
    }

    /**
     * getBucketItemsets Returns the list of itemsets at this node.
     * @return The list of itemsets at this node.
     */
    public ArrayList getBucketItemsets() {
        return bucket;
    }

    /**
     * returnBucketItemsets Replaces the bucket in this node with the one provided.
     * @param The new list of itemsets at this node.
     */
    public void returnBucketItemsets(ArrayList newBucket) {
        bucket = newBucket;
    }

    /**
     * printContents Prints the contents of this node and its children to screen.
     */
    public void printContents() {
        ItemSet currentItemset;
        List<Integer> currentItemsetItems;
        ListIterator itItems;
        MyTreeNode currentChild;

        System.out.println("level " + treeLevel + " bucket contents:");
        for (int count = 0; count < bucket.size(); count++) {
            currentItemset = (ItemSet) bucket.get(count);
            currentItemsetItems = currentItemset.getItems();
            itItems = currentItemsetItems.listIterator(0);
            while (itItems.hasNext()) {
                System.out.print(itItems.next());
            }
            System.out.print(", support " + currentItemset.getSupport() + "|");
        }
        System.out.println("\nchildren contents:");
        for (int count = 0; count < stemDescriptors.length; count++) {
            if (children.containsKey(stemDescriptors[count].getID())) {
                currentChild = (MyTreeNode) children.get(stemDescriptors[count].getID());
                currentChild.printContents();
            }
        }

        System.out.println("**************************");
    }

    /**
     * getChildren Returns the list of children from this node.
     * @return The list of children at this node.
     */
    public ArrayList getChildren() {
        return new ArrayList(children.values());
    }

    /**
     * passTransaction Passes a transaction over this node and its children for support counting.
     * @param transaction The transaction that needs passing over the node.
     * @param activeLevel The required level in the three for this transaction.
     */
    public void passTransaction(Transaction transaction, int activeLevel) {
        if (treeLevel == activeLevel) /* This is because the 1-itemsets have already been processed before being entered into tree. */ {
            for (int arrayPos = 0; arrayPos < bucket.size(); arrayPos++) {
                if (transaction.isThisSubset(((ItemSet) bucket.get(arrayPos)).getItems())) {
                    ((ItemSet) bucket.get(arrayPos)).incrementSupport();
                }
            }
        }
        passTransactionOverChildren(transaction, activeLevel);
    }

    /**
     * passTransactionsOverChildren Passes a transaction over the node's children.
     * @param transaction The transaction that needs passing over the node.
     * @param activeLevel The required level in the three for this transaction.
     */
    public void passTransactionOverChildren(Transaction transaction, int activeLevel) {
        for (int count = 0; count < stemDescriptors.length; count++) {
            if (children.containsKey(stemDescriptors[count].getID())) {

                ((MyTreeNode) children.get(stemDescriptors[count].getID())).passTransaction(transaction, activeLevel);
            }
        }
    }

    /**
     * removeInfrequentItemsets Removes the infrequent itemsets from this node and its children.
     * @param minSup Minimum support.
     */
    public void removeInfrequentItemsets(float minSup) {
        ArrayList itemsetsToRemove = new ArrayList();
        for (int bucketPos = 0; bucketPos < bucket.size(); bucketPos++) {
            if (((ItemSet) bucket.get(bucketPos)).getSupport() < minSup) {
                itemsetsToRemove.add(bucket.get(bucketPos));
            }
        }

        for (int removeCount = 0; removeCount < itemsetsToRemove.size(); removeCount++) {
            bucket.remove(bucket.indexOf(itemsetsToRemove.get(removeCount)));
        }
        removeChildrenInfrequentItemsets(minSup);
    }

    /**
     * removeChildrenInfrequentItemsets Removes the infrequent itemsets from this node's children.
     * @param minSup Minimum support.
     */
    public void removeChildrenInfrequentItemsets(float minSup) {
        for (int count = 0; count < stemDescriptors.length; count++) {
            if (children.containsKey(stemDescriptors[count].getID())) {

                ((MyTreeNode) children.get(stemDescriptors[count].getID())).removeInfrequentItemsets(minSup);
            }
        }
    }

    /**
     * getItemsets Returns the itemsets from this node and its children at a particular level.
     * @param levelRequired The level in the hashtree required.
     * @return The list of itemsets from this node and its children at the specified level.
     */
    public ArrayList getItemsets(int levelRequired) {
        ArrayList itemsets = new ArrayList();
        if (treeLevel == levelRequired) {
            itemsets.addAll(bucket);
        }

        for (int count = 0; count < stemDescriptors.length; count++) {
            if (children.containsKey(stemDescriptors[count].getID())) {
                itemsets.addAll(((MyTreeNode) children.get(stemDescriptors[count].getID())).getItemsets(levelRequired));
            }
        }
        return itemsets;
    }

    /**
     * getAllItemsets Returns the itemsets from this node and its children.
     * @return The list of itemsets from this node and its children.
     */
    public ArrayList getAllItemsets() {
        ArrayList itemsets = new ArrayList();
        itemsets.addAll(bucket);
        for (int count = 0; count < stemDescriptors.length; count++) {
            if (children.containsKey(stemDescriptors[count].getID())) {
                itemsets.addAll(((MyTreeNode) children.get(stemDescriptors[count].getID())).getAllItemsets());
            }
        }
        return itemsets;
    }

}
