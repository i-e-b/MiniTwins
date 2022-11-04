package com.example.mtservice;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

// https://developer.android.com/guide/components/bound-services#Messenger

/** Very simple service that uses interprocess comms */
public class MessageService extends Service {
    static final String TAG = "MessageService";

    /** Command the service to reply */
    static final int MSG_PING = 1;
    static class IncomingHandler extends Handler{

        IncomingHandler(Looper looper){
            super(looper);
            Log.w(TAG, "IncomingHandler is started");
        }

        @Override
        public void handleMessage(Message msg){
            //noinspection SwitchStatementWithTooFewBranches
            switch (msg.what){
                case MSG_PING:
                    try {
                        msg.replyTo.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private IncomingHandler serviceHandler;


    public MessageService() {
        //Toast.makeText(this, "Service constructor", Toast.LENGTH_SHORT).show();
        Log.w(TAG, "Message service is started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Toast.makeText(getApplicationContext(), "binding...", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "binding", Toast.LENGTH_SHORT).show();
        Log.w(TAG, "Binding");
        return new Messenger(serviceHandler).getBinder();
    }



    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();
        // Get the HandlerThread's Looper and use it for our Handler
        Looper serviceLooper = thread.getLooper();
        serviceHandler = new IncomingHandler(serviceLooper);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

}