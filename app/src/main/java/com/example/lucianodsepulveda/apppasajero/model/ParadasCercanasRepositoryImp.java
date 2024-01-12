package com.example.lucianodsepulveda.apppasajero.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParadasCercanasRepositoryImp implements ParadasCercanasRepository {
    ParadasCercanasInterface.Presenter presenter;
    Context mContext;
    Activity mActivity;

   // public static String ipv4 = "http://192.168.0.104:50000/v1/mobile/";
   public static String ipv4 = "http://192.168.0.104:50004/stcu2service/v1/mobile/";

    private List<ParadaCercana> listaParadas;
    private ParadaCercana paradaCercana;
    RequestQueue requestQueue;
    public ParadasCercanasRepositoryImp(ParadasCercanasInterface.Presenter presenter, Context mContext, Activity mActivity) {
        this.presenter = presenter;
        this.mContext = mContext;
        this.mActivity = mActivity;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    public void getParadasCercanasAPI(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) {
        List<ParadaCercana> listaFinalParadasCercanas = new ArrayList<>();
        Double radioD = Double.parseDouble(eleccionRadioParadas);

        for (ParadaCercana item : listaParadasExistentes) {

            System.out.println("las paradas obtenidas!!! --------  direccion  -------- " + item.getDireccion());
            System.out.println("las paradas obtenidas!!! --------  latitud -------- " + item.getLatitud());
            System.out.println("las paradas obtenidas!!! --------  longitud -------- " + item.getLongitud());

            Double distancia = getDistanciaLocal(Double.parseDouble(latitudStr), Double.parseDouble(longitudStr), item.getLatitud(), item.getLongitud());

            if (distancia < radioD) {
                ParadaCercana paradaCercana = new ParadaCercana();
                paradaCercana.setLatitud(item.getLatitud());
                paradaCercana.setLongitud(item.getLongitud());
                paradaCercana.setDireccion(item.getDireccion());
                paradaCercana.setLineaDenom(item.getLineaDenom());
                paradaCercana.setDistancia(String.valueOf(distancia));
                listaFinalParadasCercanas.add(paradaCercana);
            }
        }
        presenter.showParadasCecanas(listaFinalParadasCercanas);
    }

    @Override
    public Double getDistanciaLocal(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (earthRadius * c);

        return Math.round(dist * 100) / 100d; // para rendondear el resultado
    }

    @Override
    public String getRadioLocal(String seleccionRadio) {

        //para inicializar con algun valor y que no haya errores
        String eleccionRadioParadas = "500.0";

        //"5 cuadras","10 cuadras","20 cuadras","50 cuadras"
        switch (seleccionRadio) {
            case "5 cuadras":
                eleccionRadioParadas = "500.0";
                break;
            case "10 cuadras":
                eleccionRadioParadas = "1000.0";
                break;
            case "20 cuadras":
                eleccionRadioParadas = "2000.0";
                break;
            case "50 cuadras":
                eleccionRadioParadas = "5000.0";
                break;
        }

        return eleccionRadioParadas;
    }

    @Override
    public List<ParadaCercana> makeConsultaParadasRecorridoApi(final String seleccionLin) {
        listaParadas = new ArrayList<>();
        String url = ipv4+"findAllParadasParaApp/"+seleccionLin;
//        String url = ipv4+"paradasParaApp/"+seleccionLin;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray ja = response.getJSONArray("data"); // get the JSONArray

                            for(int i=0;i<ja.length();i++){

                                JSONObject parada = ja.getJSONObject(i);

                                JSONObject coordLng = new JSONObject(parada.getString("parada")).getJSONObject("coordenada");
                                String longitud = coordLng.getString("lng");

                                JSONObject coordLat = new JSONObject(parada.getString("parada")).getJSONObject("coordenada");
                                String latitud = coordLat.getString("lat");

                                String direccion= new JSONObject(parada.getString("parada")).getString("direccion");

                                paradaCercana = new ParadaCercana();
                                paradaCercana.setLatitud(Double.parseDouble(latitud));
                                paradaCercana.setLongitud(Double.parseDouble(longitud));
                                paradaCercana.setDireccion(direccion);
                                paradaCercana.setLineaDenom(seleccionLin);

                                listaParadas.add(paradaCercana);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Respuesta del servidor con error: " + error.toString());

                    }
                });

        requestQueue.add(jsonObjectRequest);

        //metodo anterior
      /*  JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{

                            for(int i=0;i<response.length();i++){

                                JSONObject parada = response.getJSONObject(i);

                                String latitud = parada.getString("lat");
                                String longitud = parada.getString("lng");
                                String direccion = parada.getString("direccion");

                                paradaCercana = new ParadaCercana();
                                paradaCercana.setLatitud(Double.parseDouble(latitud));
                                paradaCercana.setLongitud(Double.parseDouble(longitud));
                                paradaCercana.setDireccion(direccion);
                                paradaCercana.setLineaDenom(seleccionLin);

                                listaParadas.add(paradaCercana);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //label.setText(error.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);*/

        return listaParadas;
    }

    @Override
    public void getPermisosLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permisos", "Se tienen los permisos!");
            } else {
                ActivityCompat.requestPermissions(
                        mActivity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
            }
        }
    }

    @Override
    public NetworkInfo isNetAvailableLocal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //para modo avion, networkinfo es null
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void getUbicacionPasajeroLocal() {
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                presenter.showUbicacionPasajero(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void makeConsultaLineasApi() {
        //TODO: OJO!! capaz necesito esto en todas las llamadas
        requestQueue = Volley.newRequestQueue(mContext);

        String url = ipv4+"lineas/activas";

        List<String> lineasDisponibles = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray ja = response.getJSONArray("data"); // get the JSONArray

                            for(int i=0;i<ja.length();i++){
                                JSONObject linea = ja.getJSONObject(i);
                                String denominacion = linea.getString("denominacion");
                                lineasDisponibles.add(denominacion);
                            }

                            presenter.showLineasDisponibles(lineasDisponibles);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Respuesta del servidor con error: " + error.toString());

                    }
                });

        requestQueue.add(jsonObjectRequest);

        // para un objeto simple
      /*  StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("La respuesta del servidor es: " + response);
                        //response.substring(0,500)
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("La respuesta del servidor con error!! es: " + error.toString());
            }
        });
        requestQueue.add(stringRequest);
*/

        //metodo anterior
      /*List<String> lineasDisponibles = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        System.out.println("------------------ Respuesta del servidor local" + response);
                        // Process the JSON
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject linea = response.getJSONObject(i);
                                String denominacion = linea.getString("denominacion");

                                lineasDisponibles.add(denominacion);
                                presenter.showLineasDisponibles(lineasDisponibles);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println("Error del servidor!!: " + error.toString());
//                      presenter.showResponseError("no se pudo cargar el listado de lineas activas");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

       */
    }


}
