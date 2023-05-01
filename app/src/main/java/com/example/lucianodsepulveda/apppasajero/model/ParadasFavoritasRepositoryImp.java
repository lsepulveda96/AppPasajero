package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;

public class ParadasFavoritasRepositoryImp implements ParadasFavoritasRepository {
    ParadasFavoritasInterface.Presenter presenter;
    Context mContext;

    RequestQueue requestQueue;
    public ParadasFavoritasRepositoryImp(ParadasFavoritasInterface.Presenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
        requestQueue = Volley.newRequestQueue(mContext);
    }


    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //para modo avion, networkinfo es null
        return connectivityManager.getActiveNetworkInfo();
    }


}
