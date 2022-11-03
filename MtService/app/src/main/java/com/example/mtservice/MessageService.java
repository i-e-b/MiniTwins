package com.example.mtservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

/** Very simple service that uses interprocess comms */
public class MessageService extends Service {

    /** Command the service to reply */
    static final int MSG_PING = 1;

    static class IncomingHandler extends Handler{
        private final Context appContext;

        IncomingHandler(Context context){
            super(Looper.getMainLooper());
            appContext=context;
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_PING:
                    Toast.makeText(appContext, "Ping!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger _messenger;

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding...", Toast.LENGTH_SHORT).show();
        _messenger = new Messenger(new IncomingHandler(this));
        return _messenger.getBinder();
    }
}