package com.example.lucianodsepulveda.apppasajero.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.utilities.BaseVolleyFragment;
import com.example.lucianodsepulveda.apppasajero.utilities.ParadaCercana;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseVolleyFragment {

    public static String ipv4 = "http://stcu.mdn.unp.edu.ar:50002/stcu_app";

    private String resp = "";
    private ArrayAdapter<String> adaptador;
    private List<ParadaCercana> listaParadas;
    private ParadaCercana paradaCercana;
    private List<String> opciones = new ArrayList<String>();
    private TextView tvOSM;
    private Double distancia = 0.0;
    private String respuesta = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        tvOSM = (TextView) v.findViewById(R.id.tvOSM);
        hacerConsultaLineas();
        return v;
    }


    public List<ParadaCercana> hacerConsultaParadasRecorrido(final String seleccionLin) {
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
        addToQueue(jsonArrayRequest);
        return listaParadas;
    }


    //TODO pasar a model
    public void hacerConsultaLineas(){
        String url = ipv4+"/rest/lineas/activas";

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

                                opciones.add(denominacion);

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
        addToQueue(jsonArrayRequest);
    }

    public String makeRequestLlegadaCole(String idLinea, String idParada){
            String url = ipv4+"/rest/pasajeros/"+idLinea+"/"+idParada;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        respuesta = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "ERROR",error.toString() );
                    }
                }
        );

        addToQueue(getRequest);
        return respuesta;
    }

    public List<String> getOpciones() {
        return this.opciones;
    }

    public String getRespuesta(){
        return this.respuesta;
    }

}
