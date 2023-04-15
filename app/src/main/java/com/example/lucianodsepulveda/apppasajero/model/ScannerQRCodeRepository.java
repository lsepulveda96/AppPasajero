package com.example.lucianodsepulveda.apppasajero.model;

/**
 * Clase responsable de los detalles de implementación de la operación CRUD, metodos get, put, post. sabe como conectarse a una db.
 */
public interface ScannerQRCodeRepository {

     String makeRequestLlegadaColeApi(String idLinea, String idParada);

}
