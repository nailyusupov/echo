/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datasource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author nail yusupov
 */
public class ControlPanelPool {

    private static ControlPanelPool datasource;
    private BasicDataSource ds;

    private ControlPanelPool() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUsername("");
        ds.setPassword("");
        ds.setUrl("");
    }

    public static ControlPanelPool getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new ControlPanelPool();
            return datasource;
        } else {
            return datasource;
        }
    }

    public java.sql.Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}
