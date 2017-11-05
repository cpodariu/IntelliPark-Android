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
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TCPClient {
    private ArrayList<String> message;
    private final String IP = "192.168.1.236";
    private final int PORT = 1234;

    private Context context;

    public TCPClient(ArrayList<String> message) {
        this.message = message;
    }

    public ArrayList<String> run() {
//        test params
        ArrayList<String> defautResult = null;
//        defautResult = new ArrayList<String>();
//        defautResult.add("true");

        Socket backupSocket = null;
        try {
            Socket clientSocket = new Socket(IP, PORT);
            backupSocket = clientSocket;
            ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutput.writeObject(message);
            message = null;
            ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
            int i = 0;
            try {
                Object object = objectInput.readObject();
                message = (ArrayList<String>) object;
                clientSocket.close();
                return message;
            } catch (ClassNotFoundException e) {
                clientSocket.close();
                return defautResult;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defautResult;
    }

    public ArrayList<ArrayList<String>> runForTable() {
        Socket backupSocket = null;
        try {
            Socket clientSocket = new Socket(IP, PORT);
            backupSocket = clientSocket;
            ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutput.writeObject(message);
            message = null;
            ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
            int i = 0;
            ArrayList<ArrayList<String>> notifs = new ArrayList<>();
            int size = objectInput.readInt();
            for (int index = 0; index < size; index++) {
                Object notifobject = objectInput.readObject();
                notifs.add((ArrayList<String>) notifobject);
            }
            return notifs;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}