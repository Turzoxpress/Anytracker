package com.lonetiger.onetracker.View;

import com.lonetiger.onetracker.Model.GetValue;

public interface IViewGetData {

    void onGetDataFromPresenter(GetValue gt);
    void onWrongCode(int code);
    void onError(String msg);
}
