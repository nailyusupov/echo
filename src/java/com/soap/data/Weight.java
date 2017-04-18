/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soap.data;

/**
 *
 * @author nail yusupov
 */
public class Weight {

    private double PackSize, QTY;
    private String UM;

    public Weight(double pack, double q, String um) {
        PackSize = pack;
        QTY = q;
        UM = um;
    }

    public double getPackSize() {
        return PackSize;
    }

    public void setPackSize(double packSize) {
        PackSize = packSize;
    }

    public double getQTY() {
        return QTY;
    }

    public void setQTY(double qTY) {
        QTY = qTY;
    }

    public String getUM() {
        return UM;
    }

    public void setUM(String uM) {
        UM = uM;
    }

}
