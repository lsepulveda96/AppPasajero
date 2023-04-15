package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;

import androidx.appcompat.app.AppCompatActivity;

public class ParadasFavoritasInteractor extends AppCompatActivity implements ParadasFavoritasInterface.Interactor {

    private ParadasFavoritasRepository paradasFavoritasRepository;
    public ParadasFavoritasInteractor(ParadasFavoritasInterface.Presenter presenter, Context mContext) {
        paradasFavoritasRepository = new ParadasFavoritasRepositoryImp(presenter, mContext);
    }


    /**
     * Metodo que consulta la disponibilidad de la red
     * @return informacion de la red
     */
    public NetworkInfo isNetAvailable() {
        return paradasFavoritasRepository.isNetAvailableLocal();
        /* ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //for airplane mode, networkinfo is null
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork;

        */
    }


}
