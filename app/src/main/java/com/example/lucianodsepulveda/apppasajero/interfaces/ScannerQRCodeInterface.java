package com.example.lucianodsepulveda.apppasajero.interfaces;

import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;

import java.util.List;

public interface ScannerQRCodeInterface {

    interface View{
        // la vista muestra el dato una vez calculado
        void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList);

        void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError);

//        void showUbicacionParadaPasajero(String ubicacionParadaPasajero);
    }

    interface Presenter{
        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr);

//        String makeRequestGetUbicacionParadaRecorrido(String idLineaQr, String idRecorridoQr, String idParadaQr);

        NetworkInfo isNetAvailable();

        void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList);

        void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError);


//        void showUbicacionParadaPasajero(String ubicacionParadaPasajero);
    }

   /* interface Model{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);
    }
*/

    interface Interactor{
        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr );

        NetworkInfo isNetAvailable();

//        String makeRequestGetUbicacionParadaRecorrido(String idLineaQr, String idRecorridoQr, String idParadaQr);
    }

}
