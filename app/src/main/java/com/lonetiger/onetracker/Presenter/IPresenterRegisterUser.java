package com.lonetiger.onetracker.Presenter;

import android.content.Context;

import com.lonetiger.onetracker.View.MainActivity;

public interface IPresenterRegisterUser {

    void onGetRandomNumber(String num, String user_name, String user_lat, String user_long, Context ct);
    void onGetSelfUpdate(String num, String user_name, String user_lat, String user_long,Context ct);


}
