package com.example.lucianodsepulveda.apppasajero.model;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class ParadasFavoritasRepositoryImp implements ParadasFavoritasRepository {

    // ip local actual
    public static String ipv4 = "http://192.168.0.104:50004/stcu2service/v1/mobile/";

    // ip remoto actual
//    public static String ipv4 =  "http://138.36.99.248:50004/stcu2service/v1/mobile/";

//    public static String ipv4 =  "http://192.168.1.5:50004/stcu2service/v1/mobile/";

    ParadasFavoritasInterface.Presenter presenter;
    Context mContext;
    Activity mActivity;
    private String responseArriboColectivo = "";

    RequestQueue requestQueue;
    public ParadasFavoritasRepositoryImp(ParadasFavoritasInterface.Presenter presenter, Context mContext, Activity mActivity) {
        this.presenter = presenter;
        this.mContext = mContext;
        this.mActivity = mActivity;
        requestQueue = Volley.newRequestQueue(mContext);
    }


    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //para modo avion, networkinfo es null
        return connectivityManager.getActiveNetworkInfo();
    }


     public String makeRequestLlegadaColeApi(String idLineaString,  String idRecorridoString, String idParadaString){
        String url = ipv4+"obtenerTiempoLlegadaCole/"+ idLineaString +"/"+ idRecorridoString +"/"+ idParadaString;

        System.out.println("informacion: datos que envia qr. idLineaQr" + idLineaString);
        System.out.println("informacion: datos que envia qr. idRecorridoQr" + idRecorridoString);
        System.out.println("informacion: datos que envia qr. idParadaQr" + idParadaString);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseArriboColectivo =  response.getString("mensaje");
                            presenter.showArriboColectivo(responseArriboColectivo);
                            System.out.println("informacion del servidor ok : " + response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get Llegada cole: " + error.toString());
                        presenter.showArriboColectivo("No fue posible obtener tiempo arribo colectivo");
                    }
                });
        requestQueue.add(jsonObjectRequest);

        return responseArriboColectivo;

    }


}
