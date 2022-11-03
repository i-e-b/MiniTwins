package com.example.mtclient;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView v = new ListView(this);

        /*
        final PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent., null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);

        for(ResolveInfo pkg : apps) {
            v.add(pkg.toString());
            // You can now use pkg.services
        }*/

        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(0);//PackageManager.GET_SERVICES);
        for(PackageInfo pkg : pkgs) {
            if (pkg.applicationInfo == null)v.add(pkg.packageName);
            else v.add(pkg.packageName +": "+pkg.applicationInfo.enabled);
            // You can now use pkg.services
        }

        setContentView(v);
    }
}