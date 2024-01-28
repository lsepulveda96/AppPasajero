package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class ScannerQRCodeRepositoryImp implements ScannerQRCodeRepository {

    // ip local actual
   // public static String ipv4 = "http://192.168.0.104:50004/stcu2service/v1/mobile/";

    // ip remoto actual
    public static String ipv4 =  "http://138.36.99.248:50004/stcu2service/v1/mobile/";

    RequestQueue requestQueue;
    private String responseArriboColectivo = "";
    ScannerQRCodeInterface.Presenter presenter;
    Context mContext;


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


    // metodo previo a modif que no devuevle nada
/*
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
    }*/

    public String makeRequestLlegadaColeApi(String idLineaString,  String idRecorridoString, String idParadaString){
        String url = ipv4+"obtenerTiempoLlegadaCole/"+ idLineaString +"/"+ idRecorridoString +"/"+ idParadaString;

        requestQueue = Volley.newRequestQueue(mContext);

        System.out.println("informacion: datos que envia qr. idLineaQr: " + idLineaString);
        System.out.println("informacion: datos que envia qr. idRecorridoQr: " + idRecorridoString);
        System.out.println("informacion: datos que envia qr. idParadaQr: " + idParadaString);

//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                response -> responseArriboColectivo = response,
//                error -> Log.d( "ERROR",error.toString() )
//        );

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
                    }
                });
        requestQueue.add(jsonObjectRequest);

        return responseArriboColectivo;
//        return msjeResp[0];
    }



















    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //para modo avion, networkinfo es null
        return connectivityManager.getActiveNetworkInfo();
    }
}
