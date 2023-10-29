package com.CScieChat.handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable{

    //how many people should the server fit
    static final short capacity = 30;

    //Is vector instead of arraylist because vectors are threadsafe
    public static Vector<Thread> clients = new Vector<>(capacity);

    //will store sockets as keys and clients as values, each socket will have an associated client.
    //max capacity of 30 keys, but shouldn't be a problem, as other parts of code will break before 30 is reached.
    //is concurrent because other is not threadsafe, I don't know why, but it also won't work if it's not a concurrent hashmap instead of regular concurrent, strange.
    public static Map<Thread, Socket> clientMap = new ConcurrentHashMap<>(capacity);

    //store all past messages in a vector, so you can send them to any new clients, amount of stored messages equal to max amount of clients
    private static Vector<String> pastMessages = new Vector<>(capacity);

    public MessageHandler(){

    }

    //send messages to all clients
    public static void sendMessage(String name, String inputMessage){
        System.out.println("Debug_SendingMessage");
        System.out.println("Debug_MessageEquals "+inputMessage);
        System.out.println("DebugMH_clientsSize="+clients.size());

        //set the outgoing message to have the right extra bits
        String outgoingMessage = name+" says: "+inputMessage;

        pastMessages.add(outgoingMessage);

        try{
            for (Thread client: clients) {
                System.out.println("Debug_SendForLoopActive");
                DataOutputStream dos = new DataOutputStream(clientMap.get(client).getOutputStream());
                System.out.println("Debug_WriteMessage");
                dos.writeUTF(outgoingMessage);
                System.out.println();
                System.out.println("Debug_SendingMessage");
                //tired to use close, but doesn't work, so flush it is, never close the dos, this will cause a resource leak, Too Bad!
                dos.flush();
                System.out.println("Debug_FinishedClientSendLoop");
            }
        }
        catch (Exception e){
            System.out.println("Debug_ClientIOException");
        }
        System.out.println("Debug_BroadcastComplete");
    }

    public static void sendPastMessages(Thread client) throws IOException {
        if(!(pastMessages.isEmpty())) {
            System.out.println("Debug_SendingPastMessages");
            //make a dos from the client socket
            DataOutputStream dos = new DataOutputStream(clientMap.get(client).getOutputStream());
            //send each of the past messages to the client
            for (String message : pastMessages) {
                System.out.println("Debug_SendingMessage");
                dos.writeUTF(message);
                dos.flush();
            }
        }
    }

    @Override
    public void run(){
        {
            System.out.println("Debug_MSGHMultithreadedYes");
        }
    }
}
