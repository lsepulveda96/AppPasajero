package com.example.lucianodsepulveda.apppasajero.interfaces;

import android.net.NetworkInfo;

public interface ScannerQRCodeInterface {

    interface View{
        // la vista muestra el dato una vez calculado
        void showArriboColectivo(String tiempoArriboColectivo, String latParadaActualColectivo, String lngParadaActualColectivo, String fechaParadaActualString);

        void showUbicacionParadaPasajero(String ubicacionParadaPasajero);
    }

    interface Presenter{
        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr);

        String makeRequestGetUbicacionParadaRecorrido(String idLineaQr, String idRecorridoQr, String idParadaQr);

        NetworkInfo isNetAvailable();

        void showArriboColectivo(String tiempoArriboColectivo, String latParadaActualColectivo, String lngParadaActualColectivo, String fechaParadaActualString);

        void showUbicacionParadaPasajero(String ubicacionParadaPasajero);
    }

   /* interface Model{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);
    }
*/

    interface Interactor{
        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr );

        NetworkInfo isNetAvailable();

        String makeRequestGetUbicacionParadaRecorrido(String idLineaQr, String idRecorridoQr, String idParadaQr);
    }

}
