
/**
 * @(#)Transaction.java
 *
 * Represents a single transaction, containing a set of >=1 items .
 *
 * @author Adrian Bright
 */
package visualizer.tools.apriori;

import java.util.List;

public class Transaction {

    private String tID;
    /* A list of item numbers stored in String format */
    private float[] items;
    /**
     * Transaction Initialises the Transaction.
     */
    public Transaction() {
    }

    /**
     * Transaction Initialises the Transaction with a new ID and some items.
     * @param transID The transaction ID.
     * @param inputItems The list of items contained in this transaction.
     */
    public Transaction(String transID, float[] inputItems) {
        tID = transID;
        items = inputItems;
    }

    /**
     * setTID Sets the transaction ID.
     * @param id The new transaction ID.
     */
    public void setTID(String id) {
        tID = id;
    }

    /**
     * setItems Sets the items included in this transaction, in order.
     * @param itemsIn The list of items to be added.
     */
    public void setItems(float[] itemsIn) {
        items = itemsIn;
    }

    /**
     * getTID Returns the ID of this transaction.
     * @return The transaction ID.
     */
    public String getTID() {
        return tID;
    }

    /**
     * getItems Returns the items of this transaction.
     * @return The transaction items.
     */
    public float[] getItems() {
        return items;
    }

    /**
     * isThisSubset Checks if the specified itemset is a subset of this transaction.
     * @param itemset The itemset to be checked.
     * @return Whether or not the specified itemset is indeed a subset of the transaction.
     */
    public boolean isThisSubset(List<Integer> itemset) {


        int numItemsMatching = 0;
        for (Integer currentItem : itemset) {

            if (items[currentItem.intValue()] > 0.0) {
                numItemsMatching++;
            }
        }
        if (numItemsMatching == itemset.size()) {
            return true;
        } else {
            return false;
        }
    }

}
