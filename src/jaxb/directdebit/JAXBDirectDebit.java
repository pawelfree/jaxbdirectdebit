package jaxb.directdebit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author paweldudek
 */
public class JAXBDirectDebit {
    
    //www.genapps.pl/generatory/iban
    
    private static final List<String> IBANS = Arrays.asList(
            "PL84878400035440083410960616", 
            "PL08811100092964736862914089",
            "PL19102018533449974850536348",
            "PL51804610127286754562250726",
            "PL00102038442944930154144991",
            "PL84958411198737066327033897",
            "PL92124033928532154486446801",
            "PL91897310426686970123572914",
            "PL08171000077585631971186301",
            "PL67804210618606046541345817",
            "PL38101013974078119335554417",
            "PL86819100031403002218597513",
            "PL55847510459131665137793225",
            "PL80109023567191735160159548",
            "PL42150000280728034077955506",
            "PL35865810199700538305034497",
            "PL52811800025090802330342673",
            "PL55822610188806360410887712",
            "PL09109020371026918603384729",
            "PL66958410185396469287129533",
            "PL44968110256464644030527843");

    private final static String CREDITOR_NAME = "Honda Motor Europe Ltd.";
    private final static String CREDITOR_IBAN = "PL21221000090000000000104847";
    private final static String CREDITOR_COUNTRY = "GB";
    private final static String CREDITOR_ADDRESS_LINE_1 = "Ropemaker Place, 25 Ropemaker St.";
    private final static String CREDITOR_ADDRESS_LINE_2 = "London E2CY 9AN";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//       DocumentVerifier.verify("file002.xml");
        
        
        List<Batch> batches = new ArrayList<>();
        
        Party creditor = new Party(CREDITOR_NAME, CREDITOR_IBAN, CREDITOR_COUNTRY, CREDITOR_ADDRESS_LINE_1, CREDITOR_ADDRESS_LINE_2);

        Random generator = new Random();
        
        List<DirectDebitTransaction> transactions;
        
        UniqueId uniqueId = UniqueId.instance();
        
        for (int h = 0; h < 1; h++) {
            transactions = new ArrayList<>();
            for (int i = 1; i < 8; i++ ) {
                transactions.add(new DirectDebitTransaction(
                    generator.nextDouble()*Math.pow(10,generator.nextInt(3)+2),
                    uniqueId.nextId(),
                    new Party ("Debtor " + i + " name ",
                            IBANS.get(generator.nextInt(IBANS.size())), 
                            "PL" , 
                            "Debtor " + i + " address 1", 
                            "Debtor " + i + " address 2"),
                    "/NIP/9531542419/IDP/Testowy"+ uniqueId.nextId()));        
            }            
            batches.add(new Batch(transactions,h+9));
        }
        DocumentCreator.create(batches, creditor, "pain008.xml");        
    }
    
}
