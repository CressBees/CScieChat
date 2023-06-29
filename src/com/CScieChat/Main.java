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
import java.util.Objects;

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
        // this is the loop that receives and sends messages.
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

                Message.handleMessage(message, clients); // call handleMessage in the Message class

                //gets and prints client IP & port
                String clientDetails = getClientIP(mainSocket);
                System.out.println(clientDetails);

                // makes a client obj from the connected socket
                createClient(mainSocket);

                //Debug
                System.out.println("Debug_ClientsSizeEquals "+clients.size());
                //prints the list of clients
                printClientList();
                System.out.println("Debug_103824");
                // This checks the handler and sets the serverClosed to true if the server should be closed.
                // equalsIgnoreCase has the same output as .toLowerCase.equals or .toLowercase() == "string"
                if(message.equalsIgnoreCase("!close") || message.equalsIgnoreCase("/close")){
                    serverClosed = true;
                }
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
         String clientIP = socket.getRemoteSocketAddress().toString().replace("/", "");
         //makes a new client obj, and put it into the list if it is not a duplicate.
         Client client = new Client();
         //makes the client have IP and port assigned
         client.clientSetup(clientIP);

         //checks if client is a duplicate and add to list if not add it to the list.
         if(!isDuplicate(client)){
             clients.add(client);
         }

    }
    //Checks if client is a duplicate
    //It goes through the entire array of client objects and if it shares a port no. and IP with any of the members, it doesn't get added.
    private static boolean isDuplicate(Client client){
        for(int i=0;i<=clients.size()-1;i++){
            if(Objects.equals(client.IP, clients.get(i).IP) && Objects.equals(client.port, clients.get(i).port)){
                return true;
            }
        }
        return false;
    }
    //prints clients and details
    //the minus one is there because otherwise an off by one error happens.
    public static void printClientList() {
        for (int i = 0; i <= clients.size() - 1; i++) {

            //say how big clients is
            System.out.println("Debug_clientsSize=" + clients.size());

            //print client info
            System.out.println("client " + i);
            System.out.println("Debug_ClientPort" + clients.get(i).port);
            System.out.println("Debug_ClientPort" + clients.get(i).IP);
            //done
            System.out.println("Debug_ClientsListed");
        }
    }
}