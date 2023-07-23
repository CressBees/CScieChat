package com.CScieChat.handler;

import java.io.*;

import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Client extends Thread {

    public Client(String name, final Socket clientSocket, final DataInputStream readFromListenOn, final DataOutputStream sendFromListenOn, Vector clients){
        // if client is active, it is true, when stop command, false
        boolean clientActive = true;

        //is this client an admin
        boolean isAdmin = false;

        //Input is message server receives from client, output is one it sends to other clients
        String inputMessage = null;
        String outputMessage = null;

        // controls whether to send the message to the other clients
        // true = yes, false = no
        boolean isHidden;

        //This loop handles sending and receiving messages from each client
        while(clientActive) {
            isHidden = false;
            try {
                //set input message to be the message that was sent
                inputMessage = readFromListenOn.readUTF();

                //print message
                System.out.println("Debug_MessageReceived");
                System.out.println(inputMessage);

                //if the message starts with a "/", don't send it to the other clients
                if(inputMessage.startsWith("/")){
                    System.out.println("Debug_MessageHidden");
                    isHidden = true;
                }

                //send the message to other clients
                sendMessage(name, inputMessage, readFromListenOn, clients);

            } catch (EOFException eofe) {
                System.out.println("Debug_ClientMessageEOFE");
                //if you don't break here, it will just keep looping
                //TODO: find better way to do this
                break;
            }
            catch (Exception e){
                System.out.println("Debug_ClientMessageError");
                System.out.println(e);
            }
        }
    }
    //takes keyboard input from user and returns it as a string
    private String keyboardInput(){
        Scanner messageScanner = new Scanner(System.in); // create scanner
        System.out.println("Input Message");
        return messageScanner.nextLine();
    }
    private void sendMessage(String name, String inputMessage, DataInputStream readFromListenOn, Vector clients){
        for(int i = 0; i < clients.size()+1; i++){

        }
    }
}