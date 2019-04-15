package com.lonetiger.onetracker.Presenter;

import android.content.Context;

import com.lonetiger.onetracker.Model.GetValue;
import com.lonetiger.onetracker.Model.GetValueInteractor;
import com.lonetiger.onetracker.Model.ModelGetData;
import com.lonetiger.onetracker.View.IViewGetData;

public class PreserterGetData implements IPresenterGetData,GetValueInteractor {

    IViewGetData iViewGetData;

    public PreserterGetData(IViewGetData iViewGetData){

        this.iViewGetData = iViewGetData;

    }


    @Override
    public void startDataFetchingRequest(String code, Context ct) {

        ModelGetData md = new ModelGetData();
        md.getDataFromServer(code,this, ct);

    }

    @Override
    public void stopDataFetchingRequest() {

        ModelGetData md = new ModelGetData();
        md.stopDataFromServer(this);

    }


    @Override
    public void onFetchDataSuccessfully(GetValue gt) {

        iViewGetData.onGetDataFromPresenter(gt);

    }

    @Override
    public void onFetchDataFailure(int code) {

        iViewGetData.onWrongCode(code);


    }

    @Override
    public void onStoppedGettingData(String msg) {

        iViewGetData.onError(msg);


    }


}
