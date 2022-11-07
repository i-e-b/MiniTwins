package com.example.mtclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class MainActivity extends Activity {
    private ListView v;
    private final String TAG = "MtClient";

    private com.example.mtservice.IMtAidlInterface iMyAid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean ok =bindService();

        v = new ListView(this);
        v.add(ok ? "bind ok" : "bind fail :(");

        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(
                PackageManager.GET_SERVICES +
                        PackageManager.GET_PROVIDERS +
                        PackageManager.GET_ACTIVITIES);
        for(PackageInfo pkg : pkgs) {
            //v.add(pkg.packageName);

            if (pkg.packageName.contains("mtservice")){
                if (pkg.services != null) {
                    for (ServiceInfo info : pkg.services) {
                        v.add("s> " + info.name);
                    }
                }
                if (pkg.activities != null){
                    for (ActivityInfo info : pkg.activities) {
                        v.add("a> " + info.name);
                    }
                }
                if (pkg.providers != null){
                    for (ProviderInfo info : pkg.providers) {
                        v.add("p> " + info.name);
                    }
                }
            }
        }

        v.add((iMyAid==null ? "waiting for conn" : "conn is up")); // the binding is async.

        // We select our data provider by url |------ here -------|
        Uri contentUri = Uri.parse("content://com.example.mtservice/what-path");
        v.add("type says: "+getContentResolver().getType(contentUri)); // 'getType' calls com.example.mtservice.TestContentProvider#getType

        String[] project = {"a","b"};
        String[] selArg = {"u","v"};

        try (Cursor c = getContentResolver().query(contentUri, project, "mySelection", selArg, "order")) {
            if (c == null){
                v.add("Cursor from provider is null");
            } else {
                c.moveToNext();
                v.add("test=" + c.getString(0));
                c.close();
            }
        } catch (Exception ex){
            Log.e(TAG, ex.toString());
            v.add("Error reading cursor: "+ex);
        }

        setContentView(v);
    }


    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            v.add("touch: onServiceConnected");
            iMyAid = com.example.mtservice.IMtAidlInterface.Stub.asInterface(iBinder);
            v.add("AIDL bound: "+(iMyAid==null?"failed":"ok!"));

            try {
                v.add("Service version: " + iMyAid.getServiceVersion());
            } catch (Exception ex){
                v.add("Failed to call service: "+ ex);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            v.add("touch: onServiceDisconnected");
            iMyAid = null;
            v.add("AIDL released");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            v.add("AIDL binding died");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            v.add("AIDL binding was null");
        }
    };


    private boolean bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.mtservice", "com.example.mtservice.RealMtAidlService"));
        return bindService(intent, conn, Context.BIND_EXTERNAL_SERVICE + Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}