package com.sc.bus.service;
import org.junit.Test;
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

/**
 * @author Tom Bujok
 * @since 1.0.0
 */
public class SoapServerExamplesTest {

    @Test
    public void createServer() {
        SoapServer server = SoapServer.builder()
                .httpPort(9090)
                .build();
        server.start();
        server.stop();
    }

    @Test
    public void createServer_registerAutoResponder() throws WSDLException {
        SoapServer server = SoapServer.builder()
                .httpPort(9090)
                .build();
        server.start();

        URL wsdlUrl = ResourceUtils.getResourceWithAbsolutePackagePath("/", "wsdl/stockquote-service.wsdl");
        Wsdl parser = Wsdl.parse(wsdlUrl);
        SoapBuilder builder = parser.binding().localPart("StockQuoteSoapBinding").find();
        AutoResponder responder = new AutoResponder(builder);

        server.registerRequestResponder("/service", responder);
        server.stop();
    }

    @Test
    public void createServer_registerCustomResponder() throws WSDLException {
        SoapServer server = SoapServer.builder()
                .httpPort(9090)
                .build();
        server.start();

        URL wsdlUrl = ResourceUtils.getResourceWithAbsolutePackagePath("/", "wsdl/stockquote-service.wsdl");
        Wsdl parser = Wsdl.parse(wsdlUrl);
        final SoapBuilder builder = parser.binding().localPart("StockQuoteSoapBinding").find();

        AbstractResponder customResponder = new AbstractResponder(builder) {
            @Override
            public Source respond(SoapOperation invokedOperation, SoapMessage message) {
                try {
                    // build the response using builder
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
        server.stop();
    }

}