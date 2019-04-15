package com.lonetiger.onetracker.Presenter;

import android.content.Context;
import android.util.Log;

import com.lonetiger.onetracker.Model.ModelRegisterUser;

import com.lonetiger.onetracker.Model.RegisterInteractor;
import com.lonetiger.onetracker.View.IViewRegisterUser;
import com.lonetiger.onetracker.View.MainActivity;

import java.util.Random;

public class PresenterRegisterUser implements IPresenterRegisterUser,RegisterInteractor {

    IViewRegisterUser iViewRegisterUser;




    public PresenterRegisterUser(IViewRegisterUser iViewRegisterUser){

        this.iViewRegisterUser = iViewRegisterUser;
    }




    @Override
    public void onGetRandomNumber(String num, String user_name, String user_lat, String user_long, Context ct) {

        ModelRegisterUser modelRegisterUser = new ModelRegisterUser();

        modelRegisterUser.registerUserInFirebase(num,user_name,user_lat,user_long,this,ct);

    }

    @Override
    public void onGetSelfUpdate(String num, String user_name, String user_lat, String user_long, Context ct) {


    }


    @Override
    public void onHandleCodeFromModel(int code) {


        //iViewRegisterUser = new MainActivity();

        Log.d("Presenter",String.valueOf(code));
        if(code == 200){

            iViewRegisterUser.onRegisterSuccess("New User Data Added Successfully!");

        }else if(code == 201){


            iViewRegisterUser.onGenerateRandomNumberAgain("User already exists, generating another random id");

        }else if(code == 404){

            iViewRegisterUser.onRegisterError("Can't connect with database, please check your internet connection or restart the app again.");

        }

    }
}
