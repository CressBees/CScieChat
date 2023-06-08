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

// Util
import java.util.List;
import java.util.ArrayList;

// Internal
import com.CScieChat.handler.Client;
import com.CScieChat.handler.Message;
import com.CScieChat.task.DataBase;
import com.CScieChat.task.ServerIP;


public class Main {

    private static List<Client> clients = new ArrayList<>();

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

                //gets and prints client IP & port
                String clientDetails = getClientIP(mainSocket);
                System.out.println(clientDetails);

                // makes a client obj from the connected socket
                createClient(mainSocket);

                //prints the list of clients
                printClientList();

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
        }
    }

    //returns client ip of socket
    private static String getClientIP(Socket socket){ // feed the socket into the method
         return socket.getRemoteSocketAddress().toString().replace("/", "");
    }
    //Method creates a client obj to put into the list
     private static void createClient(Socket socket){
         //gets client IP and removes the weird / that appears in the string, also gets port number
         String clientIP = socket.getRemoteSocketAddress().toString().replace("/", "");
         System.out.println("Debug_ClientInfo "+clientIP);
         //makes a new client obj, and put it into the list
         Client client = new Client();
         System.out.println("Debug_ClientInfo4 "+clientIP);
         //makes the client have IP and port assigned
         client.clientSetup(clientIP);
         System.out.println("Debug_ClientInfo3 "+clientIP);
         clients.add(client);
         System.out.println("Debug_ClientInfo2 "+clientIP);
    }
    //prints clients and details !NOT DONE!
    public static void printClientList(){
        for(int i=0;i<=clients.size();i++){
            System.out.println("client "+i);
            //TODO: Finish this
        }
    }
}