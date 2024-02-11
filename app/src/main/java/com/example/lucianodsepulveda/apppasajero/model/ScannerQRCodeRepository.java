package com.example.lucianodsepulveda.apppasajero.model;

import android.net.NetworkInfo;

/**
 * Clase responsable de los detalles de implementación de la operación CRUD, metodos get, put, post. sabe como conectarse a una db.
 */
public interface ScannerQRCodeRepository {

    NetworkInfo isNetAvailableLocal();

    String makeRequestLlegadaColeApi(String idLineaQr, String idRecorridoQr, String idParadaQr);

//    String makeRequestGetUbicacionParadaRecorrido(String idLineaQr, String idRecorridoQr, String idParadaQr);

}
