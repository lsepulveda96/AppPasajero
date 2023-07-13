package com.example.lucianodsepulveda.apppasajero.interfaces;

import android.net.NetworkInfo;

public interface ScannerQRCodeInterface {

    interface View{
        // la vista muestra el dato una vez calculado
//        void showResult(String result);
    }

    interface Presenter{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);

        NetworkInfo isNetAvailable();
    }

   /* interface Model{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);
    }
*/

    interface Interactor{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);

        NetworkInfo isNetAvailable();
    }

}
