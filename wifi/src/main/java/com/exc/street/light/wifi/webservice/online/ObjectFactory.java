
package com.exc.street.light.wifi.webservice.online;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.exc.street.light.wifi.webservice.online package. 
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

    private final static QName _QueryOnlineUsers_QNAME = new QName("http://endpoint.webservice.smp.ruijie.com/", "queryOnlineUsers");
    private final static QName _QueryOnlineUsersResponse_QNAME = new QName("http://endpoint.webservice.smp.ruijie.com/", "queryOnlineUsersResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.exc.street.light.wifi.webservice.online
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryOnlineUsersResponse }
     * 
     */
    public QueryOnlineUsersResponse createQueryOnlineUsersResponse() {
        return new QueryOnlineUsersResponse();
    }

    /**
     * Create an instance of {@link QueryOnlineUsers }
     * 
     */
    public QueryOnlineUsers createQueryOnlineUsers() {
        return new QueryOnlineUsers();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryOnlineUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.webservice.smp.ruijie.com/", name = "queryOnlineUsers")
    public JAXBElement<QueryOnlineUsers> createQueryOnlineUsers(QueryOnlineUsers value) {
        return new JAXBElement<QueryOnlineUsers>(_QueryOnlineUsers_QNAME, QueryOnlineUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryOnlineUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.webservice.smp.ruijie.com/", name = "queryOnlineUsersResponse")
    public JAXBElement<QueryOnlineUsersResponse> createQueryOnlineUsersResponse(QueryOnlineUsersResponse value) {
        return new JAXBElement<QueryOnlineUsersResponse>(_QueryOnlineUsersResponse_QNAME, QueryOnlineUsersResponse.class, null, value);
    }

}
