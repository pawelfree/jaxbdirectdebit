package jaxb.directdebit.xmlutils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import jaxb.directdebit.Batch;
import jaxb.directdebit.Party;
import jaxb.directdebit.DirectDebitTransaction;
import jaxb.directdebit.UniqueId;
import jaxb.directdebit.pain008.AccountIdentification4Choice;
import jaxb.directdebit.pain008.ActiveOrHistoricCurrencyAndAmount;
import jaxb.directdebit.pain008.BranchAndFinancialInstitutionIdentification4;
import jaxb.directdebit.pain008.CashAccount16;
import jaxb.directdebit.pain008.ClearingSystemMemberIdentification2;
import jaxb.directdebit.pain008.CustomerDirectDebitInitiationV02;
import jaxb.directdebit.pain008.DirectDebitTransactionInformation9;
import jaxb.directdebit.pain008.Document;
import jaxb.directdebit.pain008.FinancialInstitutionIdentification7;
import jaxb.directdebit.pain008.GenericOrganisationIdentification1;
import jaxb.directdebit.pain008.GroupHeader39;
import jaxb.directdebit.pain008.OrganisationIdentification4;
import jaxb.directdebit.pain008.Party6Choice;
import jaxb.directdebit.pain008.PartyIdentification32;
import jaxb.directdebit.pain008.PaymentIdentification1;
import jaxb.directdebit.pain008.PaymentInstructionInformation4;
import jaxb.directdebit.pain008.PaymentMethod2Code;
import jaxb.directdebit.pain008.PostalAddress6;
import jaxb.directdebit.pain008.RemittanceInformation5;

/**
 *
 * @author paweldudek
 */
public class PainUtils {
    
    private final static GregorianCalendar calendar;
    
    static {
        calendar = new GregorianCalendar();
    }
    
    public static Document createPain008(List<Batch> batches, Party creditor) {
        
        Document pain008 = new Document();
        
        try {
            calendar.setTime(new Date());
            
            CustomerDirectDebitInitiationV02 cstmrDrctDbtInitn = new CustomerDirectDebitInitiationV02();
            cstmrDrctDbtInitn.setGrpHdr(PainUtils.createGroupHeader(batches, creditor.getIban()));
            
            batches.forEach(batch -> 
                            cstmrDrctDbtInitn.getPmtInf().add(createPaymentInstructionInformation(creditor, batch)));

            pain008.setCstmrDrctDbtInitn(cstmrDrctDbtInitn);

        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(PainUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pain008;
    }   
    
    private static GroupHeader39 createGroupHeader(List<Batch> batches, String customerIBAN) throws DatatypeConfigurationException {
        GroupHeader39 grpHdr = new GroupHeader39();
        grpHdr.setMsgId(((Long) (calendar.getTimeInMillis())).toString());
        grpHdr.setCreDtTm(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));

        Integer noOfTransactions = batches.stream().map(i -> i.getTransactions().size()).reduce(0, (j,k) -> j+k);
        grpHdr.setNbOfTxs(Integer.toString(noOfTransactions));

        BigDecimal sum = batches
                .stream()
                .flatMap(batch -> batch.getTransactions().stream())
                .map(DirectDebitTransaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        grpHdr.setCtrlSum(sum);
        grpHdr.setInitgPty(getInitiatingParty(customerIBAN));
        
        return grpHdr;
    }
    
    private static PaymentInstructionInformation4 createPaymentInstructionInformation(Party creditor, Batch batch) {
        
        PaymentInstructionInformation4 pii = new PaymentInstructionInformation4();
        pii.setPmtInfId(getPaymentInformationIdentification());
        pii.setPmtMtd(PaymentMethod2Code.DD);
        pii.setBtchBookg(Boolean.FALSE);
       
        calendar.add(GregorianCalendar.DATE, batch.getDaysToAdd());
        try {
            pii.setReqdColltnDt(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(PainUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pii.setCdtr(PainUtils.getParty(creditor));        
        pii.setCdtrAcct(getPlnAccount(creditor.getIban()));
        pii.setCdtrAgt(getAgent(creditor.getAgentId()));     
        
        batch.getTransactions().forEach(item -> pii.getDrctDbtTxInf().add(createDirectDebitTransactionInformation(item)));
        
        return pii;
    }    
    
    private static String getPaymentInformationIdentification() {
        UniqueId uniqueId = UniqueId.instance();
        return uniqueId.nextId();
    }
         
    private static PartyIdentification32 getInitiatingParty(String partyIBAN) {
        GenericOrganisationIdentification1 genericId = new GenericOrganisationIdentification1();
        genericId.setId(partyIBAN);
        
        OrganisationIdentification4 orgId = new OrganisationIdentification4();
        orgId.getOthr().add(genericId);
        
        Party6Choice id = new  Party6Choice();
        id.setOrgId(orgId);
        
        PartyIdentification32 initgPty = new PartyIdentification32();
        initgPty.setId(id);
        
        return initgPty;
    }
    
    private static DirectDebitTransactionInformation9 createDirectDebitTransactionInformation(DirectDebitTransaction transaction) {
        
        DirectDebitTransactionInformation9 ddti = new DirectDebitTransactionInformation9();
        
        PaymentIdentification1 pi = new PaymentIdentification1();
        pi.setEndToEndId(transaction.getEndToEndId());

        ddti.setPmtId(pi);
        ddti.setInstdAmt(getInstructedAmount(transaction.getValue()));
        ddti.setDbtrAgt(getAgent(transaction.getParty().getAgentId()));
        ddti.setDbtr(getParty(transaction.getParty()));
        ddti.setDbtrAcct(getPlnAccount(transaction.getParty().getIban()));
        ddti.setRmtInf(getRemittanceInformation(transaction.getRemittanceInf()));
        
        return ddti;
    }
    
    private static ActiveOrHistoricCurrencyAndAmount getInstructedAmount(BigDecimal value) {
        ActiveOrHistoricCurrencyAndAmount result = new ActiveOrHistoricCurrencyAndAmount();
        
        result.setCcy("PLN");
        result.setValue(value);
        
        return result;
    }

    private static RemittanceInformation5 getRemittanceInformation(String remittanceInformation) {
        RemittanceInformation5 ri = new RemittanceInformation5();
        ri.setUstrd(remittanceInformation);
        return ri;
    }    
        
    private static BranchAndFinancialInstitutionIdentification4 getAgent(String clearingMemberSystemId) {
        BranchAndFinancialInstitutionIdentification4 bafii = new BranchAndFinancialInstitutionIdentification4();
        FinancialInstitutionIdentification7 fii = new FinancialInstitutionIdentification7();
        ClearingSystemMemberIdentification2 csmi = new ClearingSystemMemberIdentification2();
        csmi.setMmbId(clearingMemberSystemId);
        fii.setClrSysMmbId(csmi);
        bafii.setFinInstnId(fii);
        
        return bafii;
    }    
    
    private static CashAccount16 getPlnAccount(String account) {
        AccountIdentification4Choice aic = new AccountIdentification4Choice();
        aic.setIBAN(account);
        
        CashAccount16 ca = new CashAccount16();
        ca.setId(aic);
        ca.setCcy("PLN");
        
        return ca;
    }
    
    private static PartyIdentification32 getParty(Party party) {

        PostalAddress6 address = new PostalAddress6();
        address.setCtry(party.getCountry());
        address.getAdrLine().add(party.getAdressLine1());
        address.getAdrLine().add(party.getAddressLine2());
        
        PartyIdentification32 partyId = new PartyIdentification32();
        partyId.setNm(party.getName());
        partyId.setPstlAdr(address);
        
        return partyId;
    }   
}
