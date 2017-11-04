package com.example.cpodariu.intelipark_android.NetworkUtils;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by cpodariu on 04.11.2017.
 */


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TCPClient {
    private ArrayList<String> message;
    private final String IP = "192.168.1.236";
    private final int PORT = 1234;

    private Context context;

    public TCPClient(ArrayList<String> message) {
        this.message = message;
    }

    public ArrayList<String> run(){
//        test params
        ArrayList<String> defautResult;
        defautResult = new ArrayList<String>();
        defautResult.add("true");

        Socket backupSocket = null;
        try{
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket(IP, PORT);
            backupSocket = clientSocket;
            ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutput.writeObject(message);
            message = null;
            ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
            int i = 0;
            try {
                Object object = objectInput.readObject();
                message =  (ArrayList<String>) object;
                clientSocket.close();
                return message;
            } catch (ClassNotFoundException e) {
                clientSocket.close();
                return defautResult;
            }
        }
        catch (IOException e){
            return defautResult;
        }
    }
}