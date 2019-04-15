package com.lonetiger.onetracker.View;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.lonetiger.onetracker.Other.Constants;
import com.lonetiger.onetracker.Other.Services.ForegroundService;
import com.lonetiger.onetracker.Other.Services.LocationService;
import com.lonetiger.onetracker.Presenter.IPresenterRegisterUser;
import com.lonetiger.onetracker.Presenter.IPresenterStartService;
import com.lonetiger.onetracker.Presenter.PresenterRegisterUser;
import com.lonetiger.onetracker.Presenter.PresenterStartService;
import com.lonetiger.onetracker.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements IViewRegisterUser,IViewStartService {


    private String TAG = "MainActivityTAG";


    private LinearLayout loading_panel,code_panel;
    private Button track_others;
    private TextView tracking_number,welcome_title;
    private Switch tracking_switch;

    private Button share_social_media,sms_code;



    private int randomNumberTemp = 0;
    private String nameTemp;
    private String numTemp;



    IPresenterRegisterUser iPresenterRegisterUser;
    IPresenterStartService iPresenterStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading_panel = (LinearLayout) findViewById(R.id.loadin_panel);
        code_panel = (LinearLayout) findViewById(R.id.code_panel);

        track_others = (Button)findViewById(R.id.track_others);
        tracking_number = (TextView)findViewById(R.id.tracking_code);
        tracking_switch = (Switch)findViewById(R.id.tracking_switch);
        welcome_title = (TextView)findViewById(R.id.welcome_text);
        share_social_media = (Button)findViewById(R.id.share_social_media);
        sms_code = (Button)findViewById(R.id.share_sms);

        iPresenterRegisterUser = new PresenterRegisterUser(this);



        iPresenterStartService = new PresenterStartService(this);

        //-- initialize
        SharedPreferences settings = getSharedPreferences("ID_DB", 0);
        //int user_num = settings.getInt("id", 0); //0 is the default value
        String user_name = settings.getString("name", ""); //0 is the default value
        nameTemp = user_name;

        if(nameTemp.equalsIgnoreCase("")){


            askingForName();

        }else{

            iPresenterRegisterUser.onGetRandomNumber(randomNumberGenerator(),nameTemp,"","",MainActivity.this);
        }

        //-----------


        //--track switch function
        tracking_switch.setChecked(false);
        tracking_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //starService();
                    iPresenterStartService.startLocationServiceByPresenter(MainActivity.this);

                }
                else {

                    //stopServices();
                    iPresenterStartService.stopLocationServiceByPresenter(MainActivity.this);

                }
            }
        });

        //-------------

        //--track other button function

        track_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent openTrackOther  = new Intent(MainActivity.this,TrackOther.class);
                startActivity(openTrackOther);
            }
        });

        //---------------

        //-- share code in social media

        share_social_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareInSocialMedia();
            }
        });


        //--------

        //-- share with SMS
        sms_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //--
                SharedPreferences settings = getSharedPreferences("ID_DB", 0);
                // int user_num = settings.getInt("id", 0); //0 is the default value
                String user_name = settings.getString("name", "User"); //0 is the default value

                final String temp = "Use the code below to track "+user_name+"\n" +
                        tracking_number.getText().toString()+"\n\n" +
                        "Download AnyTracker if you don't have the app.\n" +
                        "https://bit.ly/2TEDlcd"
                        ;


                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", temp);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });

        //----

    }




    public String randomNumberGenerator() {

        Random rnd = new Random();
        int n = 10000000 + rnd.nextInt(90000000);
        //return n;
        //iRandomView.onRandomNumberGenerate(n);
        randomNumberTemp = n;
        return String.valueOf(n);

    }

    @Override
    public void onRegisterSuccess(String msg) {

        //Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
        Log.d(TAG,msg);

        //showCodingPanel(String.valueOf(randomNumberTemp));

        saveID(randomNumberTemp);
        tracking_switch.setChecked(true);

        loading_panel.setVisibility(View.GONE);
        code_panel.setVisibility(View.VISIBLE);
        tracking_number.setText(String.valueOf(randomNumberTemp));
        welcome_title.setText("Welcome "+nameTemp+"!");


    }

    @Override
    public void onRegisterError(String msg) {

        //Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
        Log.d(TAG,msg);
        tracking_switch.setChecked(false);

    }

    @Override
    public void onGenerateRandomNumberAgain(String msg) {

        Log.d(TAG,msg);
        //iPresenterRegisterUser.onGetRandomNumber(randomNumberGenerator(),"","");
        iPresenterRegisterUser.onGetRandomNumber(randomNumberGenerator(),nameTemp,"","",MainActivity.this);

    }





    private void saveID(int id){

        SharedPreferences settings = getSharedPreferences("ID_DB", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("id",id);
        editor.commit();

        //starService();
        iPresenterStartService.startLocationServiceByPresenter(MainActivity.this);
    }

    @Override
    public void onStartLocationService(String msg) {

        Log.d(TAG,msg);



    }

    @Override
    public void onStopLocationService(String msg) {


        Log.d(TAG,msg);



    }

    public void askingForName() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.name_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toast.makeText(MainActivity.this,"You must enter your name",Toast.LENGTH_LONG).show();
                                    askingForName();

                                }else {



                                    SharedPreferences settings = getSharedPreferences("ID_DB", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("name",nameTemp);
                                    editor.commit();
                                    //-- start function

                                    iPresenterRegisterUser.onGetRandomNumber(randomNumberGenerator(),nameTemp,"","",MainActivity.this);


                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void shareInSocialMedia(){

        SharedPreferences settings = getSharedPreferences("ID_DB", 0);
       // int user_num = settings.getInt("id", 0); //0 is the default value
        String user_name = settings.getString("name", "User"); //0 is the default value

        String temp = "Use the code below to track "+user_name+"\n" +
                tracking_number.getText().toString()+"\n\n" +
                "Download AnyTracker if you don't have the app.\n" +
                "https://bit.ly/2TEDlcd"
                ;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, temp);

        startActivity(Intent.createChooser(share, "Sharing ID & Password"));
    }


    private void sendSMS(String phoneNo) {

        //--
        SharedPreferences settings = getSharedPreferences("ID_DB", 0);
        // int user_num = settings.getInt("id", 0); //0 is the default value
        String user_name = settings.getString("name", "User"); //0 is the default value

        final String temp = "Use the code below to track "+user_name+"\n" +
                tracking_number.getText().toString()+"\n\n" +
                "Download AnyTracker if you don't have the app.\n" +
                "https://bit.ly/2TEDlcd"
                ;


        //----------
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, temp, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void askingForNumbers() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.number_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_number = (EditText) promptsView
                .findViewById(R.id.number_input);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                numTemp = input_number.getText().toString();

                                if(nameTemp.equals("")){

                                    Toast.makeText(MainActivity.this,"You must enter a phone number",Toast.LENGTH_LONG).show();
                                    askingForNumbers();

                                }else {



                                    sendSMS(nameTemp);


                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    //---------------
}
