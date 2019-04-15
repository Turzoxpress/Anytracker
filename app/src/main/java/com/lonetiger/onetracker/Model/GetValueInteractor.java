package com.lonetiger.onetracker.Model;

public interface GetValueInteractor {

    void onFetchDataSuccessfully(GetValue gt);
    void onFetchDataFailure(int code);
    void onStoppedGettingData(String msg);
}
