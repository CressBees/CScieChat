package com.CScieChat.handler;

public class Message {
    public static void handleMessage(String message){
        // If the message is a command, send it to the command handler
        if(message.charAt(0) == '/')
            Command.runCommand(message);
        else {
            System.out.println("Message: " + message);
            broadcastMessage(message);
        }
        System.out.println("Test - message received");
    }
    public static void broadcastMessage(String message){

    }
}
