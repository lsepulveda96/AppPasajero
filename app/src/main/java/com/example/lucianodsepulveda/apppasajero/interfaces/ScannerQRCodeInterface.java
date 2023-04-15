package com.example.lucianodsepulveda.apppasajero.interfaces;

public interface ScannerQRCodeInterface {

    interface View{
        // la vista muestra el dato una vez calculado
//        void showResult(String result);
    }

    interface Presenter{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);
    }

   /* interface Model{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);
    }
*/

    interface Interactor{
        String makeRequestLlegadaCole(String idLineaQr, String idParadaQr);
    }

}
