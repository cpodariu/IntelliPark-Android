package com.example.cpodariu.intelipark_android.NetworkUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by cpodariu on 04.11.2017.
 */

public class TCPClient {
    public static final String IP = "192.168.1.236";
    public static final int PORT = 1234;
    private String message;

    public TCPClient(String message) {
        this.message = message;
    }

    public void run(){
        try{
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket(IP, PORT);
            System.out.println("Connected to server!");
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(message);
            clientSocket.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}