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
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

// Internal
import com.CScieChat.handler.Message;
import com.CScieChat.task.ServerIP;


public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static List<String> clientIPs = new ArrayList<>();

    public static void main(String[] args) {

        for (String arg : args) {
            System.out.println(arg);
        }

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
            LOGGER.log(Level.SEVERE, e.toString());
        }

        // Prints IP
        ServerIP.getIP();

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

                Message.handleMessage(message); // call handleMessage in the Message class

                String clientIP = getClientIP(mainSocket);
                System.out.println(clientIP);

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
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    //adds client IPs to a list
    private static String getClientIP(Socket socket){ // feed the socket into the method
        return socket.getRemoteSocketAddress().toString().replace("/", ""); //get the IP from the socket, for some reason a / appears at the start so remove as well
    }
}