package com.example.lucianodsepulveda.apppasajero.interfaces;

import android.net.NetworkInfo;

public interface ParadasFavoritasInterface {

    interface View{
        // la vista muestra el dato una vez calculado
        void showResult(String result);

        void showArriboColectivo(String result);
    }

    interface Presenter{
        //se comunica con la vista, necesita el mismo metodo
        void showResultPresenter(String result);
        //se comunica con el modelo, se ejecuta en el presenter

        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr);

        NetworkInfo isNetAvailable();

        void showArriboColectivo(String result);
    }

    /*interface Model{
        //mismo metodo para el modelo, se ejecuta en el model
//        void alCuadrado(String data);
        public boolean isNetAvailable();
        public boolean isOnlineNet();
    }*/

    interface Interactor {
        String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr );

        NetworkInfo isNetAvailable();
    }


}
