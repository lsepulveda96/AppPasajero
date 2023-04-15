package com.example.lucianodsepulveda.apppasajero.interfaces;

import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;

import java.util.List;

public interface ParadasCercanasInterface {

    interface View{
        // la vista muestra el dato una vez calculado
        void showResult(String result);

        void showUbicacionPasajero(String latitudeStr, String longitudeStr);

        void showLineasDisponibles(List<String> lineasDisponibles);

        void showResponseError(String error);

        void showParadasCecanas(List<ParadaCercana> listaFinalParadasCercanas);
    }
    interface Presenter{
        //se comunica con la vista, necesita el mismo metodo
        void showResultPresenter(String result);


        //se comunica con el modelo, se ejecuta en el presenter
        void obtenerPermisos();

        String obtenerRadio(String seleccionRadio);

        Double obtenerDistancia(double latInicial, double lngInicial, double latFinal, double lngFinal);

        void getListaParadasCercanas(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) throws InterruptedException;

        void showUbicacionPasajero(String latitudeStr, String longitudeStr);

        void obtenerUbicacionPasajero();

        void makeConsultaLineas();

        void showLineasDisponibles(List<String> lineasDisponibles);

        List<ParadaCercana> hacerConsultaParadasRecorrido(String seleccionLinea);

        void showResponseError(String error);

        NetworkInfo isNetAvailable();

        void showParadasCecanas(List<ParadaCercana> listaFinalParadasCercanas);
    }

    //creo que todos estos deberian
//    interface Model{
//        void obtenerPermisos();
//
//        String obtenerRadio(String seleccionRadio);
//
//        Double obtenerDistancia(double latInicial, double lngInicial, double latFinal, double lngFinal);
//
//        List<ParadaCercana> obtenerListaParadasCercanas(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) throws InterruptedException;
//
//        void obtenerUbicacionPasajero();
//
//        void makeConsultaLineas();
//
//        List<ParadaCercana> hacerConsultaParadasRecorrido(String seleccionLinea);
//
//        NetworkInfo isNetAvailable();
//        //mismo metodo para el modelo, se ejecuta en el model
//
//
//    }

    interface Interactor {
        void obtenerPermisos();

        String obtenerRadio(String seleccionRadio);

        Double obtenerDistancia(double latInicial, double lngInicial, double latFinal, double lngFinal);

        void getListaParadasCercanasApi(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) throws InterruptedException;

        void obtenerUbicacionPasajero();

        void makeConsultaLineas();

        List<ParadaCercana> hacerConsultaParadasRecorridoApi(String seleccionLinea);

        NetworkInfo isNetAvailable();
    }

}
