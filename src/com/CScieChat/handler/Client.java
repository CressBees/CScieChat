package com.CScieChat.handler;

public class Client{
    //client port number
    //it's probably bad to make these public, but I need access to them in main, come up with better solution later.
    public String port = null;
    //client ip address
    public String IP = null;

    //Getting client info into string and port
    public void clientSetup(String IPInput)
    {
        //IPInput has both IP and port, separated by a :, this splits them and then
        String[] IPAndPort = IPInput.split(":",0);
        IP = IPAndPort[0];
        port = IPAndPort[1];
    }
}