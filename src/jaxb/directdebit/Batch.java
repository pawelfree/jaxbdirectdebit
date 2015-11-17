package jaxb.directdebit;

import java.util.List;

/**
 *
 * @author paweldudek
 */
public class Batch {
    public Batch(List<DirectDebitTransaction> transactions, int daysToAdd) {
        this.transactions = transactions;
        this.daysToAdd = daysToAdd;
    }
    
    private final List<DirectDebitTransaction> transactions;
    private final int daysToAdd;

    public List<DirectDebitTransaction> getTransactions() {
        return transactions;
    }

    public int getDaysToAdd() {
        return daysToAdd;
    }
    
    
            
}
