package com.PTKaBC;
/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */

import java.io.*; // Data streams
import java.net.*; // Sockets

public class Main {
    public static void main(String[] args) {
        System.out.println("Debug_StartUp");

        //prints IP
        try {
            System.out.println("Ip: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        //makes a listener on a port
        try {
            ServerSocket listenOn = new ServerSocket(26666);
            Socket mainSocket = listenOn.accept();
            DataInputStream readFromListenOn = new DataInputStream(mainSocket.getInputStream());
            String message = readFromListenOn.readUTF();
            System.out.println("Debug_ListenerCreated");
            System.out.println("Message: " + message);
            listenOn.close();
        }
        catch (Exception e) {
            System.out.println("Debug_ListenerCreationError"+e);
        }
    }
}