package com.main.remoteapplication.actions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BluetoothConnector {
    private BluetoothAdapter bluetoothAdapter;
    private ActivityResultLauncher<Intent> enableBluetoothResultLauncher;
    private ArrayList<BluetoothDevice> bluetoothDevicesSet = new ArrayList<>();
    private boolean isConnected;



    public BluetoothConnector() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(this.bluetoothAdapter.isEnabled()){
            fillPairedDevices();
        }
    }

    public boolean isConnectionAvailable(){
        return false;
    }

    public void setEnableBluetoothResultLauncher(ActivityResultLauncher<Intent> enableBluetoothResultLauncher){
        this.enableBluetoothResultLauncher = enableBluetoothResultLauncher;
    }

    public void fillPairedDevices() {
        if(bluetoothAdapter.isEnabled()){
            bluetoothDevicesSet.clear();
            bluetoothDevicesSet.addAll(bluetoothAdapter.getBondedDevices());
        }
    }

    public ArrayList<BluetoothDevice> getBluetoothDevices(){
        return this.bluetoothDevicesSet;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isEnabledAdapter() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
}
