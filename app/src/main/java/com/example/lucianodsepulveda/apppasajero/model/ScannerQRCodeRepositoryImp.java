package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
//    public static String ipv4 = "http://stcu.mdn.unp.edu.ar:50002/stcu_app";
    public static String ipv4 = "http://192.168.0.108:50000/v1/mobile/";
    RequestQueue requestQueue;

    public ScannerQRCodeRepositoryImp(ScannerQRCodeInterface.Presenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
        requestQueue = Volley.newRequestQueue(mContext);
    }


// metodo antiguo
/*    public String makeRequestLlegadaColeApi(String idLinea, String idParada){
// hay q trabajar en este metodo. en conjunto con el de la app colectivo
        String url = ipv4+"/rest/pasajeros/"+idLinea+"/"+idParada;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> responseArriboColectivo = response,
                error -> Log.d( "ERROR",error.toString() )
        );

        requestQueue.add(getRequest);
        return responseArriboColectivo;
    }*/

    public String makeRequestLlegadaColeApi(String idLineaString,  String idRecorridoString, String idParadaString){
        String url = ipv4+"obtenerTiempoLlegadaCole/"+ idLineaString +"/"+ idRecorridoString +"/"+ idParadaString;

        System.out.println("informacion: datos que envia qr. idLineaQr" + idLineaString);
        System.out.println("informacion: datos que envia qr. idRecorridoQr" + idRecorridoString);
        System.out.println("informacion: datos que envia qr. idParadaQr" + idParadaString);

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> responseArriboColectivo = response,
                error -> Log.d( "ERROR",error.toString() )
        );

        requestQueue.add(getRequest);
        return responseArriboColectivo;
    }

    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //para modo avion, networkinfo es null
        return connectivityManager.getActiveNetworkInfo();
    }
}
