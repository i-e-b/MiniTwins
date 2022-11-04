package com.example.mtservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

// https://developer.android.com/guide/components/bound-services#Messenger

/** Very simple service that uses interprocess comms */
public class MessageService extends Service {
    static final String TAG = "MessageService";

    /** Command the service to reply */
    static final int MSG_PING = 1;
    static class IncomingHandler extends Handler{
        private final Context appContext;

        IncomingHandler(Context context){
            super(Looper.getMainLooper());
            Log.w(TAG, "IncomingHandler is started");
            appContext=context;
        }

        @Override
        public void handleMessage(Message msg){
            //noinspection SwitchStatementWithTooFewBranches
            switch (msg.what){
                case MSG_PING:
                    Toast.makeText(appContext, "Ping!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(appContext, "What?", Toast.LENGTH_SHORT).show();
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private final Messenger _messenger;

    public MessageService() {
        _messenger = new Messenger(new IncomingHandler(this));
        Log.w(TAG, "Message service is started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Toast.makeText(getApplicationContext(), "binding...", Toast.LENGTH_SHORT).show();
        Log.w(TAG, "Binding");
        return _messenger.getBinder();
    }
}