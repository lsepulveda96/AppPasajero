package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;

import java.util.List;

public class ParadasFavoritasRepositoryImp implements ParadasFavoritasRepository {
    ParadasFavoritasInterface.Presenter presenter;
    Context mContext;

    public static String ipv4 = "http://stcu.mdn.unp.edu.ar:50002/stcu_app";

    private List<ParadaCercana> listaParadas;

    RequestQueue requestQueue;
    public ParadasFavoritasRepositoryImp(ParadasFavoritasInterface.Presenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
        requestQueue = Volley.newRequestQueue(mContext);
    }


    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //for airplane mode, networkinfo is null
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork;
    }


}
