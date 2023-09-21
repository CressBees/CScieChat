package com.CScieChat.handler;

import java.io.*;

import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.Vector;

public class Client implements Runnable {

    public Socket socket;

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

    public Client(String name, final Socket clientSocket, final DataInputStream readFromListenOn, final DataOutputStream sendFromListenOn, Vector clients) throws IOException {

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

        //Create socket in client
        socket = clientSocket;

        int test = 12;

        //says if message is a command or not
        boolean isCommand = false;

        // controls whether to send the message to the other clients
        // true = yes, false = no
        boolean isHidden;

        System.out.println("Debug_ClientRunning");

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
            //if client unexpectedly disconnected, close
            catch (SocketException se){
                System.out.println("Debug_ClientDisconnectError");
                socket.close();
                readFromListenOn.close();
                sendFromListenOn.close();
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
    private void sendMessage(String name, String inputMessage, DataInputStream readFromListenOn, Vector clients, int clientPort){
        System.out.println("Debug_SendingMessage");
        System.out.println(clients.size());
        try{
            for (Object client : clients) {
                System.out.println("Debug_SendForLoopActive");
                DataOutputStream sender = new DataOutputStream(this.socket.getOutputStream()); //this.socket might not be actually connecting to a client? Check this
                System.out.println("Debug_WriteMessage");
                sender.writeUTF(name + " Says: " + inputMessage);
                System.out.println();
                System.out.println("Debug_SendingMessage");
                sender.flush();
                System.out.println("Debug_FinishedClientSendLoop");
            }
        }
        catch (IOException e){
            System.out.println("Debug_ClientIOException");
        }
        System.out.println("Debug_BroadcastComplete");
    }

    @Override
    public void run() {
        try{
            System.out.println("Test");
            testMethod();
        }
        catch(Exception exception){
            System.out.println("Debug_RunnableCatch");
        }
    }
    public void testMethod(){
        System.out.println("Debug_TestMethodRun");
    }
}