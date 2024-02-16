package com.example.lucianodsepulveda.apppasajero.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;
import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;
import com.example.lucianodsepulveda.apppasajero.model.ParadasFavoritasInteractor;

import java.util.List;

public class ParadasFavoritasPresenter implements ParadasFavoritasInterface.Presenter {
    //esta en contacto con el modelo y con la vista
    //interface
    private ParadasFavoritasInterface.View view;
    //model
    //private ParadasFavoritasInterface.Model model;
    private ParadasFavoritasInterface.Interactor interactor;

    public ParadasFavoritasPresenter(ParadasFavoritasInterface.View view, Context mContext, Activity mActivity){
        this.view = view;
        //nuevo model
        //model = new ParadasFavoritasModel(this, mContext);

        interactor = new ParadasFavoritasInteractor(this, mContext, mActivity);
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
    public void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList, String paradaActualPasajeroDire) {
        if(view!=null){
            view.showArriboColectivo(fechaParadaActualString, tiempoArriboColProximoString, latParadaActualColectivo, lngParadaActualColectivo, latParadaActualPasajero, lngParadaActualPasajero, paradaActualColeDire, codigoError, paradasPorRecorrerList, paradaActualPasajeroDire);
        }
    }

    @Override
    public void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError) {
        if(view!=null){
            view.showMsajeSinColectivos(responseTiempoArriboColectivo, codigoError);
        }
    }

//    @Override
//    public void showArriboColectivo(String result) {
//        if(view!=null){
//            view.showArriboColectivo(result);
//        }
//    }

}
