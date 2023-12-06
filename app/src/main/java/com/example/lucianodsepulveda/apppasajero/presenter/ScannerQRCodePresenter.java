package com.example.lucianodsepulveda.apppasajero.presenter;

import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;
import com.example.lucianodsepulveda.apppasajero.model.ScannerQRCodeInteractor;

public class ScannerQRCodePresenter implements ScannerQRCodeInterface.Presenter {
    //esta en contacto con el modelo y con la vista
    //interface
    private ScannerQRCodeInterface.View view;
    //model
    //private ScannerQRCodeInterface.Model model;
    private ScannerQRCodeInterface.Interactor interactor;

    public ScannerQRCodePresenter(ScannerQRCodeInterface.View view, Context mContext){
        this.view = view;
        //nuevo model
        interactor = new ScannerQRCodeInteractor(this, mContext);
    }

    @Override
    public String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr) {
        String respuesta = "";

        if(view != null){
            respuesta = interactor.makeRequestLlegadaCole(idLineaQr, idRecorridoQr, idParadaQr);
        }

        return respuesta;
    }

    @Override
    public NetworkInfo isNetAvailable() {
        return interactor.isNetAvailable();
    }

    @Override
    public void showArriboColectivo(String result) {
        if(view!=null){
            view.showArriboColectivo(result);
        }
    }


}
