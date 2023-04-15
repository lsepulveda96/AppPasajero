package com.example.lucianodsepulveda.apppasajero.model;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
public class ParadasCercanasInteractor extends AppCompatActivity implements ParadasCercanasInterface.Interactor {

    private ParadasCercanasRepository paradasCercanasRepository;
    public ParadasCercanasInteractor(ParadasCercanasInterface.Presenter presenter, Context mContext, Activity mActivity) {
        paradasCercanasRepository = new ParadasCercanasRepositoryImp(presenter, mContext, mActivity);
    }

    /**
     * Metodo que obtiene permisos necesarios de localicacion
     */
    public void obtenerPermisos() {
        paradasCercanasRepository.getPermisosLocal();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permisos", "Se tienen los permisos!");
            } else {
                ActivityCompat.requestPermissions(
                        mActivity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
            }
        }*/
    }

    /**
     * Metodo que machea la cantidad de cuadras con la cantidad de mts
     * @param seleccionRadio seleccion del usuario
     * @return
     */
    public String obtenerRadio(String seleccionRadio) {
        return paradasCercanasRepository.getRadioLocal(seleccionRadio);
        /*

        //para inicializar con algun valor y que no haya errores
        String eleccionRadioParadas = "500.0";

        //"5 cuadras","10 cuadras","20 cuadras","50 cuadras"
        if (seleccionRadio.equals("5 cuadras")) {
            eleccionRadioParadas = "500.0";
        } else if (seleccionRadio.equals("10 cuadras")) {
            eleccionRadioParadas = "1000.0";
        } else if (seleccionRadio.equals("20 cuadras")) {
            eleccionRadioParadas = "2000.0";
        } else if (seleccionRadio.equals("50 cuadras")) {
            eleccionRadioParadas = "5000.0";
        }

        return eleccionRadioParadas;
        */
    }

    @Override

    /**
     * Metodo que se utiliza para obtener la distancia entre dos ubicaciones geograficas
     */
    public Double obtenerDistancia(double lat1, double lng1, double lat2, double lng2) {
        return paradasCercanasRepository.getDistanciaLocal(lat1, lng1, lat2, lng2);
        /*
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return Math.round(dist * 100) / 100d; // para rendondear el resultado

         */
    }

    /**
     * Metodo que obtiene paradas cercanas dentro del radio especificado
     * @param listaParadasExistentes lista de todas las paradas existentes
     * @param eleccionRadioParadas eleccion radio del usuario
     * @param latitudStr latitud en que se encuentra el usuario
     * @param longitudStr longitud en que se encuentra el usuario
     */
    public void getListaParadasCercanasApi(List<ParadaCercana> listaParadasExistentes, String eleccionRadioParadas, String latitudStr, String longitudStr) throws InterruptedException {
        paradasCercanasRepository.getParadasCercanasAPI(listaParadasExistentes, eleccionRadioParadas, latitudStr, longitudStr);
        /*  List<ParadaCercana> listaFinalParadasCercanas = new ArrayList<ParadaCercana>();
        Double radioD = Double.parseDouble(eleccionRadioParadas);

        for (ParadaCercana item : listaParadasExistentes) {

            Double distancia = obtenerDistancia(Double.parseDouble(latitudStr), Double.parseDouble(longitudStr), item.getLatitud(), item.getLongitud());

            if (distancia < radioD) {
                System.out.println("---------------- Distancia: " + distancia + " Radio: " + radioD + "---------------");
                ParadaCercana paradaCercana = new ParadaCercana();
                paradaCercana.setLatitud(item.getLatitud());
                paradaCercana.setLongitud(item.getLongitud());
                paradaCercana.setDireccion(item.getDireccion());
                paradaCercana.setLineaDenom(item.getLineaDenom());
                paradaCercana.setDistancia(String.valueOf(distancia));
                listaFinalParadasCercanas.add(paradaCercana);
            }
        }
        return listaFinalParadasCercanas;

        return paradasCercanasRepository.getParadasCercanasAPI(listaParadasExistentes, eleccionRadioParadas, latitudStr, longitudStr);
    */
    }

    /**
     * Metodo que obtiene las paradas pertenecientes a un recorrido/linea
     * @param seleccionLin linea a la que pertenecen las paradas
     * @return lista paradas cercanas
     */
    public List<ParadaCercana> hacerConsultaParadasRecorridoApi(final String seleccionLin) {
        return paradasCercanasRepository.makeConsultaParadasRecorridoApi(seleccionLin);
        /*
        listaParadas = new ArrayList<ParadaCercana>();
        String url = ipv4+"/rest/paradasRecorrido/paradasParaApp/"+seleccionLin;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
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

                                String latitud = parada.getString("latitud");
                                String longitud = parada.getString("longitud");
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
//        addToQueue(jsonArrayRequest);
        // o jsonObjectRequest? ver eso
        requestQueue.add(jsonArrayRequest);

        return listaParadas; */
    }

    /**
     * Metodo que consulta la disponibilidad de la red
     * @return informacion de la red
     */
    public NetworkInfo isNetAvailable() {
        return paradasCercanasRepository.isNetAvailableLocal();
        /* ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //for airplane mode, networkinfo is null
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork;

        */
    }

    /**
     * Metodo que obtiene la ubicacion del pasajero
     */
    public void obtenerUbicacionPasajero() {
        paradasCercanasRepository.getUbicacionPasajeroLocal();
        /*
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
*/
    }

    /**
     * Metodo que obtiene un listado de lineas actualmente en servicio
     */
    public void makeConsultaLineas(){
        paradasCercanasRepository.makeConsultaLineasApi();
        /*
        //TODO: OJO!! capaz necesito esto en todas las llamadas
        requestQueue = Volley.newRequestQueue(mContext);
        String url = ipv4+"/rest/lineas/activas";
        List<String> lineasDisponibles = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject linea = response.getJSONObject(i);
                                String id = linea.getString("id");
                                String denominacion = linea.getString("denominacion");
                                String enServicio = linea.getString("enServicio");

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
                        //label.setText(error.toString());
//                        presenter.showResponseError("no se pudo cargar el listado de lineas activas");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
//        addToQueue(jsonArrayRequest);

         */
    }
}
