package com.example.mtclient;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import java.util.List;

public class MainActivity extends Activity {
    private ListView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = new ListView(this);

        /*
        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(0);//PackageManager.GET_SERVICES);
        for(PackageInfo pkg : pkgs) {
            if (pkg.applicationInfo == null)v.add(pkg.packageName);
            else v.add(pkg.packageName +": "+pkg.applicationInfo.enabled);
            // You can now use pkg.services
        }*/
        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(PackageManager.GET_SERVICES);
        for(PackageInfo pkg : pkgs) {
            if (pkg.services != null) v.add(pkg.packageName +": "+pkg.services.length);

            if (pkg.packageName.contains("mtservice")){
                if (pkg.services != null) {
                    for (ServiceInfo info : pkg.services) {
                        v.add("s> " + info.name + "; " + info.permission);
                    }
                }
                if (pkg.activities != null){
                    for (ActivityInfo info : pkg.activities) {
                        v.add("a> " + info.name + "; " + info.permission);
                    }
                }
            }
        }
        v.add("...");
        v.add("If this is the only message, check logs");

        try {
            boolean ok = bindMyService("com.example.mtservice",
                   "CONNECT", "MessageService");
            v.add("Binding did not fault");
            v.add("ok="+ok);
            v.add(_serviceConnection.toString());
        } catch (Exception ex){
            v.add("Binding failed");
            v.add(ex.toString());
            Log.e(TAG, ex.toString());
        }

        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            v.add(" -> "+serviceInfo.service.getClassName());
        }


        if (isServiceRunning("MessageService")) {
            //bindMyService("com.example.mtservice","TWINS_INTENT", "MessageService");
            v.add("Service is up!");
        }
        else {
            v.add("Service is not running");
        }
        setContentView(v);
    }

    private final String TAG = "MtClient";
    /** Messenger for communicating with the service. */
    public Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    public boolean bound;

    private final ServiceConnection _serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            Log.w(TAG, "on service connected");
            mService = new Messenger(service);
            bound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            Log.w(TAG, "on service disconnected");
            mService = null;
            bound = false;
        }

    };

    private boolean isServiceRunning(String classFragment) {
        v.add("...services found: ");
        // THIS DOESN'T SEEM TO WORK
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            v.add("rs> "+serviceInfo.service.getClassName());
            if (serviceInfo.service.getClassName().contains(classFragment)) {
                Log.w(TAG, serviceInfo.service.getClassName());
                return true;
            }
        }
        return false;
    }

    private boolean bindMyService(String namespace, String intentName, String className) {
        Intent intent = new Intent(/*"a.b.c.MY_INTENT"*/ /*namespace +"."+*/className);
        //Intent intent = new Intent(Intent.);
        //Intent intent = new Intent(getApplicationContext(), );
        intent.setPackage(namespace/*"a.b.c"*/);
        intent.setClassName(namespace, className);
        return bindService(intent, _serviceConnection, Context.BIND_AUTO_CREATE);
    }
}