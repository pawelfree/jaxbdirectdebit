package jaxb.directdebit;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import jaxb.directdebit.pain002.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author paweldudek
 */
public class DocumentVerifier {
    
    private final static String SCHEMA_FILE_NAME = "pain002.xsd";
    
    public static void verify(String fileName) {
    
        try {
            
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(SCHEMA_FILE_NAME));
            
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            unmarshaller.setSchema(schema);
            
            
            unmarshaller.unmarshal(new File(fileName));
        } 
        catch  (JAXBException | SAXException ex) {
            ex.printStackTrace();
        }
    }
        
}
