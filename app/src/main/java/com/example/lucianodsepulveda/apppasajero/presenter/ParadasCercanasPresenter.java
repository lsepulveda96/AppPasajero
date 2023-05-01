package com.example.lucianodsepulveda.apppasajero.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;
import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;
import com.example.lucianodsepulveda.apppasajero.model.ParadasCercanasInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que comunica el modelo con la vista
 */
public class ParadasCercanasPresenter implements ParadasCercanasInterface.Presenter {

    //interface
    private final ParadasCercanasInterface.View view;

    private final ParadasCercanasInterface.Interactor interactor;

    public ParadasCercanasPresenter(ParadasCercanasInterface.View view, Context mContext, Activity mActivity){
        this.view = view;
        //nuevo model
//        model = new ParadasCercanasModel(this, mContext, mActivity);
        interactor = new ParadasCercanasInteractor(this, mContext, mActivity);
    }

    //metodos view

    /**
     * Metodo que recibe resultado del model y lo envia a la vista (activity)
     * @param result resultado proveniente del model, para mostrarlo en la vista
     */
    @Override
    public void showResultPresenter(String result) {
        //llama al result de la vista
        if(view!=null){
            view.showResult(result);
        }
    }

    @Override
    public void showUbicacionPasajero(String latitudeStr, String longitudeStr) {
        if(view!=null){
            view.showUbicacionPasajero(latitudeStr,longitudeStr);
        }
    }

    @Override
    public void showLineasDisponibles(List<String> lineasDisponibles) {
        if(view!=null){
            view.showLineasDisponibles(lineasDisponibles);
        }
    }

    public void showResponseError(String error) {
        if(view!=null){
            view.showResponseError(error);
        }
    }

    @Override
    public void showParadasCecanas(List<ParadaCercana> listaFinalParadasCercanas) {
        if(view!=null){
            view.showParadasCecanas(listaFinalParadasCercanas);
        }
    }

    //metodos model

    @Override
//    public void obtenerPermisos() { model.obtenerPermisos(); }
//
//    @Override
//    public String obtenerRadio(String seleccionRadio) {
//        return model.obtenerRadio(seleccionRadio);
//    }
//
//    @Override
//    public Double obtenerDistancia(double latInicial, double lngInicial, double latFinal, double lngFinal) {
//        Double result = 0.0;
//        if(view != null){
//            result = model.obtenerDistancia(latInicial, lngInicial, latFinal, lngFinal);
//        }
//        return result;
//    }
//
//    @Override
//    public List<ParadaCercana> obtenerListaParadasCercanas(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) throws InterruptedException {
//        List<ParadaCercana> listaParadasCercanas = new ArrayList<>();
//        if(view != null){
//            listaParadasCercanas = model.obtenerListaParadasCercanas(listaParadasExistentes,eleccionRadioParadas,latitudStr,longitudStr);
//        }
//        return listaParadasCercanas;
//    }
//
//    @Override
//    public void obtenerUbicacionPasajero() {
//        if(view != null){
//            model.obtenerUbicacionPasajero();
//        }
//    }
//
//    @Override
//    public void makeConsultaLineas() {
//        if(view != null){
//            model.makeConsultaLineas();
//        }
//    }
//
//    @Override
//    public List<ParadaCercana> hacerConsultaParadasRecorrido(String seleccionLinea) {
//        List<ParadaCercana> paradasCercanas = new ArrayList<>();
//        if(view != null){
//            paradasCercanas = model.hacerConsultaParadasRecorrido(seleccionLinea);
//        }
//        return paradasCercanas;
//    }
//
//    @Override
//    public NetworkInfo isNetAvailable() {
//        return model.isNetAvailable();
//    }

    public void obtenerPermisos() { interactor.obtenerPermisos(); }

    @Override
    public String obtenerRadio(String seleccionRadio) {
        return interactor.obtenerRadio(seleccionRadio);
    }

    @Override
    public Double obtenerDistancia(double latInicial, double lngInicial, double latFinal, double lngFinal) {
        Double result = 0.0;
        if(view != null){
            result = interactor.obtenerDistancia(latInicial, lngInicial, latFinal, lngFinal);
        }
        return result;
    }

    @Override
    public void getListaParadasCercanas(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) throws InterruptedException {
        if(view != null){
            interactor.getListaParadasCercanasApi(listaParadasExistentes,eleccionRadioParadas,latitudStr,longitudStr);
        }
    }

    @Override
    public void obtenerUbicacionPasajero() {
        if(view != null){
            interactor.obtenerUbicacionPasajero();
        }
    }

    @Override
    public void makeConsultaLineas() {
        if(view != null){
            interactor.makeConsultaLineas();
        }
    }

    @Override
    public List<ParadaCercana> hacerConsultaParadasRecorrido(String seleccionLinea) {
        List<ParadaCercana> paradasCercanas = new ArrayList<>();
        if(view != null){
            paradasCercanas = interactor.hacerConsultaParadasRecorridoApi(seleccionLinea);

        }
        return paradasCercanas;
    }

    @Override
    public NetworkInfo isNetAvailable() {
        return interactor.isNetAvailable();
    }

}
