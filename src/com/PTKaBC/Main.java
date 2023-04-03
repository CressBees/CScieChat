package com.PTKaBC;
/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */

import java.io.*; // Data streams
import java.net.*; // Sockets

// SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {
    public static void main(String[] args) {

        try {
            /*
             * Create a connection to a local database file using SQLite
             * If the file does not already exist the DB file will already be created
             */
            Connection connection = DriverManager.getConnection("jdbc:sqlite:CScieChat.db");
            Statement statement = connection.createStatement(); // This creates a statement allowing us to execute SQL in the database
            /*
             * Create two sql functions to create two tables. This is mostly self-explanatory, but I will explain the SQL written here
             * CREATE TABLE IF NOT EXISTS (table name) checks to see if a table exists in a database then creates one if it does not exist
             * Everything in the brackets is the data that's stored in each row, each one being a "column"
             * INTEGER and STRING are data types, similar to the same ones used in Java already
             * PRIMARY KEY refers to the primary key of a table, this can be used as a foreign key in other tables to refer to that table.
             */
            String sql = "CREATE TABLE IF NOT EXISTS messages (unixTime INTEGER PRIMARY KEY, message STRING)";
            String sql2 = "CREATE TABLE IF NOT EXISTS users (username STRING, pass STRING)";
            // Execute the SQL and close the file
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Prints IP
        try {
            // Get local IP
            System.out.println("Local IP: " + InetAddress.getLocalHost().getHostAddress());

            // Get public IP
            URL myIP = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream()));
            String globalIP = in.readLine(); //you get the IP as a String
            System.out.println("Public IP: " + globalIP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Make a listener on a port
        try {
            ServerSocket listenOn = new ServerSocket(26666);
            Socket mainSocket = listenOn.accept();
            DataInputStream readFromListenOn = new DataInputStream(mainSocket.getInputStream());
            String message = readFromListenOn.readUTF();
            System.out.println("Message: " + message);
            listenOn.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}