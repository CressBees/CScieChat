package com.CScieChat.handler;

public class Client{
    //client port number
    private String port = null;
    //client ip address
    private String IP = null;

    //Getting client info into string and port
    public Client(String IPInput)
    {
        String[] IPAndPort = IPInput.split(":",1);
        IP = IPAndPort[1];
        port = IPAndPort[2];
    }
}
