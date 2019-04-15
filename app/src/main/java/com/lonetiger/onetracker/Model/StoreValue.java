package com.lonetiger.onetracker.Model;

public class StoreValue {

    String user_id;
    String user_name;
    String user_lat;
    String user_long;
    String code;
    String response;

    public StoreValue() {

    }

    public StoreValue(String user_id,String user_name, String user_lat, String user_long) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_lat = user_lat;
        this.user_long = user_long;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public String getUser_long() {
        return user_long;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
