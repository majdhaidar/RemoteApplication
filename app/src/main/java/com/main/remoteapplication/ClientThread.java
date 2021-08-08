package com.main.remoteapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;

public class ClientThread extends Thread {

    private BluetoothDevice device;
    public ClientThread(BluetoothDevice device) {
        this.device = device;
    }


    @Override
    public void run() {
        BluetoothSocket socket;
        try{
            socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("2d26618601fb47c28d9f10b8ec891363"));
            socket.connect();


        }catch (Exception e){

        }
        super.run();
    }
}
