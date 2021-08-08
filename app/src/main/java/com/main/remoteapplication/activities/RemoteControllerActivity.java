package com.main.remoteapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.main.remoteapplication.R;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RemoteControllerActivity extends AppCompatActivity {

    final String TAG = "RemoteControllerActivity";

    List<BluetoothDevice> connectDevicesList;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;
    UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    TextView mousePad;
    private PrintWriter out;
    private boolean isConnected=false;
    private float initX =0;
    private float initY =0;
    private float disX =0;
    private float disY =0;
    private boolean mouseMoved=false;
    public static final String MOUSE_LEFT_CLICK="left_click";
    public static final String MOUSE_SINGLE_CLICK="single_click";
    public static final String MOUSE_DOUBLE_CLICK="double_click";
    ImageButton imageButton;
    boolean isKeyboardShown = false;
    KeyEvent keyEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_controller);

        initLayout();
        dispatchKeyEvent(keyEvent);


        if(getIntent() != null){
            bluetoothDevice = getIntent().getParcelableExtra("bluetoothDevice");
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
                bluetoothSocket.connect();
                isConnected = true;
//create output stream to send data to server

                Executor executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(bluetoothSocket.getOutputStream())), true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
/*                taskRunner.executeAsync(new SendDataTask(), (data)->{

                });*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initLayout() {
        mousePad = (TextView)findViewById(R.id.textview_mouspad);
        imageButton = (ImageButton)findViewById(R.id.imageButton_keyboard);


        mousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isConnected && out!=null){
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            //save X and Y positions when user touches the TextView
                            initX =event.getX();
                            initY =event.getY();
                            mouseMoved=false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            disX = event.getX()- initX; //Mouse movement in x direction
                            disY = event.getY()- initY; //Mouse movement in y direction
                            /*set init to new position so that continuous mouse movement
                            is captured*/
                            initX = event.getX();
                            initY = event.getY();
                            if((disX < -1 && disY<-1)
                                || (disX <-1 && disY >1)
                                || (disX >1 && disY <-1)
                                || (disX >1 && disY >1)
                            ) {
                                //if(disX >1 && disY >1){
                                out.println(disX + "," + disY); //send mouse movement to server
                                mouseMoved = true;
                                //}
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                            //consider a tap only if usr did not move mouse after ACTION_DOWN
                            if(!mouseMoved){
                                out.println(MOUSE_SINGLE_CLICK);
                            }
                    }
                }
                return true;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isKeyboardShown){
                    hideKeyboard();
                }else{
                    showKeyboard();
                }
            }
        });



    }

    void showKeyboard(){
        isKeyboardShown = true;
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(imageButton.getWindowToken(), 0);
        isKeyboardShown = false;

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        String keyValue;
        if(event != null && event.getAction() == KeyEvent.ACTION_UP){
            if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                //out.println();
                keyValue = "ENTER";
            }else if(event.getKeyCode() == KeyEvent.KEYCODE_DEL){
                //out.println("BACK_SPACE");
                keyValue = "BACK_SPACE";
            }
            else if(event.getKeyCode() == KeyEvent.KEYCODE_SPACE){
                keyValue = "SPACE";
            }else {
                //out.println((char) event.getUnicodeChar());
                keyValue = String.valueOf((char) event.getUnicodeChar());
            }
            //sendData(keyValue);
            out.println(keyValue);
            return super.dispatchKeyEvent(event);
        }

        return true;
    }

    /*void sendData(String data){
        if(bluetoothConnector.isEnabledAdapter()){
            out.println(data);
        }else{
            Toast.makeText(getApplicationContext(),"Bluetooth is not enabled, No data is sent!",Toast.LENGTH_SHORT).show();
        }
    }*/
}
