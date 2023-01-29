package com.example.lucianodsepulveda.apppasajero.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;

public class ParadasFavoritasModel implements ParadasFavoritasInterface.Model {

    Context mContext;
    private ParadasFavoritasInterface.Presenter presenter;
    public ParadasFavoritasModel(ParadasFavoritasInterface.Presenter presenter, Context mContext ) {

        this.presenter = presenter;
        this.mContext = mContext;

    }

    //TODO 3 -metodo que contiene la logica, negocio
    @Override
    public boolean isNetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    @Override
    public boolean isOnlineNet() {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
