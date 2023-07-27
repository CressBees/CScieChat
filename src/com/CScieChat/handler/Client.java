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

        //client receiving port number
        //there should be a better way to do this with finals, but it works for now
        int clientPort = 0;

        //Input is message server receives from client, output is one it sends to other clients
        String inputMessage = null;
        String outputMessage = null;

        //says if message is a command or not
        boolean isCommand = false;

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


                //right now I'm just doing commands here, this is a bad way of doing it, but it will work for now
                //if the message starts with a "/", don't send it to the other clients
                if(inputMessage.startsWith("/")){
                    System.out.println("Debug_CommandReceived");
                    System.out.println("Debug_MessageHidden");
                    isHidden = true;
                    isCommand = true;
                } else if (inputMessage.startsWith("!")) {
                    System.out.println("Debug_CommandReceived");
                    isCommand = true;
                }
                //initial msg will have port, get port
                if(inputMessage.startsWith("/initial")){
                    String[] portExtractor = inputMessage.split(":");
                    clientPort = Integer.parseInt(portExtractor[1]);
                    System.out.println("Debug_PortNumber== "+clientPort);
                }

                //send the message to other clients if it is not hidden
                if (!isHidden) {
                    sendMessage(name, inputMessage, readFromListenOn, clients, clientPort);
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
    private String keyboardInput(){
        Scanner messageScanner = new Scanner(System.in); // create scanner
        System.out.println("Input Message");
        return messageScanner.nextLine();
    }

    //send messages to all clients
    //TODO: finish this
    private void sendMessage(String name, String inputMessage, DataInputStream readFromListenOn, Vector clients, int clientPort) throws IOException {
        System.out.println("Debug_SendingMessage");
        for(int i = 0; i < clients.size(); i++){

            DataOutputStream sender =  new DataOutputStream(.getOutputStream());

            /*
            //int port = clients[i].clientPort;
            Socket mySocket = new Socket("outputhost", clientPort ); // Create a new socket with client port
            DataOutputStream ISay = new DataOutputStream(mySocket.getOutputStream()); // Create an output stream

            System.out.println("Sending: " + inputMessage);

            ISay.writeUTF(inputMessage); // write the message
            ISay.flush(); // send the message

            // close the connections
            mySocket.close();
            ISay.close();
            */
        }
    }
}