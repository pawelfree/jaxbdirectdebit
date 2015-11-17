package jaxb.directdebit;

import java.io.File;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import jaxb.directdebit.pain008.Document;
import jaxb.directdebit.xmlutils.PainUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author paweldudek
 */
public class DocumentCreator {
    
    private final static String SCHEMA_FILE_NAME = "pain008.xsd";
    
    public static void create(List<Batch> batches, Party creditor, String fileName) {
        
        Document pain008 = PainUtils.createPain008(batches, creditor);
        
        try {
            
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(SCHEMA_FILE_NAME));
            
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.setSchema(schema);
            
            marshaller.marshal(pain008, System.out);
            marshaller.marshal(pain008, new File(fileName));
        } 
        catch  (JAXBException | SAXException ex) {
            ex.printStackTrace();
        }
    }
}
