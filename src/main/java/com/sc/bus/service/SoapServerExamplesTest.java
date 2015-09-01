package com.sc.bus.service;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
import org.reficio.ws.common.ResourceUtils;
import org.reficio.ws.common.XmlUtils;
import org.reficio.ws.server.core.SoapServer;
import org.reficio.ws.server.responder.AbstractResponder;
import org.reficio.ws.server.responder.AutoResponder;
import org.springframework.ws.soap.SoapMessage;

import javax.wsdl.WSDLException;
import javax.xml.transform.Source;

import java.net.URL;

public class SoapServerExamplesTest {

    public static void main(String argvp[]) throws WSDLException {
        SoapServer server = SoapServer.builder().httpPort(9090).build();
        server.start();

        URL wsdlUrl = ResourceUtils.getResource("smartcoffee-service.wsdl");
        Wsdl parser = Wsdl.parse(wsdlUrl);
        final SoapBuilder builder = parser.binding().localPart("StockQuoteSoapBinding").find();

        AutoResponder responder = new AutoResponder(builder);

        
        AbstractResponder customResponder = new AbstractResponder(builder) {
            @Override
            public Source respond(SoapOperation invokedOperation, SoapMessage message) {
                try {
                    // build the response using builder
                	System.out.println(invokedOperation);
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

}