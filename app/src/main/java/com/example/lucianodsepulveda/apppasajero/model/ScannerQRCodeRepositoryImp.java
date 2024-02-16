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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScannerQRCodeRepositoryImp implements ScannerQRCodeRepository {

    // ip local actual
//    public static String ipv4 = "http://192.168.0.105:50004/stcu2service/v1/mobile/";

    // ip remoto actual
    public static String ipv4 =  "http://138.36.99.248:50004/stcu2service/v1/mobile/";

//
    RequestQueue requestQueue;
    private String responseTiempoArriboColectivo = "";
    private String ubicacionParadaPasajero = "";
    ScannerQRCodeInterface.Presenter presenter;
    Context mContext;


    public ScannerQRCodeRepositoryImp(ScannerQRCodeInterface.Presenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
        requestQueue = Volley.newRequestQueue(mContext);
    }


    public String makeRequestLlegadaColeApi(String idLineaString,  String idRecorridoString, String idParadaString){
        String url = ipv4+"obtenerTiempoLlegadaCole/"+ idLineaString +"/"+ idRecorridoString +"/"+ idParadaString;

        requestQueue = Volley.newRequestQueue(mContext);

        System.out.println("informacion: datos que envia qr. idLineaQr: " + idLineaString);
        System.out.println("informacion: datos que envia qr. idRecorridoQr: " + idRecorridoString);
        System.out.println("informacion: datos que envia qr. idParadaQr: " + idParadaString);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseTiempoArriboColectivo =  response.getString("mensaje");

                            System.out.println("informacion del servidor ok : " + response);

                            String codigoError = response.getString("codigo");

                            if(Integer.parseInt(codigoError) == 200){

                                JSONObject dataArriboColectivo  = response.getJSONObject("data");

                                String fechaParadaActualString = dataArriboColectivo.getString("fechaParadaActual");

                                String tiempoArriboColProximoString = dataArriboColectivo.getString("tiempoArriboColProximo");

                                JSONObject paradaActualColeDir = new JSONObject(dataArriboColectivo.getString("paradaActual"));
                                String paradaActualColeDire = paradaActualColeDir.getString("direccion");

                                JSONObject paradaActualPasajeroDir = new JSONObject(dataArriboColectivo.getString("paradaActualPasajero"));
                                String paradaActualPasajeroDire = paradaActualPasajeroDir.getString("direccion");

                                JSONObject paradaActualColeCoor = new JSONObject(dataArriboColectivo.getString("paradaActual")).getJSONObject("coordenadas");
                                String coordenadasParada = paradaActualColeCoor.getString("coordinates");

                                String coordenadasSinComillas = "";
                                coordenadasSinComillas = coordenadasParada.replace("[", "").replace("]", ""); // saca corchetes
                                String[] latLngParadaActualColectivo = coordenadasSinComillas.split(",");


                                JSONObject paradaActualPasajero = new JSONObject(dataArriboColectivo.getString("paradaActualPasajero")).getJSONObject("coordenadas");
                                String coordenadasParadaActualPasajero = paradaActualPasajero.getString("coordinates");

                                String coordenadasParadaPasajeroSinComillas = "";
                                coordenadasParadaPasajeroSinComillas = coordenadasParadaActualPasajero.replace("[", "").replace("]", ""); // saca corchetes
                                String[] latLngParadaActualPasajero = coordenadasParadaPasajeroSinComillas.split(",");


                                JSONArray listaParadasARecorrer = dataArriboColectivo.getJSONArray("listaParadasPorRecorrer"); // get the JSONArray

                                List<ParadaCercana> paradasPorRecorrerList = new ArrayList<>();

                                for (int i = 0; i < listaParadasARecorrer.length(); i++) {
                                    JSONObject parada = new JSONObject(listaParadasARecorrer.getJSONObject(i).getString("parada"));
                                    ParadaCercana paradaPorRecorrer = new ParadaCercana();
                                    paradaPorRecorrer.setDireccion(parada.getString("direccion"));
                                    JSONObject coorParada = parada.getJSONObject("coordenada");
                                    paradaPorRecorrer.setLatitud(Double.parseDouble(coorParada.getString("lat")));
                                    paradaPorRecorrer.setLongitud(Double.parseDouble(coorParada.getString("lng")));
                                    paradasPorRecorrerList.add(paradaPorRecorrer);
                                }

                                presenter.showArriboColectivo(fechaParadaActualString, tiempoArriboColProximoString, latLngParadaActualColectivo[0], latLngParadaActualColectivo[1], latLngParadaActualPasajero[0], latLngParadaActualPasajero[1], paradaActualColeDire, codigoError, paradasPorRecorrerList, paradaActualPasajeroDire);

                            }else{
                                presenter.showMsajeSinColectivos(responseTiempoArriboColectivo, codigoError);
                            }

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

        return responseTiempoArriboColectivo;
    }


    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //para modo avion, networkinfo es null
        return connectivityManager.getActiveNetworkInfo();
    }




    // crear metodo obtener ubicacion pasajero
    // no usa mas, se pasaron los datos por dto en tiempo arribo colectivo
    /*public String makeRequestGetUbicacionParadaRecorrido(String idLineaString,  String idRecorridoString, String idParadaString){
        String url = ipv4+"obtenerUbicacionParadaRecorrido/"+ idLineaString +"/"+ idRecorridoString +"/"+ idParadaString;


        requestQueue = Volley.newRequestQueue(mContext);

        System.out.println("informacion: datos que envia ubicacion pasajero. idParadaQr: " + idParadaString);
        System.out.println("informacion: datos que envia ubicacion pasajero. idLineaString: " + idLineaString);
        System.out.println("informacion: datos que envia ubicacion pasajero. idRecorridoString: " + idRecorridoString);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ubicacionParadaPasajero =  response.getString("mensaje");
                            presenter.showUbicacionParadaPasajero(ubicacionParadaPasajero);
                            System.out.println("informacion del servidor ok obtenerUbicacionParadaRecorrido : " + response);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get ubicacion parada pasajero: " + error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);

        return ubicacionParadaPasajero;
    }*/




}
