package com.CScieChat.handler;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class Message {
    public static void handleMessage(String message, List<Client> clients){
        // If the message is a command, send it to the command handler.
        if(message.charAt(0) == '/')
            Command.runCommand(message);
        else {
            //if not a command, send to everyone
            System.out.println("Message: " + message);
            broadcastMessage(message, clients);
        }
        System.out.println("Test - message received");
    }
    //send the received message to everyone
    // TODO: Finish this
    public static void broadcastMessage(String message, List<Client> clients){
        for(
                int i = 0;i<clients.size()-1;i++ //the minus one is a hack fix to prevent an off by one error.
        ){
            try {
                Socket mySocket = new Socket("serverhost", Integer.parseInt(clients.get(i).port)); // Create a new socket
                DataOutputStream ISay = new DataOutputStream(mySocket.getOutputStream()); // Create an output stream
                String msg = message;
                System.out.println("Debug_SendingMessage: " + msg);
                ISay.writeUTF(msg); // write the message
                ISay.flush(); // send the message
                // Close the connection
                ISay.close();
                mySocket.close();
            } catch (Exception e) {
                System.out.println("Debug_BroadcastError "+e); // Oh no an error
            }
        }
    }
}
