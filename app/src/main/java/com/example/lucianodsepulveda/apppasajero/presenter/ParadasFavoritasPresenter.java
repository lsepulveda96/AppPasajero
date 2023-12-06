package com.example.lucianodsepulveda.apppasajero.presenter;

import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;
import com.example.lucianodsepulveda.apppasajero.model.ParadasFavoritasInteractor;

public class ParadasFavoritasPresenter implements ParadasFavoritasInterface.Presenter {
    //esta en contacto con el modelo y con la vista
    //interface
    private ParadasFavoritasInterface.View view;
    //model
    //private ParadasFavoritasInterface.Model model;
    private ParadasFavoritasInterface.Interactor interactor;

    public ParadasFavoritasPresenter(ParadasFavoritasInterface.View view, Context mContext){
        this.view = view;
        //nuevo model
        //model = new ParadasFavoritasModel(this, mContext);

        interactor = new ParadasFavoritasInteractor(this, mContext);
    }



    // TODO 5- recibe el resultado que viene del model, para enviarselo a la vista (activiy)
    @Override
    public void showResultPresenter(String result) {
        //llama al result de la vista
        if(view!=null){
            view.showResult(result);
        }
    }

    @Override
    public NetworkInfo isNetAvailable() {
        return interactor.isNetAvailable();
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
    public void showArriboColectivo(String result) {
        if(view!=null){
            view.showArriboColectivo(result);
        }
    }

}
