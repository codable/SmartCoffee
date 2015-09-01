package com.sc.bus.service;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
import org.reficio.ws.client.core.SoapClient;
import org.reficio.ws.common.ResourceUtils;
import org.reficio.ws.common.XmlUtils;
import org.reficio.ws.server.core.SoapServer;
import org.reficio.ws.server.responder.AbstractResponder;
import org.reficio.ws.server.responder.AutoResponder;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;

import java.net.URL;
import java.util.Iterator;



public class SoapClientExamplesTest {
	static SoapServer server = SoapServer.builder().httpPort(9090).build();

	public static void startServer() {
        
        server.start();

        URL wsdlUrl = ResourceUtils.getResource("smartcoffee-service.wsdl");
        Wsdl parser = Wsdl.parse(wsdlUrl);
        final SoapBuilder builder = parser.binding().localPart("SmartCoffeeSoapBinding").find();

        AutoResponder responder = new AutoResponder(builder);

        
        AbstractResponder customResponder = new AbstractResponder(builder) {
            @Override
            public Source respond(SoapOperation invokedOperation, SoapMessage message) {
                try {
                    // build the response using builder
                	
                	Document q = message.getDocument();
                	System.out.println(q.toString());
                    
                    String response = builder.buildOutputMessage(invokedOperation);
                    // here you can tweak the response -> for example with XSLT
                    //...
                    return XmlUtils.xmlStringToSource(response);
                } catch (Exception e) {
                    // will automatically generate SOAP-FAULT
                    throw new RuntimeException("my custom error", e);
                }
            }
        };

        server.registerRequestResponder("/service", customResponder);
        //server.stop();
    }
	
    public static void main(String[] ss) throws Exception, SAXException, WSDLException {
    	startServer();
    	
        // construct the client
        String url = String.format("http://localhost:%d%s", 9090, "/service");
        SoapClient client = SoapClient.builder().endpointUri(url).build();

        URL wsdlUrl = ResourceUtils.getResource("smartcoffee-service.wsdl");
        Wsdl parser = Wsdl.parse(wsdlUrl);
        SoapBuilder soapBuilder = parser.binding().localPart("SmartCoffeeSoapBinding").find();

        // get the operation to invoked -> assumption our operation is the first operation in the WSDL's
        SoapOperation operation = soapBuilder.operation().name("postesalescreate").find();

        // construct the request
        String request1 = soapBuilder.buildInputMessage(operation);
        // post the request to the server
        //String response1 = client.post(request1);
        // get the response
        

        String request =
        		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempurl.org\">\n" +
        				"   <soapenv:Header/>\n"+
        				"   <soapenv:Body>\n"+
        				"      <tem:postesalescreate>\n"+
        				"         <tem:astr_request>\n"+
        				"            <book1>\n"+
        				"               <name>gero et</name>\n"+
        				"               <author>sonoras imperio</author>"+
        				"               <price>quae divum incedo</price>\n"+
        				"            </book1>\n"+
        				"         </tem:astr_request>\n"+
        				"      </tem:postesalescreate>\n"+
        				"   </soapenv:Body>\n"+
        				"</soapenv:Envelope>";

        String response1 = client.post(request);

        String expectedResponse = "" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:stoc=\"http://reficio.org/stockquote.wsdl\" xmlns:stoc1=\"http://reficio.org/stockquote.xsd\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <stoc:GetLastTradePriceResponse>\n" +
                "         <stoc1:TradePrice>\n" +
                "            <price>?</price>\n" +
                "         </stoc1:TradePrice>\n" +
                "      </stoc:GetLastTradePriceResponse>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        
        //System.out.println(request);
        //System.out.println(response1);
        server.stop();
    }

}