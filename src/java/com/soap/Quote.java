/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.soap.data.*;

/**
 *
 * @author nail yusupov
 */
public class Quote {

    private ArrayList<ResponseQuoteData> responseDataList;

    public Quote(QuoteData data) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://services.echo.com/Quote.asmx";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(data), url);
        // print SOAP Response
        System.out.print("Response SOAP Message:");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        InputStream inStream = new ByteArrayInputStream(out.toByteArray());

        //if the response xml is desired in the String
        String strMsg = new String(out.toByteArray());
        System.out.println(strMsg);
        soapConnection.close();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inStream);

        Node soapEnvelope = getNode("soap:Envelope", document.getChildNodes());
        Node soapBody = getNode("soap:Body", soapEnvelope.getChildNodes());
        Node GetQuoteResponse = getNode("GetQuoteResponse", soapBody.getChildNodes());
        Node GetQuoteResult = getNode("GetQuoteResult", GetQuoteResponse.getChildNodes());
        Node RateQuote = getNode("RateQuote", GetQuoteResult.getChildNodes());
        Node RateDetails = getNode("RateDetails", RateQuote.getChildNodes());

        NodeList carriers = RateDetails.getChildNodes();

        responseDataList = new ArrayList<ResponseQuoteData>();

        for (int i = 0; i < carriers.getLength(); i++) {

            Node currentNode = carriers.item(i);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

                ResponseQuoteData responseData = new ResponseQuoteData();

                Node RateDetail = currentNode;
                Node Carrier = getNode("Carrier", RateDetail.getChildNodes());

                String CarrierName = getNodeValue("CarrierName", Carrier.getChildNodes());
                String TransitDays = getNodeValue("TransitDays", Carrier.getChildNodes());
                String SCAC = getNodeValue("SCAC", Carrier.getChildNodes());

                Node OriginTerminal = getNode("OriginTerminal", Carrier.getChildNodes());

                String OriginTerminalContact = getNodeValue("Contact", OriginTerminal.getChildNodes());
                String OriginTerminalAddress = getNodeValue("Address", OriginTerminal.getChildNodes());
                String OriginTerminalPhone1 = getNodeValue("Phone1", OriginTerminal.getChildNodes());
                String OriginTerminalPhone2 = getNodeValue("Phone2", OriginTerminal.getChildNodes());
                String OriginTerminalCsz = getNodeValue("Csz", OriginTerminal.getChildNodes());

                Node DestinationTerminal = getNode("DestinationTerminal", Carrier.getChildNodes());

                String DestinationTerminalContact = getNodeValue("Contact", DestinationTerminal.getChildNodes());
                String DestinationTerminalAddress = getNodeValue("Address", DestinationTerminal.getChildNodes());
                String DestinationTerminalPhone1 = getNodeValue("Phone1", DestinationTerminal.getChildNodes());
                String DestinationTerminalPhone2 = getNodeValue("Phone2", DestinationTerminal.getChildNodes());
                String DestinationTerminalCsz = getNodeValue("Csz", DestinationTerminal.getChildNodes());

                Node Charges = getNode("Charges", RateDetail.getChildNodes());

                String BaseCharge = getNodeValue("BaseCharge", Charges.getChildNodes());
                String Discount = getNodeValue("Discount", Charges.getChildNodes());
                String DiscountPercent = getNodeValue("DiscountPercent", Charges.getChildNodes());
                String FuelCharge = getNodeValue("FuelCharge", Charges.getChildNodes());
                String AccessorialCharge = getNodeValue("AccessorialCharge", Charges.getChildNodes());
                String TotalCharge = getNodeValue("TotalCharge", Charges.getChildNodes());

                responseData.setCarrierName(CarrierName);
                responseData.setTransitDays(TransitDays);
                responseData.setSCAC(SCAC);
                responseData.setOriginTerminalContact(OriginTerminalContact);
                responseData.setOriginTerminalAddress(OriginTerminalAddress);
                responseData.setOriginTermainlPhone1(OriginTerminalPhone1);
                responseData.setOrigintermanilPhone2(OriginTerminalPhone2);
                responseData.setOriginTerminalCsz(OriginTerminalCsz);
                responseData.setDestinationTerminalContact(DestinationTerminalContact);
                responseData.setDestinationTerminalPhone1(DestinationTerminalPhone1);
                responseData.setDestinationTerminalPhone2(DestinationTerminalPhone2);
                responseData.setDestinationTerminalAddress(DestinationTerminalAddress);
                responseData.setDestinationTerminalCsz(DestinationTerminalCsz);
                responseData.setBaseCharge(BaseCharge);
                responseData.setDiscount(Discount);
                responseData.setDiscountPercent(DiscountPercent);
                responseData.setFuelCharge(FuelCharge);
                responseData.setAccesorialCharge(AccessorialCharge);
                responseData.setTotalCharge(TotalCharge);

                responseDataList.add(responseData);
                // System.out.println(responseData.getSCAC());
            }
        }
    }

    public ArrayList<ResponseQuoteData> getResponseData() {
        return responseDataList;
    }

    protected static Node getNode(String tagName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }

    protected static String getNodeValue(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            Node data = childNodes.item(x);
            if (data.getNodeType() == Node.TEXT_NODE) {
                return data.getNodeValue();
            }
        }
        return "";
    }

    protected static String getNodeValue(String tagName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++) {
                    Node data = childNodes.item(y);
                    if (data.getNodeType() == Node.TEXT_NODE) {
                        return data.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    protected static String getNodeAttr(String attrName, Node node) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }

    protected static String getNodeAttr(String tagName, String attrName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++) {
                    Node data = childNodes.item(y);
                    if (data.getNodeType() == Node.ATTRIBUTE_NODE) {
                        if (data.getNodeName().equalsIgnoreCase(attrName)) {
                            return data.getNodeValue();
                        }
                    }
                }
            }
        }

        return "";
    }

    private static SOAPMessage createSOAPRequest(QuoteData data) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // !IMPORTANT
        // all the data is hardcoded at this point
        // however this is where the data should be extracted from the database
        // by calling Data data = new Connection().getData();
        // and than parsing all the content in the soap message
        // the content is already parsed it just requires the live data

        String serverURI = "http://www.echo.com/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("echo", serverURI);

        // SOAP Body Generated Here
        // all of the nodes below are mandatory
        // if any optional nodes are desired refer to the ECHO API or SOAPUI for
        // exact Node names
        SOAPBody soapBody = envelope.getBody();
        SOAPElement getQuote = soapBody.addChildElement("GetQuote", "echo");
        SOAPElement echoRateRequest = getQuote.addChildElement("echoRateRequest", "echo");
        SOAPElement authInfo = echoRateRequest.addChildElement("AuthInfo", "echo");
        SOAPElement UserId = authInfo.addChildElement("UserId", "echo");
        UserId.addTextNode(data.getUserId());
        SOAPElement Password = authInfo.addChildElement("Password", "echo");
        Password.addTextNode(data.getPassword());
        SOAPElement Requests = echoRateRequest.addChildElement("Requests", "echo");
        SOAPElement Request = Requests.addChildElement("Request", "echo");
        SOAPElement Items = Request.addChildElement("Items", "echo");
        String accessorialHazMatPickupcharge = "";
        for (int i = 0; i < data.getItems().size(); i++) {
            SOAPElement Item = Items.addChildElement("Item", "echo");
            SOAPElement Class = Item.addChildElement("Class", "echo");
            Class.addTextNode(data.getItems().get(i).getItemClass());
            accessorialHazMatPickupcharge = data.getItems().get(i).getItemClass().equals("85") ? "8" : "";
            SOAPElement Weight = Item.addChildElement("Weight", "echo");
            Weight.addTextNode(data.getItems().get(i).getItemWeight());
        }
        SOAPElement Origin = Request.addChildElement("Origin", "echo");
        SOAPElement Zip = Origin.addChildElement("Zip", "echo");
        Zip.addTextNode(data.getOriginZip());
        SOAPElement Destination = Request.addChildElement("Destination", "echo");
        SOAPElement Zip1 = Destination.addChildElement("Zip", "echo");
        Zip1.addTextNode(data.getDestinationZip());
        if (data.getNotifyPriorDelivery() || !accessorialHazMatPickupcharge.isEmpty()) {
            SOAPElement Accessorials = Request.addChildElement("Accessorials", "echo");
            if (data.getNotifyPriorDelivery()) {
                SOAPElement Accessorial = Accessorials.addChildElement("Accessorial", "echo");
                SOAPElement AccessorialId = Accessorial.addChildElement("AccessorialId", "echo");
                AccessorialId.addTextNode("14");
            }
            if (!accessorialHazMatPickupcharge.isEmpty()) {
                SOAPElement Accessorial1 = Accessorials.addChildElement("Accessorial", "echo");
                SOAPElement AccessorialId1 = Accessorial1.addChildElement("AccessorialId", "echo");
                AccessorialId1.addTextNode(accessorialHazMatPickupcharge);
            }
        }
        SOAPElement PickupDate = Request.addChildElement("PickupDate", "echo");
        PickupDate.addTextNode(data.getPickupDate());
        SOAPElement palletQty = Request.addChildElement("PalletQty","echo");
        palletQty.addTextNode(data.getNumberOfPallets());
        SOAPElement ReturnMultipleCarriers = Request.addChildElement("ReturnMultipleCarriers", "echo");
        ReturnMultipleCarriers.addTextNode(data.getReturnMultipleCarriers());
        SOAPElement SaveQuote = Request.addChildElement("SaveQuote", "echo");
        SaveQuote.addTextNode(data.getSaveQuote());

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "GetQuote");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }
}
