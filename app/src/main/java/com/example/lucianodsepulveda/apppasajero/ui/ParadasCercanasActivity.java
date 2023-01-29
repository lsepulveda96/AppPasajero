package com.example.lucianodsepulveda.apppasajero.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;
import com.example.lucianodsepulveda.apppasajero.presenter.ParadasCercanasPresenter;
import com.example.lucianodsepulveda.apppasajero.utilities.ParadaCercana;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ParadasCercanasActivity extends FragmentActivity implements ParadasCercanasInterface.View {

    private Button btnObtenerUbicacion;
    private Button btnEncontrarUbicacion;
    private TextView tvUbicacion;
    private String miLatitud;
    private String miLongitud;
    private String eleccionRadioParadas;
    private List<ParadaCercana> listaParadas;
    private List<ParadaCercana> listaParadasCercanas;
    private Spinner itemSeleccionRadio;
    private Spinner itemSeleccionLinea;
    private TextView tvRadio;
    private ArrayAdapter<String> adapter;
    private ProgressBar progressBar;
    private TextView tvDisponibilidadRed;
    private TextView tvDisponibilidadInternet;
    private ProgressDialog dialogBuscandoUbicacion;
    private ProgressDialog dialogBuscandoParadas;
    private ProgressDialog dialogCargandoLineas;

    //para interface
    ParadasCercanasInterface.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParadasCercanasPresenter(this, this, this);
        setContentView(R.layout.activity_paradas_cercanas);

        presenter.obtenerPermisos();
//        obtenerPermisos();
        inicializarElementos();
        try {
            obtenerParadasCercanas();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void inicializarElementos() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        itemSeleccionLinea = (Spinner) findViewById(R.id.itemSeleccionLinea);
        tvUbicacion = (TextView) findViewById(R.id.tvUbicacion);
        tvRadio = (TextView) findViewById(R.id.tvRadio);
        itemSeleccionRadio = (Spinner) findViewById(R.id.spinner);
        btnObtenerUbicacion = (Button) findViewById(R.id.btnObtenerUbicacion);
        btnEncontrarUbicacion = (Button) findViewById(R.id.btnBuscarParadas);
        btnEncontrarUbicacion.setEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setearLatitud("0");
        tvDisponibilidadRed = (TextView) findViewById(R.id.tv_net);
        tvDisponibilidadInternet = (TextView) findViewById(R.id.tv_access);

        boolean redHabilitada = isOnlineNet();
        if (redHabilitada) {
            tvDisponibilidadRed.setText("Red habilitada");
        } else {
            tvDisponibilidadRed.setText("Red deshabilitada");
        }

        boolean accesoInternet = isNetDisponible();
        if (accesoInternet) {
            tvDisponibilidadInternet.setText("Conectado a internet");
        } else {
            tvDisponibilidadInternet.setText("Sin conexion a internet");
        }
    }

    //TODO pasar a model
//    private void obtenerPermisos() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                Log.i("Permisos", "Se tienen los permisos!");
//            } else {
//                ActivityCompat.requestPermissions(
//                        this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
//            }
//        }
//    }

    private void obtenerParadasCercanas() throws InterruptedException {
        String[] opciones = {"5 cuadras", "10 cuadras", "20 cuadras", "50 cuadras"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        itemSeleccionRadio.setAdapter(adapter);
        listaParadasCercanas = new ArrayList<ParadaCercana>();

        btnEncontrarUbicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                dialogBuscandoUbicacion = new ProgressDialog(ParadasCercanasActivity.this);
                dialogBuscandoUbicacion.setMessage("Detectando ubicación..");
                dialogBuscandoUbicacion.show();

                System.out.println("----------------------se esta obteniendo la ubicacion----------------------------------");
                //TODO pasar a model
                obtenerUbicacionPasajero();


                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        dialogBuscandoUbicacion.cancel();

                        if (obtenerLatitutd().equals("0")) {
                            Toast toast1 = Toast.makeText(getApplicationContext(), "No se pudo obtener su ubicación actual", Toast.LENGTH_LONG);
                            toast1.show();
                        } else {


                            System.out.println("---------------------- ya se obtuvo correctamente la ubicacion----------------------------------");
                            dialogBuscandoParadas = new ProgressDialog(ParadasCercanasActivity.this);
                            dialogBuscandoParadas.setMessage("Buscando paradas cercanas..");
                            dialogBuscandoParadas.show();


                            System.out.println("----------------------se estan obteniendo todas las paradas----------------------------------");
                            //TODO pasar a model
                            obtenerTodasParadas();

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {


                                    try {
                                        if (listaParadas.size() == 0) {

                                            System.out.println("----------------------no se pudo cargar la lista de paradas----------------------------------");
                                            dialogBuscandoParadas.cancel();
                                            Looper.prepare();
                                            Toast.makeText(getApplicationContext(), "No se pudo cargar la lista de paradas", Toast.LENGTH_LONG).show();
                                            Looper.loop();
                                            return;
                                        }

                                        System.out.println("----------------------se obtuvieron todas las paradas----------------------------------");
                                        System.out.println("----------------------se esta obteniendo las paradas mas cercanas----------------------------------");
                                        //TODO pasar a model
                                        listaParadasCercanas = obtenerListaParadasCercanas(listaParadas);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if (listaParadasCercanas.size() == 0) {
                                                System.out.println("----------------------no se encontraron paradas cercanas----------------------------------");

                                                dialogBuscandoParadas.cancel();

                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "No se encontraron paradas cercanas", Toast.LENGTH_LONG).show();
                                                Looper.loop();
                                                return;
                                            }

                                            System.out.println("----------------------se obtuvieron las paradas cercanas y se abre el mapa----------------------------------");

                                            dialogBuscandoParadas.cancel();
                                            Intent intent = new Intent(ParadasCercanasActivity.this, MapsActivity.class);
                                            intent.putParcelableArrayListExtra("key", (ArrayList<? extends Parcelable>) listaParadasCercanas);
                                            intent.putExtra("lat", obtenerLatitutd());
                                            intent.putExtra("lng", obtenerLongitud());
                                            intent.putExtra("radio", eleccionRadioParadas);

                                            startActivity(intent);
                                            Looper.prepare(); // to be able to make toast
                                            Toast.makeText(getApplicationContext(), "Abriendo mapa..", Toast.LENGTH_LONG).show();
                                            Looper.loop();

                                        }
                                    }, 5000);

                                }
                            }, 5000);

                        }
                    }
                };
                handler.postDelayed(r, 10000);
            } // fin onClick


        });
    }

    //TODO pasar a model
    private List<ParadaCercana> obtenerListaParadasCercanas(List<ParadaCercana> paradas) throws InterruptedException {
        obtenerRadio();
        List<ParadaCercana> listaFinalParadasCercanas = new ArrayList<ParadaCercana>();
        Double radioD = Double.parseDouble(eleccionRadioParadas);

        //Todo: esto esta andando? probar con println
        if (paradas.size() == 0) {
            Hilo hilo = new Hilo();
            hilo.run();
        }

        for (ParadaCercana item : paradas) {

            Double distancia = obtenerDistancia(Double.parseDouble(obtenerLatitutd()), Double.parseDouble(obtenerLongitud()), item.getLatitud(), item.getLongitud());

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
    }

    // Metodo que se utiliza para obtener la distancia entre dos ubicaciones geograficas
    public Double obtenerDistancia(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return Math.round(dist * 100) / 100d; // para rendondear el resultado
    }

    public void obtenerUbicacionPasajero() {

        LocationManager locationManager = (LocationManager) ParadasCercanasActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvUbicacion.setText(location.getLatitude() + "" + location.getLongitude());
                setearLatitud(String.valueOf(location.getLatitude()));
                setearLongitud(String.valueOf(location.getLongitude()));
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

    }

    public String obtenerLatitutd(){return this.miLatitud; }

    public String obtenerLongitud(){
        return this.miLongitud;
    }

    public void setearLatitud(String lat){
        this.miLatitud = lat;
    }

    public void setearLongitud(String lng){
        this.miLongitud = lng;
    }

    //TODO pasar a model
    //metodo que recupera la eleccion del usuario
    public void obtenerRadio() {

        String seleccionRadio = itemSeleccionRadio.getSelectedItem().toString();
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
    }

    //TODO pasar a model
    public void actualizarInformacion(View view) {

        boolean redHab = isOnlineNet();
        if(redHab) {
            tvDisponibilidadRed.setText( "Red habilitada" );
        }else{
            tvDisponibilidadRed.setText( "Red deshabilitada" );
        }

        boolean accesoInter = isNetDisponible();
        if(accesoInter) {
            tvDisponibilidadInternet.setText( "Conectado a internet" );
        }else{
            tvDisponibilidadInternet.setText("Sin conexion a internet");
        }

        final MainFragment fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_fragment) ;
        fragment.hacerConsultaLineas();
        dialogCargandoLineas = new ProgressDialog( ParadasCercanasActivity.this );
        dialogCargandoLineas.setMessage( "Cargando listado de lineas" );
        dialogCargandoLineas.show();

        final Handler handler2 = new Handler();
        final Runnable r2 = new Runnable(){
            public void run() {
                dialogCargandoLineas.cancel();
                List<String> opciones = fragment.getOpciones();
                Set<String> hs = new HashSet<String>();
                hs.addAll(opciones);
                adapter.clear();
                for(String opcion: hs){
                    adapter.add(opcion);
                }
                itemSeleccionLinea.setAdapter(adapter);

                if(adapter.isEmpty()) {
                    Toast.makeText( ParadasCercanasActivity.this,"No se pudo cargar el listado de lineas", Toast.LENGTH_SHORT ).show();
                }else{
                    btnEncontrarUbicacion.setEnabled( true );
                }
            }
        };
        handler2.postDelayed(r2,5000);
    }

    // red habilitada
    public boolean isNetDisponible(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    // si hay acceso a internet
    public Boolean isOnlineNet(){

        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //metodo para mostrar resultados desde el model, si no lo necesito lo borro
    @Override
    public void showResult(String result) {

    }

    class Hilo extends Thread{
        public void run(){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // estos dos metodos se pueden unir
    private void obtenerTodasParadas() {
        listaParadas = new ArrayList<ParadaCercana>();
        listaParadas = obtenerParadasDesdeServidor();

        Boolean bandera = true;
        long cont = 0;

        while(bandera && cont < 20000000) {
            if (listaParadas.size() != 0) {
                bandera = false;

            }
            cont++;
        }
    }

    public List<ParadaCercana> obtenerParadasDesdeServidor(){
        String seleccionLinea = itemSeleccionLinea.getSelectedItem().toString();
        obtenerRadio();
        MainFragment fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_fragment) ;
        List<ParadaCercana> listaTodasParadas = fragment.hacerConsultaParadasRecorrido(seleccionLinea);
        return listaTodasParadas;
    }

}
