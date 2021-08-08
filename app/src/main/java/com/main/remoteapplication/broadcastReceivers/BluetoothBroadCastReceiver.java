package com.main.remoteapplication.broadcastReceivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothBroadCastReceiver extends BroadcastReceiver {

    BluetoothChangeListener bluetoothChangeListener;
    public BluetoothBroadCastReceiver(BluetoothChangeListener bluetoothChangeListener) {
        this.bluetoothChangeListener = bluetoothChangeListener;
    }

    public interface BluetoothChangeListener{
        void stateChanged(int state);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
            bluetoothChangeListener.stateChanged(state);
        }
    }
}
