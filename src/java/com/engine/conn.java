/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine;

import com.datasource.ControlPanelPool;
import com.google.gson.Gson;
import com.soap.Quote;
import com.soap.data.Item;
import com.soap.data.QuoteData;
import com.soap.data.ResponseQuoteData;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author nail yusupov
 */
public class conn implements Runnable{
    
    String uuid;
    String pallets;
    String haz;
    String weight;
    String origin;
    String destination;
    
    public conn(String oz, String dz, String w, String h, String p, String u){
                uuid = u;
                pallets = p;
                haz= h;
                weight = w;
                origin = oz;
                destination = dz;
    }

    @Override
    public void run() {
            //QuoteServletData servletdata = new QuoteServletData(request.getParameter("id"));
            QuoteData data = new QuoteData();
            //String warehouseZip = lookupNearestWarehouse(request.getParameter("originzip"));
            data.setUserId("");
            data.setPassword("");
            data.setPickupDate(DateTime.now().toLocalDate().toString());
            data.setSaveQuote("false");
            data.setReturnMultipleCarriers("true");
            //data.setOriginZip(request.getParameter("originzip"));
            data.setNumberOfPallets(pallets);
            //data.setDestinationZip(warehouseZip);
            // data.setOriginZip(servletdata.getOriginZip());
            //data.setDestinationZip(servletdata.getWarehouseZip());
            Item item = new Item("0", (haz.equals("1") ? "85" : "65"), weight);
            ArrayList<Item> items = new ArrayList<>();
            items.add(item);
            data.setItems(items);
            //List<Item> tempItems = servletdata.getItems();
            //data.setItems((ArrayList<Item>) tempItems);
            data.setNotifyPriorDelivery(true);
            ArrayList<ResponseQuoteData> responseData = null;
            String productsWeight = weight;
            String productsClass = haz.equals("1") ? "haz" : "non-haz";
            //double tempW = 0.0;
            //String productsWeight = "";
            //String productsClass = "";
            //for(int i = 0; i < tempItems.size(); i++){
            //        tempW = Double.valueOf(tempItems.get(i).getItemWeight());
            //    try{
            //    }catch(Exception e){e.printStackTrace();}
            //    productsClass += tempItems.get(i).getItemClass()+ "; ";
            //}
            //productsWeight = String.valueOf(tempW);
            try {
                //Quote quote = new Quote(data);
                //responseData = quote.getResponseData();
                java.sql.Connection con = null;
                java.sql.PreparedStatement stmt = null;
                java.sql.ResultSet rs = null;
                con = ControlPanelPool.getInstance().getConnection();
                /*
                for (int i = 0; i < responseData.size(); i++) {
                    stmt = con.prepareStatement("INSERT INTO [dbo].[tblEchoCarrier] ([CarrierName] ,[TransitDays] ,[PickupDate] ,[SCAC] ,[OriginZip] ,[DestinationZip] ,[OriginTerminalContact] ,[OriginTerminalAddress] ,[OriginTerminalPhone1] ,[OriginTerminalPhone2] ,[OriginTerminalCsz] ,[DestinationTerminalContact] ,[DestinationTerminalAddress] ,[DestinationTerminalPhone1] ,[DestinationTerminalPhone2] ,[DestinationTerminalCsz] ,[BaseCharge] ,[Discount] ,[DiscountPercent] ,[FuelCharge] ,[AccessorialCharge] ,[TotalCharge] ,[ItemClass] ,[ItemWeight] ,[Leg] ,[LegUid], [Pallets], [timestamp]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE())");
                    stmt.setString(1, responseData.get(i).getCarrierName());
                    stmt.setString(2, responseData.get(i).getTransitDays());
                    stmt.setString(3, responseData.get(i).getPickupDate());
                    stmt.setString(4, responseData.get(i).getSCAC());
                    stmt.setString(5, request.getParameter("originzip"));
                    stmt.setString(6, warehouseZip);
                    stmt.setString(7, responseData.get(i).getOriginTerminalContact());
                    stmt.setString(8, responseData.get(i).getOriginTerminalAddress());
                    stmt.setString(9, responseData.get(i).getOriginTermainlPhone1());
                    stmt.setString(10, responseData.get(i).getOrigintermanilPhone2());
                    stmt.setString(11, responseData.get(i).getOriginTerminalCsz());
                    stmt.setString(12, responseData.get(i).getDestinationTerminalContact());
                    stmt.setString(13, responseData.get(i).getDestinationTerminalAddress());
                    stmt.setString(14, responseData.get(i).getDestinationTerminalPhone1());
                    stmt.setString(15, responseData.get(i).getDestinationTerminalPhone2());
                    stmt.setString(16, responseData.get(i).getDestinationTerminalCsz());
                    stmt.setString(17, responseData.get(i).getBaseCharge());
                    stmt.setString(18, responseData.get(i).getDiscount());
                    stmt.setString(19, responseData.get(i).getDiscountPercent());
                    stmt.setString(20, responseData.get(i).getFuelCharge());
                    stmt.setString(21, responseData.get(i).getAccesorialCharge());
                    stmt.setString(22, responseData.get(i).getTotalCharge());
                    stmt.setString(23, productsClass);
                    stmt.setString(24, productsWeight);
                    stmt.setString(25, "1");
                    stmt.setString(26, uuid);
                    stmt.setString(27, request.getParameter("pallets"));
                    stmt.executeUpdate();
                }
                data.setOriginZip(warehouseZip);
                data.setDestinationZip(request.getParameter("destinationzip"));
                data.setNotifyPriorDelivery(true);
                quote = new Quote(data);
                responseData = quote.getResponseData();
                for (int i = 0; i < responseData.size(); i++) {
                    stmt = con.prepareStatement("INSERT INTO [dbo].[tblEchoCarrier] ([CarrierName] ,[TransitDays] ,[PickupDate] ,[SCAC] ,[OriginZip] ,[DestinationZip] ,[OriginTerminalContact] ,[OriginTerminalAddress] ,[OriginTerminalPhone1] ,[OriginTerminalPhone2] ,[OriginTerminalCsz] ,[DestinationTerminalContact] ,[DestinationTerminalAddress] ,[DestinationTerminalPhone1] ,[DestinationTerminalPhone2] ,[DestinationTerminalCsz] ,[BaseCharge] ,[Discount] ,[DiscountPercent] ,[FuelCharge] ,[AccessorialCharge] ,[TotalCharge] ,[ItemClass] ,[ItemWeight] ,[Leg] ,[LegUid], [Pallets], [timestamp]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE())");
                    stmt.setString(1, responseData.get(i).getCarrierName());
                    stmt.setString(2, responseData.get(i).getTransitDays());
                    stmt.setString(3, responseData.get(i).getPickupDate());
                    stmt.setString(4, responseData.get(i).getSCAC());
                    stmt.setString(5, warehouseZip);
                    stmt.setString(6, request.getParameter("destinationzip"));
                    stmt.setString(7, responseData.get(i).getOriginTerminalContact());
                    stmt.setString(8, responseData.get(i).getOriginTerminalAddress());
                    stmt.setString(9, responseData.get(i).getOriginTermainlPhone1());
                    stmt.setString(10, responseData.get(i).getOrigintermanilPhone2());
                    stmt.setString(11, responseData.get(i).getOriginTerminalCsz());
                    stmt.setString(12, responseData.get(i).getDestinationTerminalContact());
                    stmt.setString(13, responseData.get(i).getDestinationTerminalAddress());
                    stmt.setString(14, responseData.get(i).getDestinationTerminalPhone1());
                    stmt.setString(15, responseData.get(i).getDestinationTerminalPhone2());
                    stmt.setString(16, responseData.get(i).getDestinationTerminalCsz());
                    stmt.setString(17, responseData.get(i).getBaseCharge());
                    stmt.setString(18, responseData.get(i).getDiscount());
                    stmt.setString(19, responseData.get(i).getDiscountPercent());
                    stmt.setString(20, responseData.get(i).getFuelCharge());
                    stmt.setString(21, responseData.get(i).getAccesorialCharge());
                    stmt.setString(22, responseData.get(i).getTotalCharge());
                    stmt.setString(23, productsClass);
                    stmt.setString(24, productsWeight);
                    stmt.setString(25, "2");
                    stmt.setString(26, uuid);
                    stmt.setString(27, request.getParameter("pallets"));
                    stmt.executeUpdate();
                }*/
                stmt = con.prepareStatement("INSERT INTO [dbo].[tblEchoCarrier] ([CarrierName] ,[TransitDays] ,[PickupDate] ,[SCAC] ,[OriginZip] ,[DestinationZip] ,[OriginTerminalContact] ,[OriginTerminalAddress] ,[OriginTerminalPhone1] ,[OriginTerminalPhone2] ,[OriginTerminalCsz] ,[DestinationTerminalContact] ,[DestinationTerminalAddress] ,[DestinationTerminalPhone1] ,[DestinationTerminalPhone2] ,[DestinationTerminalCsz] ,[BaseCharge] ,[Discount] ,[DiscountPercent] ,[FuelCharge] ,[AccessorialCharge] ,[TotalCharge] ,[ItemClass] ,[ItemWeight] ,[Leg] ,[LegUid], [Pallets],[timestamp]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE())");
                data.setOriginZip(origin);
                data.setDestinationZip(destination);
                Quote quote = new Quote(data);
                responseData = quote.getResponseData();
                for (int i = 0; i < responseData.size(); i++) {
                    stmt.setString(1, responseData.get(i).getCarrierName());
                    stmt.setString(2, responseData.get(i).getTransitDays());
                    stmt.setString(3, responseData.get(i).getPickupDate());
                    stmt.setString(4, responseData.get(i).getSCAC());
                    stmt.setString(5, origin);
                    stmt.setString(6, destination);
                    stmt.setString(7, responseData.get(i).getOriginTerminalContact());
                    stmt.setString(8, responseData.get(i).getOriginTerminalAddress());
                    stmt.setString(9, responseData.get(i).getOriginTermainlPhone1());
                    stmt.setString(10, responseData.get(i).getOrigintermanilPhone2());
                    stmt.setString(11, responseData.get(i).getOriginTerminalCsz());
                    stmt.setString(12, responseData.get(i).getDestinationTerminalContact());
                    stmt.setString(13, responseData.get(i).getDestinationTerminalAddress());
                    stmt.setString(14, responseData.get(i).getDestinationTerminalPhone1());
                    stmt.setString(15, responseData.get(i).getDestinationTerminalPhone2());
                    stmt.setString(16, responseData.get(i).getDestinationTerminalCsz());
                    stmt.setString(17, responseData.get(i).getBaseCharge());
                    stmt.setString(18, responseData.get(i).getDiscount());
                    stmt.setString(19, responseData.get(i).getDiscountPercent());
                    stmt.setString(20, responseData.get(i).getFuelCharge());
                    stmt.setString(21, responseData.get(i).getAccesorialCharge());
                    stmt.setString(22, responseData.get(i).getTotalCharge());
                    stmt.setString(23, productsClass);
                    stmt.setString(24, productsWeight);
                    stmt.setString(25, "0");
                    stmt.setString(26, uuid);
                    stmt.setString(27, pallets);
                    stmt.executeUpdate();
                }
                con.close();
            } catch (Exception ex) {
                Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
}
