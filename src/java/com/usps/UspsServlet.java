/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usps;

import com.datasource.ParchemPool;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author nail yusupov
 */
public class UspsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        if (request.getParameter("q") != null) {
            //make call to usps freight service
            generateUSPSsoapRequest(request.getParameter("q"));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void generateUSPSsoapRequest(String parameter) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String url = "https://www.ups.com/ups.app/xml/Rate";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(parameter), url);
            // print SOAP Response
            System.out.print("Response SOAP Message:");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            InputStream inStream = new ByteArrayInputStream(out.toByteArray());

            //if the response xml is desired in the String
            String strMsg = new String(out.toByteArray());
            System.out.println(strMsg);
            soapConnection.close();
        } catch (SOAPException ex) {
            Logger.getLogger(UspsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UspsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SOAPMessage createSOAPRequest(String parameter) {
        SOAPMessage soapMessage = null;
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
            envelope.addNamespaceDeclaration("upss", "http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0");
            envelope.addNamespaceDeclaration("wsf", "http://www.ups.com/schema/wsf");
            envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            envelope.addNamespaceDeclaration("common", "http://www.ups.com/XMLSchema/XOLTWS/Common/v1.0");

            // SOAP Body Generated Here
            // all of the nodes below are mandatory
            // if any optional nodes are desired refer to the ECHO API or SOAPUI for
            // exact Node names
            SOAPBody soapBody = envelope.getBody();

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("Security", "wsse");

            soapMessage.saveChanges();

            /* Print the request message */
            System.out.print("Request SOAP Message:");
            soapMessage.writeTo(System.out);

        } catch (SOAPException | IOException ex) {
            Logger.getLogger(UspsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return soapMessage;
    }

}
