package com.example.mtservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RealMtAidlService extends Service {
    private final String TAG = "AIDL_svc";

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    private final IBinder iBinder = new IMtAidlInterface.Stub(){

        @Override
        public int basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                              double aDouble, String aString) throws RemoteException {
            Log.i(TAG, "Remote call to basicTypes");
            return 123;
        }

        @Override
        public String getServiceVersion(){
            Log.i(TAG, "Remote call to getServiceVersion");
            return "MtService_1.0.0";
        }
    };


}
