package jaxb.directdebit;

/**
 *
 * @author paweldudek
 */
public class Party {

    private final String name;
    private final String iban;
    private final String country;
    private final String adressLine1;
    private final String addressLine2;
    private final String agentId;
    
    public Party(String name, String iban, String country, String adressLine1, String addressLine2) {
        this.name = name;
        this.iban = iban;
        this.country = country;
        this.adressLine1 = adressLine1;
        this.addressLine2 = addressLine2;
        this.agentId = iban.substring(4,12);
    }    

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public String getCountry() {
        return country;
    }

    public String getAdressLine1() {
        return adressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAgentId() {
        return agentId;
    }
    
}
