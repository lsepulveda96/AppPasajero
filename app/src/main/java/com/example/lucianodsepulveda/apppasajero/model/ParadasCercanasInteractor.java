package com.example.lucianodsepulveda.apppasajero.model;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;

import java.util.List;
public class ParadasCercanasInteractor extends AppCompatActivity implements ParadasCercanasInterface.Interactor {

    private final ParadasCercanasRepository paradasCercanasRepository;
    public ParadasCercanasInteractor(ParadasCercanasInterface.Presenter presenter, Context mContext, Activity mActivity) {
        paradasCercanasRepository = new ParadasCercanasRepositoryImp(presenter, mContext, mActivity);
    }

    /**
     * Metodo que obtiene permisos necesarios de localicacion
     */
    public void obtenerPermisos() {
        paradasCercanasRepository.getPermisosLocal();
    }

    /**
     * Metodo que machea la cantidad de cuadras con la cantidad de mts
     * @param seleccionRadio seleccion del usuario
     * @return radio seleccionado
     */
    public String obtenerRadio(String seleccionRadio) {
        return paradasCercanasRepository.getRadioLocal(seleccionRadio);
    }


    /**
     * Metodo que se utiliza para obtener la distancia entre dos ubicaciones geograficas
     */
    @Override
    public Double obtenerDistancia(double lat1, double lng1, double lat2, double lng2) {
        return paradasCercanasRepository.getDistanciaLocal(lat1, lng1, lat2, lng2);
    }

    /**
     * Metodo que obtiene paradas cercanas dentro del radio especificado
     * @param listaParadasExistentes lista de todas las paradas existentes
     * @param eleccionRadioParadas eleccion radio del usuario
     * @param latitudStr latitud en que se encuentra el usuario
     * @param longitudStr longitud en que se encuentra el usuario
     */
    public void getListaParadasCercanasApi(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) {
        paradasCercanasRepository.getParadasCercanasAPI(listaParadasExistentes, eleccionRadioParadas, latitudStr, longitudStr);
    }

    /**
     * Metodo que obtiene las paradas pertenecientes a un recorrido/linea
     * @param seleccionLin linea a la que pertenecen las paradas
     * @return lista paradas cercanas
     */
    public List<ParadaCercana> hacerConsultaParadasRecorridoApi(final String seleccionLin) {
        return paradasCercanasRepository.makeConsultaParadasRecorridoApi(seleccionLin);
    }

    /**
     * Metodo que consulta la disponibilidad de la red
     * @return informacion de la red
     */
    public NetworkInfo isNetAvailable() {
        return paradasCercanasRepository.isNetAvailableLocal();
    }

    /**
     * Metodo que obtiene la ubicacion del pasajero
     */
    public void obtenerUbicacionPasajero() {
        paradasCercanasRepository.getUbicacionPasajeroLocal();
    }

    /**
     * Metodo que obtiene un listado de lineas actualmente en servicio
     */
    public void makeConsultaLineas(){
        paradasCercanasRepository.makeConsultaLineasApi();
    }
}
