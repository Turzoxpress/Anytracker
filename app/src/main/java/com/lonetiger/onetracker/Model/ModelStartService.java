package com.lonetiger.onetracker.Model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lonetiger.onetracker.Other.Constants;
import com.lonetiger.onetracker.Other.Services.ForegroundService;
import com.lonetiger.onetracker.Other.Services.LocationService;
import com.lonetiger.onetracker.View.MainActivity;

public class ModelStartService implements IModelStartServices {

    private String TAG = "ModelStartService";

    @Override
    public void startLocationService(Context ct) {

        if(isServiceRunningInForeground(ct,ForegroundService.class)){

            //  Toast.makeText(MainActivity.this,"Service is already running...",Toast.LENGTH_LONG).show();
            Log.d(TAG,"Service is already running...");

        }else {

            Intent startIntent = new Intent(ct, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            ct.startService(startIntent);


        }

    }

    @Override
    public void stopLocationService(Context ct) {

        Intent stopIntent = new Intent(ct, ForegroundService.class);
        stopIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        ct.stopService(stopIntent);
        ct.stopService(new Intent(ct, LocationService.class));

        Toast.makeText(ct,"Self tracking turned off! No one can track you now",Toast.LENGTH_LONG).show();

    }


    //--




    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        boolean serviceStatus = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                //return service.foreground;
                serviceStatus = true;
            }
        }
        return serviceStatus;
    }



}
