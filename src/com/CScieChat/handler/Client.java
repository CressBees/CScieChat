package com.CScieChat.handler;

import com.CScieChat.Main;

import java.io.*;

import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import static com.CScieChat.handler.MessageHandler.clientMap;
import static com.CScieChat.handler.MessageHandler.clients;

public class Client implements Runnable {

    //how long client names are allowed to be
    final int lengthAllowed = 20;

    //assign variables for run
    //name of this client
    String clientName;

    //socket of this client
    public Socket socket;

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

    //This determines what signifiers are used for marking commands, right now, if you use ! as the marker, it will be publicly displayed, but / will hide
    final String[] commandMarkers = {"!","/"};

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

        //checks if initial message has been gotten, to avoid duplicates
        boolean initialMessageReceived = false;

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
                if(inputMessage.startsWith("!")||inputMessage.startsWith("/")){
                    System.out.println("Debug_CommandReceived");
                    System.out.println("Debug_MessageHidden");
                    isCommand = true;

                    //if command starts with a /, don't broadcast it.
                    if(inputMessage.startsWith("/")){isHidden = true;}

                    //if it's the initial message, change name to name
                    if (inputMessage.startsWith("/initial")&& !initialMessageReceived){
                        initialMessageReceived = true;
                        clientName = inputMessage.replaceFirst("/initial:","");

                        //if the client name is too long, trim it down
                        clientName = clientName.substring(0, Math.min(clientName.length(), lengthAllowed));
                    } else if (inputMessage.startsWith("!exit")||inputMessage.startsWith("/exit")) {
                        clientActive = false;
                    }
                }

                //if it's a command, send it to the command handler
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
                    clientInputStream.close();
                    clientOutputStream.close();
                    socket.close();
                    clients.remove(Thread.currentThread());
                    clientMap.remove(Thread.currentThread());
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
        String command;
        //remove the command signifier
        if(inputMessage.startsWith("!")){command = inputMessage.replaceFirst("!","");}
        else {command = inputMessage.replaceFirst("/","");}
    }
}