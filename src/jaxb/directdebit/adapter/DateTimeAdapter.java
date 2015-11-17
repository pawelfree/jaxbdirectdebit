package jaxb.directdebit.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author paweldudek
 */
public class DateTimeAdapter extends XmlAdapter<String, XMLGregorianCalendar>{
    DateFormat format;
    
    {
        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
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
