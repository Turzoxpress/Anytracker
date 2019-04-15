package com.lonetiger.onetracker.Model;

import android.content.Context;

public interface IModelRegisterUser {

    String getNumber();
    String getLat();
    String getLong();
    void registerUserInFirebase(String user_id, String user_name, String user_lat, String user_long, RegisterInteractor rgs, Context ct);
    void updateSelfData(String user_id, String user_name, String user_lat,String user_long, Context ct);
}
