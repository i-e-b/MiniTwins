package com.example.mtservice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String TAG = "BootBroadcastReceiver";

    public BootBroadcastReceiver(){
        super();
        Log.w(TAG,"BootBroadcastReceiver up");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG,"Received message: "+intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, MessageService.class);
            context.startService(serviceIntent);
        }
    }
}