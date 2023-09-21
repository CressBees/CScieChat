package com.CScieChat.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class MessageHandler implements Runnable{
    //Is vector instead of arraylist because vectors are threadsafe
    public static Vector<Thread> clients = new Vector<>();
    public MessageHandler(){

    }

    //send messages to all clients
    //TODO: finish this
    public static void sendMessage(String name, String inputMessage){
        System.out.println("Debug_SendingMessage");
        System.out.println("DebugMH_clientsSize="+clients.size());
        try{
            for (Object client : clients) {
                System.out.println("Debug_SendForLoopActive"+client);
                //DataOutputStream sender = new DataOutputStream(client.socket.getOutputStream()); //this.socket might not be actually connecting to a client? Check this
                System.out.println("Debug_WriteMessage");
                //sender.writeUTF(name + " Says: " + inputMessage);
                System.out.println();
                System.out.println("Debug_SendingMessage");
                //sender.flush();
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
            System.out.println("Debug_MSGHMultithreadYes");
        }
    }
}
