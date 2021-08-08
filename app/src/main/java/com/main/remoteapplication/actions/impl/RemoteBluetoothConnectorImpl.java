package com.main.remoteapplication.actions.impl;

import android.bluetooth.BluetoothSocket;

import com.main.remoteapplication.actions.RemoteConnector;
import com.main.remoteapplication.models.ServerConnection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RemoteBluetoothConnectorImpl implements RemoteConnector {
    @Override
    public Socket initiateConnection(ServerConnection serverConnection) {
        Socket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName(serverConnection.getRemoteIp());
            socket = new Socket(serverAddress,serverConnection.getPortNumber());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socket;
    }

    @Override
    public void sendStream(BluetoothSocket socket, String data) {
        if(socket != null){
            try {
                OutputStreamWriter outputStream = new OutputStreamWriter(socket.getOutputStream());

                BufferedWriter bufferedWriter = new BufferedWriter(outputStream);
                PrintWriter printWriter = new PrintWriter(bufferedWriter,true);
                printWriter.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
