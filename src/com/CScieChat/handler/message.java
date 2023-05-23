package com.CScieChat.handler;

public class Message {
    public static void handleMessage(String message){
        // If the message is a command, send it to the command handler
        if(message.charAt(0) == '/')
            Command.runCommand(message);
        else {
            System.out.println("Message: " + message);

            // TODO: broadcast message to all clients

        }
        System.out.println("Test - message recieved");
    }
}
