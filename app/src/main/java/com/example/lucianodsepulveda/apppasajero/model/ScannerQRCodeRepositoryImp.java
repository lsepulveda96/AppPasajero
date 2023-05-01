package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;

public class ScannerQRCodeRepositoryImp implements ScannerQRCodeRepository {
    ScannerQRCodeInterface.Presenter presenter;
    Context mContext;
    private String responseArriboColectivo = "";
    public static String ipv4 = "http://stcu.mdn.unp.edu.ar:50002/stcu_app";
    RequestQueue requestQueue;

    public ScannerQRCodeRepositoryImp(ScannerQRCodeInterface.Presenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
        requestQueue = Volley.newRequestQueue(mContext);
    }



    public String makeRequestLlegadaColeApi(String idLinea, String idParada){

        String url = ipv4+"/rest/pasajeros/"+idLinea+"/"+idParada;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> responseArriboColectivo = response,
                error -> Log.d( "ERROR",error.toString() )
        );

        requestQueue.add(getRequest);
        return responseArriboColectivo;
    }
}
