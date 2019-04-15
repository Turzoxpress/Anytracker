package com.lonetiger.onetracker.Presenter;

import android.content.Context;

import com.lonetiger.onetracker.Model.ModelRegisterUser;

public class PresenterUpdateUser implements IPresenterRegisterUser {
    @Override
    public void onGetRandomNumber(String num, String user_name, String user_lat, String user_long, Context ct) {

    }

    @Override
    public void onGetSelfUpdate(String num, String user_name, String user_lat, String user_long, Context ct) {

        ModelRegisterUser modelRegisterUser = new ModelRegisterUser();

        modelRegisterUser.updateSelfData(num,user_name,user_lat,user_long,ct);

    }
}
