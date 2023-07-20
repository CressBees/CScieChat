package com.CScieChat.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.BufferedReader;
import java.io.BufferedInputStream;

import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    public Client(String name, Socket clientSocket, DataInputStream readFromListenOn, DataOutputStream sendFromListenOn){
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
    private static String keyboardInput(){
        Scanner messageScanner = new Scanner(System.in); // create scanner
        System.out.println("Input Message");
        return messageScanner.nextLine();
    }
}