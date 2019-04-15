package com.lonetiger.onetracker.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.MarkerOptions;

import com.lonetiger.onetracker.Model.GetValue;
import com.lonetiger.onetracker.Model.StoreValue;
import com.lonetiger.onetracker.Other.AnimateCamera;
import com.lonetiger.onetracker.Other.Constants;
import com.lonetiger.onetracker.Presenter.IPresenterGetData;
import com.lonetiger.onetracker.Presenter.PreserterGetData;
import com.lonetiger.onetracker.R;

public class TrackOther extends AppCompatActivity implements OnMapReadyCallback,IViewGetData {


    private String TAG = "TrackOther";

    private GoogleMap mMap;
    private String input_code;
    private String input_username, input_user_lat, input_user_long;

    private Handler handler = new Handler();
    private Runnable runner;

    private Marker userOwnMarker;
    private Bitmap userOwnMarkerImage;
    private GoogleMap googleMap;

    private int callingRepeater = 0;
    private int updateUserPosition = 0;

    IPresenterGetData iPresenterGetData;

    private boolean startHandler = false;
    private boolean startAddingMarker = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_track_other);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iPresenterGetData = new PreserterGetData(this);

        askingForCode();




    }

    @Override
    public void onMapReady(GoogleMap googleMapInstance) {

        googleMap = googleMapInstance;

    }

    @Override
    public void onGetDataFromPresenter(GetValue gt) {

        Log.d(TAG,gt.getUser_name()+"-------------"+gt.getUser_lat()+"-------------"+gt.getUser_long());
        if(!startHandler){

            startGettingData();
            startHandler = true;
        }

        if(!startAddingMarker){

            addUserMarker(gt.getUser_name(),gt.getUser_lat(),gt.getUser_long());
            startAddingMarker = true;
        }else {

            updateUserPosition(new LatLng(Double.parseDouble(gt.getUser_lat()), Double.parseDouble(gt.getUser_long())));


        }


    }

    @Override
    public void onWrongCode(int code) {

        if(code == 2){

            askingForCode();
            Toast.makeText(TrackOther.this,"You entered wrong code! Please enter the right code.",Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(TrackOther.this,"Can't connect with database, please check your network connection.",Toast.LENGTH_LONG).show();


        }
    }

    @Override
    public void onError(String msg) {

        Log.d(TAG,msg);

    }



    public void askingForCode() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(TrackOther.this);
        View promptsView = li.inflate(R.layout.custom_edittext, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                TrackOther.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText code = (EditText) promptsView
                .findViewById(R.id.code);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                input_code = code.getText().toString();

                                if(input_code.equals("")){

                                    Toast.makeText(TrackOther.this,"You must enter the code.",Toast.LENGTH_LONG).show();
                                    askingForCode();

                                }else {


                                    //-- start function
                                    iPresenterGetData.startDataFetchingRequest(input_code,TrackOther.this);



                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void startGettingData(){


        runner = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // Repetitive task


                //--------

                iPresenterGetData.startDataFetchingRequest(input_code,TrackOther.this);


                //-- recursing the handler
                handler.postDelayed(this, 5000);
            }


        };

        //-- start for first time

        handler.postDelayed(runner, 5000);
    }


    private void addUserMarker(String user_name, String user_lat, String user_long) {


        LatLng lt = new LatLng(Double.parseDouble(user_lat), Double.parseDouble(user_long));
        //Log.d(lt.);

        userOwnMarkerImage = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);
        userOwnMarker = googleMap.addMarker(new MarkerOptions()
                .position(lt)
                // .anchor(0.5f, 0.5f)
                .title(user_name)
                //.snippet(totalFullAddress.get(i))
                .icon(BitmapDescriptorFactory.fromBitmap(userOwnMarkerImage)));


        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                //Here you can take the snapshot or whatever you want
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(userOwnMarker.getPosition()));
            }

            @Override
            public void onCancel() {

            }
        });


    }

    private void updateUserPosition(LatLng ltln) {

        //userOwnMarker.setPosition(ltln);
        // userOwnMarker.setRotation(direction);
        AnimateCamera an = new AnimateCamera();
        an.animateMarker(googleMap, userOwnMarker, ltln, false);

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(ltln));
        //googleMap.animateCamera( CameraUpdateFactory.zoomTo( 13.0f ) );
    }



    @Override
    public void onBackPressed() {


        handler.removeCallbacks(runner);
        //Intent openNewWindow = new Intent(TrackOhter.this, Splash.class);
        //startActivity(openNewWindow);
        iPresenterGetData.stopDataFetchingRequest();
        finish();

    }


}