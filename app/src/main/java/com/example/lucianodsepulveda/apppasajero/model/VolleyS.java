package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Clase que devuelve una unica instancia de la cola a utilizar
 */
public class VolleyS {
    private static VolleyS mVolleyS = null;
    // Este objeto es la cola que usará la aplicación
    private final RequestQueue mRequestQueue;

    private VolleyS(Context context) {
        this.mRequestQueue = Volley.newRequestQueue(context);
    }

    // aplica patron singleton
    public static VolleyS getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new VolleyS(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}