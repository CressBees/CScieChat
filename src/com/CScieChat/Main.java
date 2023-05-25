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
import com.CScieChat.task.DataBase;
import com.CScieChat.task.ServerIP;


public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static List<String> clientIPs = new ArrayList<>();

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();

        for (String arg : args) {
            System.out.println(arg);
        }

        boolean serverClosed = false;

        // Setup SQL
        dataBase.setup();

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