package com.example.lucianodsepulveda.apppasajero.presenter;

import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;
import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;
import com.example.lucianodsepulveda.apppasajero.model.ScannerQRCodeInteractor;

import java.util.List;

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

   /* @Override
    public String makeRequestGetUbicacionParadaRecorrido(String idLineaQr, String idRecorridoQr, String idParadaQr) {
        String respuesta = "";

        if(view != null){
            respuesta = interactor.makeRequestGetUbicacionParadaRecorrido(idLineaQr, idRecorridoQr, idParadaQr);
        }

        return respuesta;
    }*/

    @Override
    public NetworkInfo isNetAvailable() {
        return interactor.isNetAvailable();
    }

    @Override
    public void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList) {
        if(view!=null){
            view.showArriboColectivo(fechaParadaActualString, tiempoArriboColProximoString, latParadaActualColectivo, lngParadaActualColectivo, latParadaActualPasajero, lngParadaActualPasajero, paradaActualColeDire, codigoError, paradasPorRecorrerList);
        }
    }

    @Override
    public void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError) {
        if(view!=null){
            view.showMsajeSinColectivos(responseTiempoArriboColectivo, codigoError);
        }
    }



/*    @Override
    public void showUbicacionParadaPasajero(String ubicacionParadaPasajero) {
        if(view!=null){
            view.showUbicacionParadaPasajero(ubicacionParadaPasajero);
        }
    }*/


}
