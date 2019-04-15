package com.lonetiger.onetracker.Presenter;

import android.content.Context;

import com.lonetiger.onetracker.Model.GetValue;
import com.lonetiger.onetracker.Model.GetValueInteractor;

public interface IPresenterGetData {

    void startDataFetchingRequest(String code, Context ct);
    void stopDataFetchingRequest();
}
