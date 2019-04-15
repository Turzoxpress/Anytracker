package com.lonetiger.onetracker.Model;

import android.content.Context;

public interface IModelGetData {

    void getDataFromServer(String user_id, GetValueInteractor gt, Context ct);
    void stopDataFromServer(GetValueInteractor gt);
}
