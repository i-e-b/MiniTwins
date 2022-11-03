package com.example.mtclient;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView v = new ListView(this);

        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(PackageManager.GET_SERVICES);
        for(PackageInfo pkg : pkgs) {
            v.add(pkg.packageName);
            // You can now use pkg.services
        }

        setContentView(v);
    }
}