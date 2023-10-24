package com.CScieChat.handler;

import java.io.*;

import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client implements Runnable {

    //name of this client
    String clientName;

    //socket of this client
    public Socket socket = null;

    //what this client is receiving from it's associated client
    DataInputStream clientInputStream;

    //what this client is sending
    DataOutputStream clientOutputStream;

    // if client is active, it is true, when stop command, false
    boolean clientActive;

    //is this client an admin
    boolean isAdmin;

    //client receiving port number
    //there should be a better way to do this with finals, but it works for now
    int clientPort = 43254143;

    //Input is message server receives from client, output is one it sends to other clients
    String inputMessage = null;
    String outputMessage = null;

    //assign variables for run
    public Client(String name, final Socket clientSocket, final DataInputStream readFromListenOn, final DataOutputStream sendFromListenOn) throws IOException {

        clientName = name;

        //Create socket in client
        socket = clientSocket;

        clientInputStream = readFromListenOn;

        clientOutputStream = sendFromListenOn;
    }

    @Override
    public void run() {
        // if client is active, it is true, when stop command, false
        boolean clientActive = true;

        //is this client an admin
        boolean isAdmin = false;

        //client receiving port number
        //there should be a better way to do this with finals, but it works for now
        int clientPort = 0;

        int test = 12;

        //says if message is a command or not
        boolean isCommand;

        // controls whether to send the message to the other clients
        // true = yes, false = no
        boolean isHidden;

        System.out.println("Debug_ClientRunning");

        //This loop handles sending and receiving messages from each client
        while(clientActive) {
            isCommand = false;
            isHidden = false;
            try {
                //set input message to be the message that was sent
                inputMessage = clientInputStream.readUTF();

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
                if(isCommand){
                    commandHandler(inputMessage, clientName, isAdmin);
                }

                //send the message to other clients if it is not hidden
                if (!isHidden) {
                    MessageHandler.sendMessage(clientName, inputMessage);
                }

            } catch (EOFException eofe) {
                System.out.println("Debug_ClientMessageEOFE");
                //if you don't break here, it will just keep looping
                //TODO: find better way to do this
                break;
            }
            //if client unexpectedly disconnected, close
            catch (SocketException se){
                System.out.println("Debug_ClientDisconnectError");
                try {
                    socket.close();
                    clientInputStream.close();
                    clientOutputStream.close();
                }
                catch (Exception e){
                    System.out.println("Debug_MetaException: Abandon Hope");
                }
                //TODO: put a remove this client from vector here
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

    private void commandHandler(String inputMessage, String clientName, boolean isAdmin){
        System.out.println("Debug_CommandRun");
    }
}