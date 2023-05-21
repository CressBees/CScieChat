package com.CScieChat;
/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */

// Java I/O and networking imports
import java.io.*; // Data streams
import java.net.*; // Sockets

// SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Util
import java.util.List;
import java.util.ArrayList;

// Internal
//there used to be an import here, maybe this has broken something?????

public class Main {
    private static List<String> clientIPs = new ArrayList<>();
    public static void main(String[] args) {
        boolean serverClosed = false;
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
            String sql = "CREATE TABLE IF NOT EXISTS messages (unixTime INTEGER PRIMARY KEY, handler STRING)";
            String sql2 = "CREATE TABLE IF NOT EXISTS users (username STRING, pass STRING)";
            // Execute the SQL and close the file
            // (would close the connection if it was connected to MySQL or something similar, however SQLite is file based)
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        boolean privateIpShown = false;
        // Prints IP
        try {
            // Get local IP
            System.out.println("Local IP: " + InetAddress.getLocalHost().getHostAddress()); // Gets the private IP (the one your device is assigned on the network)
            privateIpShown = true;
            // Get public IP
            URL myIP = new URL("https://checkip.amazonaws.com"); // set a URL variable
            BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream())); // Send a request to the URL
            String globalIP = in.readLine(); // Get the IP as a string
            System.out.println("Public IP: " + globalIP + "\n");
        } catch (UnknownHostException e) {
            //
            if(privateIpShown) {
                System.out.println("Your public IP could not be determined, there are multiple reasons why this could happen.\n1. You are offline\n2. The server we request to is offline\n3. Some of your network settings could be causing issues\n4. Your IP may be blocked by the server we request to.\nEven though your public IP could not be determined the ser will still run.\nThis just means you won't be able to connect outside of your network.");
            } else {
                throw new RuntimeException(e); // This prints the error then exits the program, this is what I want, as if the private IP can't be found something is very wrong
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Make a listener on a port
        // this is the loop that receives and sends messages
        try {
            // Create a socket with the selected port
            ServerSocket listenOn = new ServerSocket(26695);
            // While the server has not been closed
            while(!serverClosed) {
                // Open the connection(?)
                Socket mainSocket = listenOn.accept();
                // Listen for a handler(?)
                DataInputStream readFromListenOn = new DataInputStream(mainSocket.getInputStream());
                // Store the handler and print it out
                String message = readFromListenOn.readUTF();
                System.out.println("Message: " + message);


                //TODO: broadcasts handler to all clients
                //If it successfully sends the message, will return true, else, false
                if(com.CScieChat.handler.message.broadcastMessage(message) == true) {
                    System.out.println("Debug_MessageSent");
                } else{
                    //Message was not sent
                    System.out.println("Debug_MessageFailed");
                }
                // This checks the handler and sets the serverClosed to true if thr server should be closed.
                // equalsIgnoreCase has the same output as .toLowerCase.equals or .toLowercase() == "string"
                serverClosed = message.equalsIgnoreCase("!close") || message.equalsIgnoreCase("/close");
            }
            System.out.println("closing server");
            // Close the server
            listenOn.close();
        }
        //something has broken
        catch (Exception e) {
            System.out.println("Error: Something has gone wrong");
            System.out.println(e);
        }
    }
    private static void addClientIPToList(){

    }
}