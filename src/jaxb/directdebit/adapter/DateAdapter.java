package jaxb.directdebit.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author paweldudek
 */
public class DateAdapter extends XmlAdapter<String, XMLGregorianCalendar>{
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public XMLGregorianCalendar unmarshal(String v) throws Exception {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(format.parse(v));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
    }

    @Override
    public String marshal(XMLGregorianCalendar v) throws Exception {
        return format.format(v.toGregorianCalendar().getTime());
    }
    
}
