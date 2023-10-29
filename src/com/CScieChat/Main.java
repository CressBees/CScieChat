package com.CScieChat;/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */

// Java I/O and networking imports
import java.io.*; // Data streams
import java.net.*; // Sockets

// SQL

// Util

// Internal
import com.CScieChat.handler.Client;
//import com.CScieChat.handler.Message;
import com.CScieChat.handler.MessageHandler;
import com.CScieChat.task.DataBase;
import com.CScieChat.task.ServerIP;

import static com.CScieChat.handler.MessageHandler.*;


public class Main {

    //Is vector instead of arraylist because vectors are threadsafe
    //public static Vector<Thread> clients = new Vector<>();

    //Default name for clients
    static final String defaultName = "Anonymous";

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();

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

            //make a message handler thread, sends messages to clients
            createMessageHandler();

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

                if(clients.size()< clients.capacity()) {
                    //make a new client, default name is Anonymous
                    createClient(mainSocket, readFromListenOn, sendFromListenOn);
                }

                //Debug
                System.out.println("DebugMain_ClientsSizeEquals "+clients.size());
                //prints the list of clients
                printClientList();
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

    private static void createMessageHandler(){
        System.out.println("Creating Message Handler");
        Thread messageHandler = new Thread(new MessageHandler());
    }

    //Make a new thread with a client
    private static void createClient(Socket clientSocket, DataInputStream readFromListenOn, DataOutputStream sendFromListenOn) throws IOException {
        System.out.println("Creating new client");

        //Make new client with the socket + Input & Output streams
        Thread clientThread = new Thread(new Client(Main.defaultName, clientSocket, readFromListenOn, sendFromListenOn));

        System.out.println("Debug_AddClientObjToVector");

        //add client to vector
        clients.add(clientThread);

        //Map the created client with the socket the client was created with
        //I will now complain about threads. For some reason, if you create an object
        //and then make it run in a thread, you can't access the local variables in that object
        //which is why I have to do this vector + map nonsense, instead of just a simple method call.
        //Figuring all this out took months of project time, I am somewhat annoyed.
        clientMap.put(clientThread, clientSocket);

        //make the thread start working
        clientThread.start();
    }

    //prints clients
    // the minus one is there because otherwise an off by one error happens.
    public static void printClientList() {
        for (int i = 0; i <= clients.size() - 1; i++) {
            //say how big clients is
            System.out.println("DebugPCL_clientsSize=" + clients.size());

            //print client info
            System.out.println("client " + i);
        }
    }
}