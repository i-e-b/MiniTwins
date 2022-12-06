package com.example.mtclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
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
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.mtplug.IPlugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import dalvik.system.PathClassLoader;

public class MainActivity extends Activity {
    private ListView v;
    private WebView view;
    private final String TAG = "MtClient";

    private com.example.mtservice.IMtAidlInterface iMyAid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean ok = bindService();

        view = new WebView(this);
        this.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.loadUrl("content://com.example.mtservice/test-web-file.html"); // try to load from our external content provider directly

        v = new ListView(this);
        v.add(ok ? "bind ok" : "bind fail :(");

        @SuppressLint("QueryPermissionsNeeded") // we are requesting a specific package
        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(
                PackageManager.GET_SERVICES +
                        PackageManager.GET_PROVIDERS +
                        PackageManager.GET_ACTIVITIES);
        for (PackageInfo pkg : pkgs) {
            //v.add(pkg.packageName);

            if (pkg.packageName.contains("mtservice")) {
                if (pkg.services != null) {
                    for (ServiceInfo info : pkg.services) {
                        v.add("s> " + info.name);
                    }
                }
                if (pkg.activities != null) {
                    for (ActivityInfo info : pkg.activities) {
                        v.add("a> " + info.name);
                    }
                }
                if (pkg.providers != null) {
                    for (ProviderInfo info : pkg.providers) {
                        v.add("p> " + info.name);
                    }
                }
            }
        }

        ContentResolver cr = getContentResolver();
        v.add((iMyAid == null ? "waiting for conn" : "conn is up")); // the binding is async.

        // We select our data provider by url |------ here -------|
        Uri contentUri = Uri.parse("content://com.example.mtservice/what-path");
        v.add("type says: " + cr.getType(contentUri)); // 'getType' calls com.example.mtservice.TestContentProvider#getType

        tryRunPlugin(cr);



        // Read from service using a cursor (as if it was a DB)
        String[] project = {"a", "b"};
        String[] selArg = {"u", "v"};
        try (Cursor c = cr.query(contentUri, project, "mySelection", selArg, "order")) {
            if (c == null) {
                v.add("Cursor from provider is null");
            } else {
                c.moveToNext();
                v.add("test=" + c.getString(0));
                c.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            v.add("Error reading cursor: " + ex);
        }

        Uri uri2 = Uri.parse("content://com.example.mtservice/raw-data-please");
        try {
            InputStream inputStream = cr.openInputStream(uri2);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            v.add(result.toString("UTF-8"));
            inputStream.close();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            v.add("Error reading input stream: " + ex);
        }

        //setContentView(v);
    }

    private void tryRunPlugin(ContentResolver cr){
        String className = "e.s.MtPluginTest.TestPlugin";
        String methodToInvoke = "testMethod";
        String apkFile = "content://com.example.mtservice/plugin.apk";
        try {
            // To try:
            //    https://stackoverflow.com/a/40179430/423033
            //    https://stackoverflow.com/a/4022071/423033
            //    https://stackoverflow.com/a/25344068/423033
            //    https://stackoverflow.com/q/35197171/423033
            /*
            PathClassLoader pathClassLoader = new dalvik.system.PathClassLoader(
                    "content://com.example.mtservice/plugin.apk",
                    ClassLoader.getSystemClassLoader());

            // This should be loaded from the APK, possibly by the service -->  e.s.MtPluginTest.TestPlugin::testMethod
            Class<?> handler = Class.forName("e.s.MtPluginTest.TestPlugin", true, pathClassLoader);
*/

            /*
            final File optimizedDexOutputPath = getDir("outdex", 0);

            DexClassLoader dLoader = new DexClassLoader(apkFile, optimizedDexOutputPath.getAbsolutePath(),
                    null, ClassLoader.getSystemClassLoader().getParent());

            Class<?> loadedClass = dLoader.loadClass(className);
            Object obj = (Object)loadedClass.newInstance();
            Method m = loadedClass.getMethod(methodToInvoke);
            m.invoke(obj);
             */

            // Before the secondary dex file can be processed by the DexClassLoader,
            // it has to be first copied from asset resource to a storage location.
            String hotFile = "plugin_hot.apk";
            File targetPath = getDir("dex", Context.MODE_PRIVATE);
            File dexInternalStoragePath = new File(targetPath, hotFile);
            copyFromServerToFile(cr, apkFile, dexInternalStoragePath);

            PathClassLoader loader = new PathClassLoader(dexInternalStoragePath.getAbsolutePath(), getClassLoader());
            Class<?> loadedClass = loader.loadClass(className);
            IPlugin obj = (IPlugin)loadedClass.newInstance();
            obj.RunActivity(this, this::onPluginFinished);
            //Method m = loadedClass.getMethod(methodToInvoke);
            //m.invoke(obj);
            /*
            final File optimizedDexOutputPath = getDir("outdex", 0);
            DexClassLoader dLoader = new DexClassLoader(dexInternalStoragePath.getAbsolutePath(),
                    optimizedDexOutputPath.getAbsolutePath(), targetPath.getAbsolutePath(),
                    ClassLoader.getSystemClassLoader().getParent());

            Class<?> loadedClass = dLoader.loadClass(className);
            Object obj = (Object)loadedClass.newInstance();
            Method m = loadedClass.getMethod(methodToInvoke);
            m.invoke(obj);*/
        } catch (Exception ex){
            Log.e(TAG, "Failed to run plugin: "+ex);
        }
    }

    public void onPluginFinished(){
        Log.i(TAG, "Plugin is ending, has asked us to restore state");
        // Restore our own view
        this.setContentView(view);
        Log.i(TAG, "State restored");
    }

    /** Read a file from the server to our app's internals */
    private void copyFromServerToFile(ContentResolver cr, String sourcePath, File destFile) throws Exception {
        InputStream is = cr.openInputStream(Uri.parse(sourcePath));
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            if (destFile.exists()) { // fresh copy
                if (!destFile.delete()) Log.w(TAG, "File did not delete");
            }
            OutputStream dexWriter = new BufferedOutputStream(new FileOutputStream(destFile));

            byte[] buf = new byte[4096];
            int len;
            while ((len = bis.read(buf, 0, 4096)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.flush();
            dexWriter.close();
        } finally {
            bis.close();
            is.close();
        }
    }

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            v.add("touch: onServiceConnected");
            iMyAid = com.example.mtservice.IMtAidlInterface.Stub.asInterface(iBinder);
            v.add("AIDL bound: " + (iMyAid == null ? "failed" : "ok!"));

            try {
                v.add("Service version: " + iMyAid.getServiceVersion());
            } catch (Exception ex) {
                v.add("Failed to call service: " + ex);
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