package com.main.remoteapplication.actions;

import android.bluetooth.BluetoothSocket;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.main.remoteapplication.models.ServerConnection;

import java.net.Socket;

public interface RemoteConnector {

    Socket initiateConnection(ServerConnection serverConnection);
    void sendStream(BluetoothSocket socket, String data);

}
