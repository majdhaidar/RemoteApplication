package com.main.remoteapplication.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.remoteapplication.ClientThread;
import com.main.remoteapplication.R;
import com.main.remoteapplication.actions.BluetoothConnector;
import com.main.remoteapplication.adapters.BluetoothDevicesRecyclerAdapter;
import com.main.remoteapplication.broadcastReceivers.BluetoothBroadCastReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "MainActivity";

    Button connectServer,enableBluetooth;
    BluetoothConnector bluetoothConnector;
    BluetoothDevice bluetoothDevice;
    LinearLayout layoutEnableBluetooth,layoutPairedDevices;
    RecyclerView recyclerViewPairedDevices;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        registerBluetoothChangeReceiver();

        Toast.makeText(this,"Bluetooth OFF! Turn On to use the app",Toast.LENGTH_SHORT).show();
    }

    private void registerBluetoothChangeReceiver() {
        broadcastReceiver = new BluetoothBroadCastReceiver(new BluetoothBroadCastReceiver.BluetoothChangeListener() {
            @Override
            public void stateChanged(int state) {
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(getApplicationContext(),"Bluetooth OFF! Turn On to use the app",Toast.LENGTH_SHORT).show();
                        layoutEnableBluetooth.setVisibility(View.VISIBLE);
                        layoutPairedDevices.setVisibility(View.GONE);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(getApplicationContext(),"Bluetooth ON! Getting Paired Devices",Toast.LENGTH_SHORT).show();
                        layoutEnableBluetooth.setVisibility(View.GONE);
                        layoutPairedDevices.setVisibility(View.VISIBLE);
                        displayPairedDevices();
                        break;
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onResume() {
        bluetoothConnector = new BluetoothConnector();
        if(bluetoothConnector.isEnabledAdapter()){
            layoutEnableBluetooth.setVisibility(View.GONE);
            layoutPairedDevices.setVisibility(View.VISIBLE);
            displayPairedDevices();
        }else{
            layoutPairedDevices.setVisibility(View.GONE);
            layoutEnableBluetooth.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void initLayout() {
        initViewVariables();
        initClickListeners();


    }

    private void initViewVariables() {
        connectServer = (Button)findViewById(R.id.button_connect_server);
        enableBluetooth = (Button)findViewById(R.id.button_enable_bluetooth);
        layoutEnableBluetooth = (LinearLayout)findViewById(R.id.layout_enable_bluetooth);
        layoutPairedDevices= (LinearLayout)findViewById(R.id.layout_paired_devices);
        recyclerViewPairedDevices = (RecyclerView)findViewById(R.id.recyclerview_paired_devices);
    }

    private void initClickListeners() {
        connectServer.setOnClickListener(this);
        enableBluetooth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_enable_bluetooth:
                enableBluetooth();
                break;
            case R.id.button_connect_server:
                connectToServer();
                break;
        }
    }

    private void connectToBluetooth(BluetoothDevice bluetoothDevice) {
        //ParcelUuid[] uuids = bluetoothDevice.getUuids();
        try {

            Intent intent = new Intent(getApplicationContext(),RemoteControllerActivity.class);
            intent.putExtra("bluetoothDevice",bluetoothDevice);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Connected to bluetooth device",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() {
/*        if(bluetoothConnector.isConnected()){
            ServerConnection serverConnection = new ServerBluetoothConnection(Constants.REMOTE_IP,Constants.REMOTE_PORT);
            RemoteConnector remoteConnector = new RemoteConnectorImpl();
            remoteConnector.initiateConnection(serverConnection);
        }*/
    }

/*    private void sendData() {
        RemoteConnector remoteConnector = new RemoteBluetoothConnectorImpl();
        remoteConnector.sendStream(bluetoothSocket,"Hello");
    }*/

    private void enableBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        enableBluetoothResultLauncher.launch(intent);
    }

    private void displayPairedDevices(){
        layoutPairedDevices.setVisibility(View.VISIBLE);
        bluetoothConnector.fillPairedDevices();

        BluetoothDevicesRecyclerAdapter bluetoothDevicesRecyclerAdapter = new BluetoothDevicesRecyclerAdapter(bluetoothConnector.getBluetoothDevices(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerViewPairedDevices.getChildLayoutPosition(view);

                BluetoothDevice bluetoothDevice = bluetoothConnector.getBluetoothDevices().get(itemPosition);
                connectToBluetooth(bluetoothDevice);
            }
        });

        recyclerViewPairedDevices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPairedDevices.setAdapter(bluetoothDevicesRecyclerAdapter);

/*        StringBuffer stringBuffer = new StringBuffer();
        for(BluetoothDevice bluetoothDevice:bluetoothConnector.getBluetoothDevices()){
            stringBuffer.append(bluetoothDevice.getName()+"\n");
        }
        textViewPairedDevices.setText(stringBuffer);*/
    }

    ActivityResultLauncher<Intent> enableBluetoothResultLauncher= registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == Activity.RESULT_OK){
                                layoutEnableBluetooth.setVisibility(View.GONE);
                                displayPairedDevices();
                            }
                        }
                    }
            );



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String name = device.getName();
                if (name != null)
                    Log.d(TAG, "Discovery of Bluetooth Devices:" + name);

                if (name != null && name.equals("PHIL-PC")) {
                    Log.d(TAG, "Discover the target Bluetooth device and start thread connection");
                    new Thread(new ClientThread(device)).start();

                    // Bluetooth search is a process that consumes a lot of system resources. Once the target device is found, the scan can be turned off.
                    bluetoothConnector.getBluetoothAdapter().cancelDiscovery();
                }
            }
        }
    };



}