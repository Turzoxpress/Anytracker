package com.lonetiger.onetracker.Other.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.lonetiger.onetracker.Presenter.IPresenterRegisterUser;
import com.lonetiger.onetracker.Presenter.PresenterRegisterUser;
import com.lonetiger.onetracker.Presenter.PresenterUpdateUser;
import com.lonetiger.onetracker.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Turzo on 01-Aug-17.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

  private String TAG = "LocationService";
  private LocationRequest mLocationRequest;
  private GoogleApiClient mGoogleApiClient;
  private static final String LOGSERVICE = "#######";


  IPresenterRegisterUser iPresenterRegisterUser;





  @Override
  public void onCreate() {

    super.onCreate();
    buildGoogleApiClient();
   //-- start something
    iPresenterRegisterUser = new PresenterUpdateUser();

    Log.d(LOGSERVICE, "onCreate");

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    //Log.i(LOGSERVICE, "onStartCommand");
    // Toast.makeText(this, "Updating location started!", Toast.LENGTH_SHORT).show();
    Log.d(TAG,"----------Location service started!-----------------");
    // getUser();








    if (!mGoogleApiClient.isConnected()) {

      mGoogleApiClient.connect();
    }

    return START_NOT_STICKY;

  }


  @Override
  public void onConnected(Bundle bundle) {
    Log.d(LOGSERVICE, "onConnected" + bundle);

    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (l != null) {

      //-- start 1
    }

    startLocationUpdate();


  }

  @Override
  public void onConnectionSuspended(int i) {

    Log.d(TAG,"----------Location service Connection Suspended!-----------------");

  }

  @Override
  public void onLocationChanged(Location location) {

    double lat = location.getLatitude();
    double lng = location.getLongitude();


   //--

    SharedPreferences settings = getSharedPreferences("ID_DB", 0);
    int user_num = settings.getInt("id", 0); //0 is the default value
    String user_name = settings.getString("name", "User"); //0 is the default value
    //---------


   sendLatLong(String.valueOf(user_num),user_name,String.valueOf(lat), String.valueOf(lng));

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    // Toast.makeText(this, "Updating location stopped!", Toast.LENGTH_SHORT).show();
    Log.d("111111Updating...","---------------------- Service Stopped! ---------------------- ");

    if (mGoogleApiClient.isConnected())
      mGoogleApiClient.disconnect();
    stopSelf();

  }


  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // Log.i(LOGSERVICE, "onConnectionFailed ");
    Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();

  }

  private void initLocationRequest() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(60000);
    //mLocationRequest.setFastestInterval(5000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

  }

  private void startLocationUpdate() {
    initLocationRequest();

    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
  }

  private void stopLocationUpdate() {
    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addOnConnectionFailedListener(this)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build();
  }

  public void sendLatLong(String user_num,String user_name, String user_lat, String user_long){


    iPresenterRegisterUser.onGetSelfUpdate(user_num,user_name,user_lat,user_long,getApplicationContext());


  }








}