/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine;

import com.datasource.ControlPanelPool;
import com.soap.data.Item;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author nail yusupov
 */
public class QuoteServletData {

    private String originZip, destinationZip, productClass, whZip, country, productUm;
    private double productWeight;
    private List<Item> items;

    /**
     *
     * @param id - zTblOpp4.ID
     */
    public QuoteServletData(String id) {
        items = new ArrayList<>();
        java.sql.Connection con = null;
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;
        try {
            con = ControlPanelPool.getInstance().getConnection();
            /*
            stmt = con.prepareStatement("SELECT * FROM zTblOpp4 WHERE z3ID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            originZip = "";
            country = "";
            while (rs.next()) {
                originZip = rs.getString("ZIP");
                country = rs.getString("COUNTRY");
            }
            productClass = "";
             */
            System.out.println("tblOppMngr4.z4ID = " + id);
            String z3Id = "";
            String z1Id = "";
            stmt = con.prepareStatement("SELECT * FROM tblOppMngr4 WHERE z4ID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                z3Id = rs.getString("z3ID");
                z1Id = rs.getString("z1ID");
                originZip = rs.getString("SFZIP");
            }
            stmt = con.prepareStatement("SELECT * FROM tblOppMngr1 WHERE z1ID = ?");
            stmt.setString(1, z1Id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                destinationZip = rs.getString("STZIP");
            }
            //https://www.zipcodeapi.com/rest/NGaSQLHFvsGMP1rbcB7RJbb67rX5JAqxgAq6m7LSAsEpt5BFGIxqUIw29u7S4xqk/distance.json/10801/08854/mile
            //get the warehouse and compare the zip codes and find out which one is the closest and use that 
            //for generating the quote that is crossdock
            //while the regular quote should be generated as well
            //the warehouse should also be checked to be able to handle the hazmat material if the matrial is hazmat 
            //when it is a crossdock the notify prior to delivery fee should be added
            //if it is a direct order do not notify prior to delivery
            stmt = con.prepareStatement("SELECT * FROM tblOppMngr3 WHERE z1ID = ?");
            stmt.setString(1, z1Id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("HZMT") != null && !rs.getString("HZMT").isEmpty()) {
                    if (rs.getString("HZMT").startsWith("Haz")) {
                        productClass = "85";
                    }
                    if (rs.getString("HZMT").startsWith("Non")) {
                        productClass = "65";
                    }
                } else {
                    productClass = "65";
                }
                productUm = rs.getString("UNITMEASURE") == null ? "LB" : rs.getString("UNITMEASURE");
                productWeight = (rs.getDouble("QUANT") == 0 ? 1 : rs.getDouble("QUANT")) * (rs.getDouble("MEASURE") == 0 ? 1 : rs.getDouble("MEASURE"));
                Item it = new Item(rs.getString("ID"), productClass, String.valueOf(productWeight));
                items.add(it);
            }
            stmt = con.prepareStatement("SELECT * FROM tblUMMultiplier WHERE UM = ?");
            stmt.setString(1, productUm);
            rs = stmt.executeQuery();
            while (rs.next()) {
                productWeight = productWeight * rs.getFloat("LBmultplier");
            }
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
                        whZip = key;
                    }
                }
            }
            con.close();
        } catch (IOException | SQLException | PropertyVetoException ex) {
            Logger.getLogger(QuoteServletData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con, stmt, rs);
        }
    }

    /**
     * @return the originZip
     */
    public String getOriginZip() {
        return originZip;
    }

    /**
     * @return the destinationZip
     */
    public String getDestinationZip() {
        return destinationZip;
    }

    /**
     * @return the productClass
     */
    //public String getProductClass() {
    //    return productClass;
    //}
    /**
     * @return the productZip
     */
    public String getWarehouseZip() {
        return whZip;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    //public String getProductWeight() {
    //    return String.valueOf(productWeight);
    //}
    public List<Item> getItems() {
        return items;
    }

}
