package com.CScieChat.handler;

public class Client{
    //client port number
    private String port = null;
    //client ip address
    private String IP = null;

    //Getting client info into string and port
    public void clientSetup(String IPInput)
    {
        //IPInput has both IP and port, seperated by a :, this splits them and then
        String[] IPAndPort = IPInput.split(":",0);
        IP = IPAndPort[0];
        port = IPAndPort[1];
    }
}