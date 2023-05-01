package com.example.lucianodsepulveda.apppasajero.model;

import android.net.NetworkInfo;

import java.util.List;

/**
 * Clase responsable de los detalles de implementación de la operación CRUD, metodos get, put, post. sabe como conectarse a una base de datos.
 */
public interface ParadasCercanasRepository {
    void getParadasCercanasAPI(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr);

    Double getDistanciaLocal(double lat1, double lng1, double lat2, double lng2);

    String getRadioLocal(String seleccionRadio);

    List<ParadaCercana> makeConsultaParadasRecorridoApi(String seleccionLin);

    void getPermisosLocal();

    NetworkInfo isNetAvailableLocal();

    void getUbicacionPasajeroLocal();

    void makeConsultaLineasApi();
}
