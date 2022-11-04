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
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import java.util.List;

public class MainActivity extends Activity {
    private ListView v;
    private final String TAG = "MtClient";

    private com.example.mtservice.IMtAidlInterface iMyAid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = new ListView(this);

        boolean ok =bindService();
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
                        v.add("s> " + info.name + "; " + info.permission);
                        v.add("__ " + info.processName);
                    }
                }
                if (pkg.activities != null){
                    for (ActivityInfo info : pkg.activities) {
                        v.add("a> " + info.name + "; " + info.permission);
                    }
                }
                if (pkg.providers != null){
                    for (ProviderInfo info : pkg.providers) {
                        v.add("p> " + info.name + "; " + info.authority);
                    }
                }
            }
        }

        v.add((iMyAid==null ? "no conn" : "conn is up"));

        Uri contentUri = Uri.parse("content://com.example.mtservice/what-path");
        v.add("type says: "+getContentResolver().getType(contentUri));

        setContentView(v);
    }


    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            v.add("touch: onServiceConnected");
            iMyAid = com.example.mtservice.IMtAidlInterface.Stub.asInterface(iBinder);
            v.add("AIDL bound: "+(iMyAid==null?"failed":"ok!"));
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