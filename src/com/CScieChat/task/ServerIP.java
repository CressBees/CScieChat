package com.CScieChat.task;

import com.CScieChat.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerIP {
    public static void getIP() {
        boolean privateIpShown = false;
        try {
            // Get local IP
            System.out.println("Local IP: " + InetAddress.getLocalHost().getHostAddress()); // Gets the private IP (the one your device is assigned on the network)
            privateIpShown = true;
            // Get public IP
            URL myIP = new URL("https://checkip.amazonaws.com"); // set a URL variable
            BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream())); // Send a request to the URL
            String globalIP = in.readLine(); // Get the IP as a string
            System.out.println("Public IP: " + globalIP + "\n");
        } catch (UnknownHostException e) {
            //
            if(privateIpShown) {
                Logger logger = Logger.getLogger(Main.class.getName());
                logger.log(Level.WARNING, "Your public IP could not be determined, there are multiple reasons why this could happen.\n1. You are offline\n2. The server we request to is offline\n3. Some of your network settings could be causing issues\n4. Your IP may be blocked by the server we request to.\nEven though your public IP could not be determined the server will still run.\nThis just means you won't be able to connect outside of your network (or device).");
            } else {
                throw new RuntimeException(e); // This prints the error then exits the program, this is what I want, as if the private IP can't be found something is <b>very</b> wrong.
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
