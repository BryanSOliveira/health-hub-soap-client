package br.com.bryan.soapclient.handlers;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;

import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public class ClientAuthenticationHandler implements SOAPHandler<SOAPMessageContext> {
    private final String username;
    private final String password;

    public ClientAuthenticationHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {
            try {
                SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
                if (envelope.getHeader() == null) {
                    envelope.addHeader();
                }
                SOAPHeader header = envelope.getHeader();

                QName qnameUser = new QName("http://impl.webservice.bryan.com.br/", "username");
                SOAPHeaderElement userElement = header.addHeaderElement(qnameUser);
                userElement.addTextNode(username);

                QName qnamePass = new QName("http://impl.webservice.bryan.com.br/", "password");
                SOAPHeaderElement passElement = header.addHeaderElement(qnamePass);
                passElement.addTextNode(password);

            } catch (SOAPException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }
}
