package com.main.remoteapplication.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.remoteapplication.R;

import java.util.ArrayList;

public class BluetoothDevicesRecyclerAdapter extends RecyclerView.Adapter<BluetoothDevicesRecyclerAdapter.ViewHolder> {

    ArrayList<BluetoothDevice> bluetoothDevices;
    View.OnClickListener clickListener= null;
    public BluetoothDevicesRecyclerAdapter(ArrayList<BluetoothDevice> bluetoothDevices, View.OnClickListener clickListener) {
        this.bluetoothDevices = bluetoothDevices;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bluetooth_device,parent,false);
        view.setOnClickListener(clickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(bluetoothDevices.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.textview_device_name);
            //textView.setOnClickListener(clickListener);
        }

        public TextView getTextView(){
            return textView;
        }
    }
}
