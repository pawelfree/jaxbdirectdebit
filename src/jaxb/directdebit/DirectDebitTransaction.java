package jaxb.directdebit;

import java.math.BigDecimal;

/**
 *
 * @author paweldudek
 */
public class DirectDebitTransaction {
    
    private DirectDebitTransaction() {
        
    }
    
    public DirectDebitTransaction (
            Double value, 
            String endToEndId,
            Party party,
            String remittanceInf) {
        
        this.value = new BigDecimal(((Double)((double) Math.round(value*100)/100)).toString()).setScale(2);
        this.endToEndId = endToEndId;
        this.party = party;
        this.remittanceInf = remittanceInf;
    }
    
    private Party party;
    private BigDecimal value; //amount
    private String endToEndId; //16 charachters
    private String remittanceInf;

    public Party getParty() {
        return party;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public String getRemittanceInf() {
        return remittanceInf;
    }
    
}
