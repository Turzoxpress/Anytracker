package com.lonetiger.onetracker.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.lonetiger.onetracker.CustomProgressBar;
import com.lonetiger.onetracker.R;

public class Next extends AppCompatActivity {

    private Button enter;

    //-- ad purpose
    private InterstitialAd mInterstitialAd;
    private boolean showAdd = true;
    private int buttonpressed = 0;

    public CustomProgressBar customProgressBar;
    private boolean showLoadingBar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        enter = (Button)findViewById(R.id.enter);

        //--

        //--ad purpose
        MobileAds.initialize(Next.this, getResources().getString(R.string.application_id));
        mInterstitialAd = new InterstitialAd(Next.this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("547BABAE3AB2FE105C08D5339A13F684")
                .build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {


            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {


                Intent openMain = new Intent(Next.this,MainActivity.class);
                startActivity(openMain);
                finish();

            }
        });


        //-------------

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded() && showAdd) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");


                    Intent openMain = new Intent(Next.this,MainActivity.class);
                    startActivity(openMain);
                    finish();

                }


            }
        });

        //--custom loading bar area

        customProgressBar = new CustomProgressBar(Next.this);
        customProgressBar.show();
        stopLoadingBar();


    }

    private void stopLoadingBar(){

        if(!showLoadingBar){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    customProgressBar.hide();
                    //Toast.makeText(Matched_Activity.this, "No match found!", Toast.LENGTH_LONG).show();

                }
            }, 5000);

            showLoadingBar = true;
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                       finish();


                    }
                }).create().show();
    }
}