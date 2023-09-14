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
import java.util.*;

// Internal
import com.CScieChat.handler.Client;
//import com.CScieChat.handler.Message;
import com.CScieChat.task.DataBase;
import com.CScieChat.task.ServerIP;


public class Main {

    //Is vector instead of arraylist because vectors are threadsafe
    public static Vector<Client> clients = new Vector<>();

    //Default name for clients
    static final String defaultName = "Anonymous";

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

        // this is the loop that creates and connects clients
        // Right now it never ends
        try {
            // Create a socket with the selected port
            ServerSocket listenOn = new ServerSocket(26695);
            System.out.println("Server Started...");
            System.out.println("Waiting for Clients...");
            // While the server has not been closed
            while(!serverClosed) {
                // Open the connection
                Socket mainSocket = listenOn.accept();

                //Print connected clients +1 is to prevent an off by one error. Computers start from 0
                // for some reason, if I do this in the print func, it just adds a 1 onto the end of clients.size
                // So we do it here instead
                int clientQuantity = clients.size()+1;
                System.out.println("Client Number " + clientQuantity + " has connected");

                // Get streams (don't cross them)
                DataInputStream readFromListenOn = new DataInputStream(mainSocket.getInputStream());
                DataOutputStream sendFromListenOn = new DataOutputStream(mainSocket.getOutputStream());


                //make a new client, default name is Anonymous
                createClient(defaultName, mainSocket, readFromListenOn, sendFromListenOn, clients);

                //Debug
                System.out.println("Debug_ClientsSizeEquals "+clients.size());
                //prints the list of clients
                printClientList();
                // TODO: Make this loop endable
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

    //Make a new thread with a client
    private static void createClient(String defaultName, Socket clientSocket, DataInputStream readFromListenOn, DataOutputStream sendFromListenOn, Vector<Client> clients) throws IOException {
        System.out.println("Creating new client");

        //Make new client with the socket + Input & Output streams
        Client clientThread = new Client(defaultName, clientSocket, readFromListenOn, sendFromListenOn, clients);

        System.out.println("Debug_AddClientObjToVector");

        //add client to vector
        Main.clients.add(clientThread);

        //make the thread start working
        clientThread.start();
    }

    //prints clients
    // the minus one is there because otherwise an off by one error happens.
    public static void printClientList() {
        for (int i = 0; i <= clients.size() - 1; i++) {
            //say how big clients is
            System.out.println("Debug_clientsSize=" + clients.size());

            //print client info
            System.out.println("client " + i);
        }
    }
}