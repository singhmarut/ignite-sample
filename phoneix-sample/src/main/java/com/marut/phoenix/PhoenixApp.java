package com.marut.phoenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by marutsingh on 12/29/16.
 */
public class PhoenixApp {

    public static void main(String[] args){
        try {
            Connection conn = DriverManager.getConnection("jdbc:phoenix:localhost:3333", null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
