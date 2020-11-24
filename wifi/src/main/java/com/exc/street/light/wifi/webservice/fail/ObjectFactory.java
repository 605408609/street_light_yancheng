
package com.exc.street.light.wifi.webservice.fail;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.exc.street.light.wifi.webservice.fail package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryRadiusLogsResponse_QNAME = new QName("http://audit.webservice.smp.ruijie.com/", "queryRadiusLogsResponse");
    private final static QName _QueryRadiusLogs_QNAME = new QName("http://audit.webservice.smp.ruijie.com/", "queryRadiusLogs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.exc.street.light.wifi.webservice.fail
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryRadiusLogsResponse }
     * 
     */
    public QueryRadiusLogsResponse createQueryRadiusLogsResponse() {
        return new QueryRadiusLogsResponse();
    }

    /**
     * Create an instance of {@link QueryRadiusLogs }
     * 
     */
    public QueryRadiusLogs createQueryRadiusLogs() {
        return new QueryRadiusLogs();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRadiusLogsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://audit.webservice.smp.ruijie.com/", name = "queryRadiusLogsResponse")
    public JAXBElement<QueryRadiusLogsResponse> createQueryRadiusLogsResponse(QueryRadiusLogsResponse value) {
        return new JAXBElement<QueryRadiusLogsResponse>(_QueryRadiusLogsResponse_QNAME, QueryRadiusLogsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRadiusLogs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://audit.webservice.smp.ruijie.com/", name = "queryRadiusLogs")
    public JAXBElement<QueryRadiusLogs> createQueryRadiusLogs(QueryRadiusLogs value) {
        return new JAXBElement<QueryRadiusLogs>(_QueryRadiusLogs_QNAME, QueryRadiusLogs.class, null, value);
    }

}
