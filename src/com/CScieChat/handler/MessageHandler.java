package com.CScieChat.handler;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable{
    //Is vector instead of arraylist because vectors are threadsafe
    public static Vector<Thread> clients = new Vector<>();

    //will store sockets as keys and clients as values, each socket will have an associated client.
    //max capacity of 30 keys, but shouldn't be a problem, as other parts of code will break before 30 is reached.
    //is concurrent because other is not threadsafe, I don't know why, but it also won't work if it's not a concurrent hashmap instead of regular concurrent, strange.
    public static Map<Thread, Socket> clientMap = new ConcurrentHashMap<>(30);

    public MessageHandler(){

    }

    //send messages to all clients
    //TODO: finish this
    public static void sendMessage(String name, String inputMessage){
        System.out.println("Debug_SendingMessage");
        System.out.println("DebugMH_clientsSize="+clients.size());
        try{
            for (Thread client: clients) {
                System.out.println("Debug_SendForLoopActive");
                //this.socket might not be actually connecting to a client? Check this
                DataOutputStream sender = new DataOutputStream(clientMap.get(client).getOutputStream());
                System.out.println("Debug_WriteMessage");
                sender.writeUTF(name + " Says: " + inputMessage);
                System.out.println();
                System.out.println("Debug_SendingMessage");
                sender.flush();
                System.out.println("Debug_FinishedClientSendLoop");
            }
        }
        catch (Exception e){
            System.out.println("Debug_ClientIOException");
        }
        System.out.println("Debug_BroadcastComplete");
    }

    @Override
    public void run(){
        {
            System.out.println("Debug_MSGHMultithreadedYes");
        }
    }
}
