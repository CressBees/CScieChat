package com.CScieChat.handler;

public class Command {
    public static void runCommand(String message) {
        String[] command = message.replaceFirst("/", "").split("\\W+");

        for(int arg=0 ; arg < command.length ; arg++) {
            System.out.println(arg + " - " + command[arg]);
        }
    }
}