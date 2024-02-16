package com.example.lucianodsepulveda.apppasajero.interfaces;

import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;

import java.util.List;

public interface ParadasFavoritasInterface {

    interface View{
        // la vista muestra el dato una vez calculado
        void showResult(String result);

        void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList, String paradaActualPasajeroDire);

        void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError);
    }

    interface Presenter{
        //se comunica con la vista, necesita el mismo metodo
        void showResultPresenter(String result);
        //se comunica con el modelo, se ejecuta en el presenter

        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr);

        NetworkInfo isNetAvailable();

        void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList, String paradaActualPasajeroDire);

        void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError);
    }


    interface Interactor {
        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr );

        NetworkInfo isNetAvailable();
    }


}
