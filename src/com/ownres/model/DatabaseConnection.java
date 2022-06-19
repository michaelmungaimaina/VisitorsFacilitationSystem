package com.ownres.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {


        public static Connection ConnectDatabase() {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Driver loaded");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Visitors_Facilitation_Schema", "root",
                        "m!ch43lMung4!"); //$A3SM1+h70$#@37141538
                System.out.println("Database connected");
                return connection;
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
            return null;
        }

    }