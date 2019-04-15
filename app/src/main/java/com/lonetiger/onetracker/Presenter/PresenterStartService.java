package com.lonetiger.onetracker.Presenter;

import android.content.Context;

import com.lonetiger.onetracker.Model.ModelStartService;
import com.lonetiger.onetracker.View.IViewStartService;

public class PresenterStartService implements IPresenterStartService {

    IViewStartService iViewStartService;

    public PresenterStartService(IViewStartService iViewStartService){

        this.iViewStartService = iViewStartService;

    }


    @Override
    public void startLocationServiceByPresenter(Context ct) {


        ModelStartService ms = new ModelStartService();
        ms.startLocationService(ct);

        iViewStartService.onStartLocationService("Service has been started");

    }

    @Override
    public void stopLocationServiceByPresenter(Context ct) {

        ModelStartService ms = new ModelStartService();
        ms.stopLocationService(ct);

        iViewStartService.onStartLocationService("Service has been stopped");

    }
}
