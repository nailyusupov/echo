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
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbutils.DbUtils;
import org.joda.time.DateTime;

/**
 *
 * @author nail yusupov
 */
public class QuoteServlet extends HttpServlet {

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

        /*if (request.getParameter("id") != null) {
            QuoteServletData servletdata = new QuoteServletData(request.getParameter("id"));
            QuoteData data = new QuoteData();
            data.setUserId("ER33218");
            data.setPassword("Echo3218");
            data.setPickupDate(DateTime.now().toLocalDate().toString());
            data.setSaveQuote("false");
            data.setReturnMultipleCarriers("true");
            data.setOriginZip(servletdata.getOriginZip());
            data.setDestinationZip(servletdata.getWarehouseZip());
            //Item item = new Item("0", servletdata.getProductClass(), servletdata.getProductWeight());
            //ArrayList<Item> items = new ArrayList<>();
            //items.add(item);
            //data.setItems(items);
            List<Item> tempItems = servletdata.getItems();
            data.setItems((ArrayList<Item>) tempItems);
            data.setNotifyPriorDelivery(true);
            ArrayList<ResponseQuoteData> responseData = null;
            double tempW = 0.0;
            String productsWeight = "";
            String productsClass = "";
            for (int i = 0; i < tempItems.size(); i++) {
                try {
                    tempW = Double.valueOf(tempItems.get(i).getItemWeight());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                productsClass += tempItems.get(i).getItemClass() + "; ";
            }
            productsWeight = String.valueOf(tempW);
            try {
                Quote quote = new Quote(data);
                responseData = quote.getResponseData();
                String uuid = UUID.randomUUID().toString();
                java.sql.Connection con = null;
                java.sql.PreparedStatement stmt = null;
                java.sql.ResultSet rs = null;
                con = ControlPanelPool.getInstance().getConnection();
                for (int i = 0; i < responseData.size(); i++) {
                    stmt = con.prepareStatement("INSERT INTO [dbo].[tblEchoCarrier] ([z4ID] ,[CarrierName] ,[TransitDays] ,[PickupDate] ,[SCAC] ,[OriginZip] ,[DestinationZip] ,[OriginTerminalContact] ,[OriginTerminalAddress] ,[OriginTerminalPhone1] ,[OriginTerminalPhone2] ,[OriginTerminalCsz] ,[DestinationTerminalContact] ,[DestinationTerminalAddress] ,[DestinationTerminalPhone1] ,[DestinationTerminalPhone2] ,[DestinationTerminalCsz] ,[BaseCharge] ,[Discount] ,[DiscountPercent] ,[FuelCharge] ,[AccessorialCharge] ,[TotalCharge] ,[ItemClass] ,[ItemWeight] ,[Leg] ,[LegUid], [timestamp]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE())");
                    stmt.setString(1, request.getParameter("id"));
                    stmt.setString(2, responseData.get(i).getCarrierName());
                    stmt.setString(3, responseData.get(i).getTransitDays());
                    stmt.setString(4, responseData.get(i).getPickupDate());
                    stmt.setString(5, responseData.get(i).getSCAC());
                    stmt.setString(6, servletdata.getOriginZip());
                    stmt.setString(7, servletdata.getWarehouseZip());
                    stmt.setString(8, responseData.get(i).getOriginTerminalContact());
                    stmt.setString(9, responseData.get(i).getOriginTerminalAddress());
                    stmt.setString(10, responseData.get(i).getOriginTermainlPhone1());
                    stmt.setString(11, responseData.get(i).getOrigintermanilPhone2());
                    stmt.setString(12, responseData.get(i).getOriginTerminalCsz());
                    stmt.setString(13, responseData.get(i).getDestinationTerminalContact());
                    stmt.setString(14, responseData.get(i).getDestinationTerminalAddress());
                    stmt.setString(15, responseData.get(i).getDestinationTerminalPhone1());
                    stmt.setString(16, responseData.get(i).getDestinationTerminalPhone2());
                    stmt.setString(17, responseData.get(i).getDestinationTerminalCsz());
                    stmt.setString(18, responseData.get(i).getBaseCharge());
                    stmt.setString(19, responseData.get(i).getDiscount());
                    stmt.setString(20, responseData.get(i).getDiscountPercent());
                    stmt.setString(21, responseData.get(i).getFuelCharge());
                    stmt.setString(22, responseData.get(i).getAccesorialCharge());
                    stmt.setString(23, responseData.get(i).getTotalCharge());
                    stmt.setString(24, productsClass);
                    stmt.setString(25, productsWeight);
                    stmt.setString(26, "1");
                    stmt.setString(27, uuid);
                    stmt.executeUpdate();
                }
                data.setOriginZip(servletdata.getWarehouseZip());
                data.setDestinationZip(servletdata.getDestinationZip());
                data.setNotifyPriorDelivery(true);
                quote = new Quote(data);
                responseData = quote.getResponseData();
                for (int i = 0; i < responseData.size(); i++) {
                    stmt = con.prepareStatement("INSERT INTO [dbo].[tblEchoCarrier] ([z4ID] ,[CarrierName] ,[TransitDays] ,[PickupDate] ,[SCAC] ,[OriginZip] ,[DestinationZip] ,[OriginTerminalContact] ,[OriginTerminalAddress] ,[OriginTerminalPhone1] ,[OriginTerminalPhone2] ,[OriginTerminalCsz] ,[DestinationTerminalContact] ,[DestinationTerminalAddress] ,[DestinationTerminalPhone1] ,[DestinationTerminalPhone2] ,[DestinationTerminalCsz] ,[BaseCharge] ,[Discount] ,[DiscountPercent] ,[FuelCharge] ,[AccessorialCharge] ,[TotalCharge] ,[ItemClass] ,[ItemWeight] ,[Leg] ,[LegUid],[timestamp]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE())");
                    stmt.setString(1, request.getParameter("id"));
                    stmt.setString(2, responseData.get(i).getCarrierName());
                    stmt.setString(3, responseData.get(i).getTransitDays());
                    stmt.setString(4, responseData.get(i).getPickupDate());
                    stmt.setString(5, responseData.get(i).getSCAC());
                    stmt.setString(6, servletdata.getWarehouseZip());
                    stmt.setString(7, servletdata.getDestinationZip());
                    stmt.setString(8, responseData.get(i).getOriginTerminalContact());
                    stmt.setString(9, responseData.get(i).getOriginTerminalAddress());
                    stmt.setString(10, responseData.get(i).getOriginTermainlPhone1());
                    stmt.setString(11, responseData.get(i).getOrigintermanilPhone2());
                    stmt.setString(12, responseData.get(i).getOriginTerminalCsz());
                    stmt.setString(13, responseData.get(i).getDestinationTerminalContact());
                    stmt.setString(14, responseData.get(i).getDestinationTerminalAddress());
                    stmt.setString(15, responseData.get(i).getDestinationTerminalPhone1());
                    stmt.setString(16, responseData.get(i).getDestinationTerminalPhone2());
                    stmt.setString(17, responseData.get(i).getDestinationTerminalCsz());
                    stmt.setString(18, responseData.get(i).getBaseCharge());
                    stmt.setString(19, responseData.get(i).getDiscount());
                    stmt.setString(20, responseData.get(i).getDiscountPercent());
                    stmt.setString(21, responseData.get(i).getFuelCharge());
                    stmt.setString(22, responseData.get(i).getAccesorialCharge());
                    stmt.setString(23, responseData.get(i).getTotalCharge());
                    stmt.setString(24, productsClass);
                    stmt.setString(25, productsWeight);
                    stmt.setString(26, "2");
                    stmt.setString(27, uuid);
                    stmt.executeUpdate();
                }
                data.setOriginZip(servletdata.getOriginZip());
                data.setDestinationZip(servletdata.getDestinationZip());
                data.setNotifyPriorDelivery(false);
                quote = new Quote(data);
                responseData = quote.getResponseData();
                for (int i = 0; i < responseData.size(); i++) {
                    stmt = con.prepareStatement("INSERT INTO [dbo].[tblEchoCarrier] ([z4ID] ,[CarrierName] ,[TransitDays] ,[PickupDate] ,[SCAC] ,[OriginZip] ,[DestinationZip] ,[OriginTerminalContact] ,[OriginTerminalAddress] ,[OriginTerminalPhone1] ,[OriginTerminalPhone2] ,[OriginTerminalCsz] ,[DestinationTerminalContact] ,[DestinationTerminalAddress] ,[DestinationTerminalPhone1] ,[DestinationTerminalPhone2] ,[DestinationTerminalCsz] ,[BaseCharge] ,[Discount] ,[DiscountPercent] ,[FuelCharge] ,[AccessorialCharge] ,[TotalCharge] ,[ItemClass] ,[ItemWeight] ,[Leg] ,[LegUid],[timestamp]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE())");
                    stmt.setString(1, request.getParameter("id"));
                    stmt.setString(2, responseData.get(i).getCarrierName());
                    stmt.setString(3, responseData.get(i).getTransitDays());
                    stmt.setString(4, responseData.get(i).getPickupDate());
                    stmt.setString(5, responseData.get(i).getSCAC());
                    stmt.setString(6, servletdata.getOriginZip());
                    stmt.setString(7, servletdata.getDestinationZip());
                    stmt.setString(8, responseData.get(i).getOriginTerminalContact());
                    stmt.setString(9, responseData.get(i).getOriginTerminalAddress());
                    stmt.setString(10, responseData.get(i).getOriginTermainlPhone1());
                    stmt.setString(11, responseData.get(i).getOrigintermanilPhone2());
                    stmt.setString(12, responseData.get(i).getOriginTerminalCsz());
                    stmt.setString(13, responseData.get(i).getDestinationTerminalContact());
                    stmt.setString(14, responseData.get(i).getDestinationTerminalAddress());
                    stmt.setString(15, responseData.get(i).getDestinationTerminalPhone1());
                    stmt.setString(16, responseData.get(i).getDestinationTerminalPhone2());
                    stmt.setString(17, responseData.get(i).getDestinationTerminalCsz());
                    stmt.setString(18, responseData.get(i).getBaseCharge());
                    stmt.setString(19, responseData.get(i).getDiscount());
                    stmt.setString(20, responseData.get(i).getDiscountPercent());
                    stmt.setString(21, responseData.get(i).getFuelCharge());
                    stmt.setString(22, responseData.get(i).getAccesorialCharge());
                    stmt.setString(23, responseData.get(i).getTotalCharge());
                    stmt.setString(24, productsClass);
                    stmt.setString(25, productsWeight);
                    stmt.setString(26, "0");
                    stmt.setString(27, uuid);
                    stmt.executeUpdate();
                }
                con.close();
            } catch (Exception ex) {
                Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        if (request.getParameter("format") == null && request.getParameter("originzip") != null && request.getParameter("destinationzip") != null && request.getParameter("weight") != null && request.getParameter("haz") != null && request.getParameter("pallets") != null) {
            response.setContentType("text/html;charset=UTF-8");
            String uuid = UUID.randomUUID().toString();
            java.sql.Connection con = null;
            java.sql.PreparedStatement stmt = null;
            try {
                con = ControlPanelPool.getInstance().getConnection();
                stmt = con.prepareStatement("DELETE tblEchoTemp");
                stmt.executeUpdate();
                stmt = con.prepareStatement("INSERT INTO tblEchoTemp (uid) VALUES (?)");
                stmt.setString(1, uuid);
                stmt.executeUpdate();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread t = new Thread((Runnable) new conn(request.getParameter("originzip"), request.getParameter("destinationzip"), request.getParameter("weight"), request.getParameter("haz"), request.getParameter("pallets"), uuid));
            t.start();
        }
        if (request.getParameter("format") != null && request.getParameter("originzip") != null && request.getParameter("destinationzip") != null && request.getParameter("weight") != null && request.getParameter("haz") != null && request.getParameter("pallets") != null) {
            response.setContentType("application/json");
            try {
                QuoteData data = new QuoteData();
                data.setUserId("");
                data.setPassword("");
                data.setPickupDate(DateTime.now().toLocalDate().toString());
                data.setSaveQuote("false");
                data.setReturnMultipleCarriers("true");
                data.setNumberOfPallets(request.getParameter("pallets"));
                Item item = new Item("0", (request.getParameter("haz").equals("1") ? "85" : "65"), request.getParameter("weight"));
                ArrayList<Item> items = new ArrayList<>();
                items.add(item);
                data.setItems(items);
                data.setNotifyPriorDelivery(true);
                ArrayList<ResponseQuoteData> responseData = null;
                data.setOriginZip(request.getParameter("originzip"));
                data.setDestinationZip(request.getParameter("destinationzip"));
                Quote quote = new Quote(data);
                responseData = quote.getResponseData();
                ResponseObject obj = new ResponseObject();
                if (request.getParameter("weight").length() > 4) {
                    obj.StatusCode = 2;
                    obj.Message = "Weight must be 1-9999";
                }
                obj.Resources = responseData;
                response.getWriter().write(new Gson().toJson(obj));
            } catch (Exception ex) {
                Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ResponseObject obj = new ResponseObject();
            obj.StatusCode = 2;
            obj.Message = "Invalid parameters";
            response.getWriter().write(new Gson().toJson(obj));
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

    private String lookupNearestWarehouse(String originZip) {
        String zip = "";
        java.sql.Connection con = null;
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;
        try {
            con = ControlPanelPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM tblWarehouse WHERE Active = 1");
            ConcurrentMap<String, String> warehouseZip = new ConcurrentHashMap<>();
            rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("WHZIP") != null) {
                    warehouseZip.put(rs.getString("WHZIP"), "");
                }
            }
            for (String key : warehouseZip.keySet()) {
                if (key.matches("[0-9]+")) {
                    URL url = new URL("http://10.1.1.58:8080/zip/distance?zip1=" + originZip + "&zip2=" + key);
                    URLConnection conn = url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        warehouseZip.put(key, inputLine.substring(1, inputLine.length() - 1));
                    }
                    br.close();
                }
            }
            double min = 10000.0;
            for (String key : warehouseZip.keySet()) {
                if (!warehouseZip.get(key).isEmpty() && warehouseZip.get(key) != null) {
                    if (Double.valueOf(warehouseZip.get(key)) < min) {
                        min = Double.valueOf(warehouseZip.get(key));
                        zip = key;
                    }
                }
            }
            con.close();
        } catch (IOException | SQLException | PropertyVetoException ex) {
            Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con, stmt, rs);
        }
        return zip;
    }

    private void conn(String oz, String dz, String w, String h, String p, String u) {
        try {
            URL url = new URL("http://10.1.1.58:8080/asynchecho/quote?originzip=" + oz + "&destinationzip=" + dz + "&weight=" + w + "&haz=" + h + "&pallets=" + p + "&uid=" + u);
            URLConnection con = url.openConnection();
            con.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(QuoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
