package com.lonetiger.onetracker.Other;

import com.lonetiger.onetracker.Presenter.PresenterUpdateUser;

public class Constants {

    public static String server_path = "http://anytrackerbd.com/tracker_backend/index.php?p=";
    public static String getDataURL = "getdata_user";
    public static String registrationURL = "signup_user";
    public static String updateURL = "update_user";
    public static String deleteURL = "delete_user";
    public static String privacyURL = "http://anytrackerbd.com/other/anytracker/privacy.html";

    public static String New_User_Path = "users";

    public interface ACTION {
        public static String MAIN_ACTION = "com.truiton.foregroundservice.action.main";
        public static String PREV_ACTION = "com.truiton.foregroundservice.action.prev";
        public static String PLAY_ACTION = "com.truiton.foregroundservice.action.play";
        public static String NEXT_ACTION = "com.truiton.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

}
