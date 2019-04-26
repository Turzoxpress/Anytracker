package com.lonetiger.onetracker.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lonetiger.onetracker.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Splash extends AppCompatActivity {


        private static final int PERMISSION_REQUEST_CODE = 200;
        private static int TIME_OUT = 4000; //Time to launch the another activity

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);


            if(checkPermission() == true){



                //-- after task
                goToNext();

            }else{

                //-- Requesting the permission for location access in android devices with Marshmallow or later
                requestPermission();

            }






        }

    //-- Check permission if location is on or off on android devices with Marshmallow or later
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        // int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED; // && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //-- If location permission is off then request the permission on android devices with Marshmallow or later
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    //-- Permission result on android devices with Marshmallow or later
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    // boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){

                        //-- after task
                        goToNext();



                    }
                    else {

                        //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(Splash.this,"Permission Denied, You cannot access location data",Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to location, otherwise you can't use this app's features",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                requestPermission();

                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;

        }
    }

    //-- Set alert dialogue if user denied the location permission
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Splash.this)
                .setIcon(R.drawable.ic_location)
                .setTitle("Needs location permission")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void goToNext(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent openMain = new Intent(Splash.this,Next.class);
                startActivity(openMain);
                finish();

            }
        }, TIME_OUT);



    }
}
