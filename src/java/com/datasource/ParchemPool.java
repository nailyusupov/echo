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
public class ParchemPool {

    private static ParchemPool datasource;
    private BasicDataSource ds;

    private ParchemPool() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUsername("");
        ds.setPassword("");
        ds.setUrl("");
    }

    public static ParchemPool getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new ParchemPool();
            return datasource;
        } else {
            return datasource;
        }
    }

    public java.sql.Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}
