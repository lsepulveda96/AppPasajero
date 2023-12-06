package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;

public class ParadasFavoritasInteractor extends AppCompatActivity implements ParadasFavoritasInterface.Interactor {

    private final ParadasFavoritasRepository paradasFavoritasRepository;
    public ParadasFavoritasInteractor(ParadasFavoritasInterface.Presenter presenter, Context mContext) {
        paradasFavoritasRepository = new ParadasFavoritasRepositoryImp(presenter, mContext);
    }


    /**
     * Metodo que consulta la disponibilidad de la red
     * @return informacion de la red
     */
    public NetworkInfo isNetAvailable() {
        return paradasFavoritasRepository.isNetAvailableLocal();
    }

    @Override
    public String makeRequestLlegadaCole(String idLineaQr, String idRecorridoQr, String idParadaQr) {
        return paradasFavoritasRepository.makeRequestLlegadaColeApi(idLineaQr, idRecorridoQr, idParadaQr);
    }

}
