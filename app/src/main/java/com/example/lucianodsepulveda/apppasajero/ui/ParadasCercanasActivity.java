package com.example.lucianodsepulveda.apppasajero.ui;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;
import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;
import com.example.lucianodsepulveda.apppasajero.presenter.ParadasCercanasPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ParadasCercanasActivity extends Activity implements ParadasCercanasInterface.View {

    private String myLat, myLng;

    String eleccionLinea = "";
    String eleccionRadioParadas = "";
    private Button btnBuscarParadas;
    private List<String> lineasDisponibles;
    private String miLongitud, miLatitud;
    private List<ParadaCercana> listaParadasExistentes, listaParadasCercanas;
    private TextView tvRadio, tvUbicacion, tvNetwork;
    private ArrayAdapter<String> adapterSeleccionLinea, adapterSeleccionRadio;
    private ProgressDialog dialogBuscandoParadas, dialogCargandoLineas;
    AutoCompleteTextView autoCompleteTextViewLinea;
    AutoCompleteTextView autoCompleteTextViewRadio;

    //para interface
    ParadasCercanasInterface.Presenter presenter;

    private SwipeRefreshLayout swipe;

    //necesario para comprobar internet en tiempo real
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    //necesario para comprobar internet en tiempo real
    private void checkStatus(){


        NetworkInfo activeNetwork = presenter.isNetAvailable();
        if (null != activeNetwork) {

            switch (activeNetwork.getType()){
                case ConnectivityManager.TYPE_WIFI:
                case ConnectivityManager.TYPE_MOBILE:
//                    Toast.makeText(getApplicationContext(),"mobile encenidido", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(),"wifi encenidido", Toast.LENGTH_SHORT).show();
                    tvNetwork.setVisibility(View.GONE);
                    break;
            }
        }else {
            tvNetwork.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"internet apagado", Toast.LENGTH_SHORT).show();
            // si no hay internet no permite continuar, ver cuando volver a habilitarlo (cuando este cargada lista de lineas)
            btnBuscarParadas.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParadasCercanasPresenter(this, this, this);
        setContentView(R.layout.activity_paradas_cercanas);

        setLat("0");

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        autoCompleteTextViewRadio = findViewById(R.id.autoCompleteRadio);

        presenter.obtenerPermisos();
        inicializarElementos();
        try {
            obtenerParadasCercanas();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        autoCompleteTextViewLinea = findViewById(R.id.autoCompleteLinea);

        //desplaza para abajo para actualizar, reemplaza boton actualizar
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(() -> {

            presenter.makeConsultaLineas();

            dialogCargandoLineas = new ProgressDialog( ParadasCercanasActivity.this );
            dialogCargandoLineas.setMessage( "Cargando listado de lineas" );
            dialogCargandoLineas.show();

            final Handler handler2 = new Handler();
            final Runnable r2 = new Runnable(){
                public void run() {
                    swipe.setRefreshing(false);
                    dialogCargandoLineas.cancel();
                    Set<String> hs = new HashSet<String>();
                    hs.addAll(getLineasDisponibles());
                    adapterSeleccionLinea.clear();
                    for(String opcion: hs){
                        adapterSeleccionLinea.add(opcion);
                    }
                    autoCompleteTextViewLinea.setText("");
                    autoCompleteTextViewRadio.setText("");
                    autoCompleteTextViewRadio.setFocusable(false);
                    autoCompleteTextViewLinea.setFocusable(false);
                    eleccionLinea = "";
                    eleccionRadioParadas = "";

                    autoCompleteTextViewLinea.setAdapter(adapterSeleccionLinea);
                    btnBuscarParadas.setEnabled(false);

                    // si no se pudo cargar las lineas
                    if(adapterSeleccionLinea.isEmpty()) {
                        Toast.makeText( ParadasCercanasActivity.this,"No se pudo cargar el listado de lineas", Toast.LENGTH_SHORT ).show();
                    }
//                    else{
//                        btnBuscarParadas.setEnabled( true );
//                        itemSeleccionLinea.setEnabled(true);
//                    }
                }
            };
            handler2.postDelayed(r2,5000);
        });

        autoCompleteTextViewLinea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                eleccionLinea = item.toString();
                enableBtnBuscarParadas();
            }
        });
        // para que no sea editable, solo toma las opciones disponibles
        autoCompleteTextViewLinea.setInputType(InputType.TYPE_NULL);

        autoCompleteTextViewRadio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                eleccionRadioParadas = item.toString();
                enableBtnBuscarParadas();
            }
        });
        autoCompleteTextViewRadio.setInputType(InputType.TYPE_NULL);



    } // fin onCreate

    private void enableBtnBuscarParadas() {
        if(!eleccionRadioParadas.isEmpty() && !eleccionLinea.isEmpty())
            btnBuscarParadas.setEnabled(true);
//            Toast.makeText(this, "btn activado", Toast.LENGTH_SHORT).show();
    }


    private void inicializarElementos() {

        adapterSeleccionLinea = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item);
        tvRadio = (TextView) findViewById(R.id.tvSeleccionRadio);
        adapterSeleccionRadio = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item);

        btnBuscarParadas = (Button) findViewById(R.id.btnBuscarParadas);
        btnBuscarParadas.setEnabled(false);

        setLat("0");
        lineasDisponibles = new ArrayList<>();
        tvNetwork = (TextView)findViewById(R.id.tv_network);
        tvUbicacion = (TextView)findViewById(R.id.tvUbicacion);

        //necesario para comprobar internet en tiempo real
        IntentFilter intentFilter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        //


        //puede ir dentro de un metodo, se repite dos veces. Aca y para actualizar

        presenter.makeConsultaLineas();

        dialogCargandoLineas = new ProgressDialog( ParadasCercanasActivity.this );
        dialogCargandoLineas.setMessage( "Cargando listado de lineas" );
        dialogCargandoLineas.show();

        final Handler handler2 = new Handler();
        final Runnable r2 = new Runnable(){
            public void run() {
                swipe.setRefreshing(false);
                dialogCargandoLineas.cancel();
                Set<String> hs = new HashSet<String>();
                hs.addAll(getLineasDisponibles());
                adapterSeleccionLinea.clear();
                for(String opcion: hs){
                    adapterSeleccionLinea.add(opcion);
                }
                autoCompleteTextViewLinea.setAdapter(adapterSeleccionLinea);
                adapterSeleccionLinea.setDropDownViewResource(R.layout.textview_spinner_selected);


                // si no se pudo cargar las lineas
                if(adapterSeleccionLinea.isEmpty()) {
                    Toast.makeText( ParadasCercanasActivity.this,"No se pudo cargar el listado de lineas", Toast.LENGTH_SHORT ).show();
                }
//                else{
//                    btnBuscarParadas.setEnabled( true );
//                    itemSeleccionLinea.setEnabled(true);
//                }
            }
        };
        handler2.postDelayed(r2,5000);


    }

    private void obtenerParadasCercanas() throws InterruptedException {
        String[] opciones = getResources().getStringArray(R.array.radio_paradas);
        ArrayAdapter<String> adapterRadioParadas = new ArrayAdapter<String>(this,R.layout.textview_spinner_selected, opciones);
        autoCompleteTextViewRadio.setAdapter(adapterRadioParadas);
        adapterSeleccionRadio.setDropDownViewResource(R.layout.textview_spinner_selected);
        listaParadasCercanas = new ArrayList<ParadaCercana>();

        btnBuscarParadas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                BuscarParadasCercanas buscarParadasCercanas = new BuscarParadasCercanas();
                buscarParadasCercanas.execute();

            } // fin onClick


        });
    }

    public void setLat(String lat){this.myLat = lat;}

    public void setLng(String lng){
        this.myLng = lng;
    }

    public String getLat(){return this.myLat;}

    public String getLng(){return this.myLng;}

    //metodo para mostrar resultados desde el model, si no lo necesito lo borro
    @Override
    public void showResult(String result) {}

    @Override
    public void showUbicacionPasajero(String latitudeStr, String longitudeStr) {
        tvUbicacion.setText(latitudeStr + " " + longitudeStr);
        setLat(latitudeStr);
        setLng(longitudeStr);
    }

    @Override
    public void showLineasDisponibles(List<String> lineasDisponibles) {
        this.lineasDisponibles = lineasDisponibles;
    }

    @Override
    public void showResponseError(String error) {
        Toast.makeText( ParadasCercanasActivity.this,error, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void showParadasCecanas(List<ParadaCercana> listaFinalParadasCercanas) {
        this.listaParadasCercanas = listaFinalParadasCercanas;
    }



    // estos dos metodos se pueden unir
    private void obtenerTodasParadas() {
        listaParadasExistentes = new ArrayList<ParadaCercana>();
        listaParadasExistentes = obtenerParadasDesdeServidor();

        Boolean bandera = true;
        long cont = 0;

        //esto se puede mejorar
        while(bandera && cont < 20000000) {
            if (listaParadasExistentes.size() != 0) {
                bandera = false;

            }
            cont++;
        }
    }

    public List<ParadaCercana> obtenerParadasDesdeServidor(){
        List<ParadaCercana> listaTodasParadas = presenter.hacerConsultaParadasRecorrido(eleccionLinea);
        return listaTodasParadas;
    }

    public List<String> getLineasDisponibles() {
        return this.lineasDisponibles;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }


    // boton buscar paradas cercanas // obtiene ubicacion y luego llama a los demas metodos
    public class BuscarParadasCercanas extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialogGPS;

        public double lati = 0.0;
        public double longi = 0.0;

        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogGPS = new ProgressDialog(ParadasCercanasActivity.this);
            progressDialogGPS.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialogGPS.setCancelable(false);
            progressDialogGPS.setMessage("Obteniendo ubicacion GPS...");
            progressDialogGPS.show();
        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelado por usuario");
            mLocationManager.removeUpdates(mVeggsterLocationListener);
            progressDialogGPS.dismiss();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialogGPS.dismiss();
            if(getLat().equals("0")){
                Toast.makeText(ParadasCercanasActivity.this, "No se pudo obtener su ubicacion actual", Toast.LENGTH_SHORT).show();
            }else{
                System.out.println("---------------------- ubicacion obtenida con exito----------------------------------");
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
                            if (listaParadasExistentes.size() == 0) {

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
                            // esto lo retorna en string
//                                        eleccionRadioParadas = presenter.obtenerRadio(itemSeleccionRadio.getSelectedItem().toString());

//                                        presenter.getListaParadasCercanas(listaParadasExistentes, eleccionRadioParadas, getLatitutd(), getLongitud());
                            presenter.getListaParadasCercanas(listaParadasExistentes, String.valueOf(eleccionRadioParadas), getLat(), getLng());

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


                                intent.putExtra("lat", getLat());
                                intent.putExtra("lng", getLng());
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


        @Override
        protected Boolean doInBackground(String... params) {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    if (ContextCompat.checkSelfPermission(ParadasCercanasActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ParadasCercanasActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)){
                            ActivityCompat.requestPermissions(ParadasCercanasActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }else{
                            ActivityCompat.requestPermissions(ParadasCercanasActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mVeggsterLocationListener);
                }
            });

            int timeoutGPS = 0;
            while ((this.lati == 0.0 | this.longi == 0.0) & timeoutGPS < 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("esperando a la respuesta de la locacion gps" + timeoutGPS);
                timeoutGPS++;
            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                try {
                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    System.out.println("la latitud obtenida: " + lati);
                    System.out.println("++++++++++++++++++++++++++++++");
                    System.out.println("la longitud obtenida: " + longi);
                    setLat(String.valueOf(lati));
                    setLng(String.valueOf(longi));
                } catch (Exception e) {
                    System.out.println("error obteniendo la dir" + e);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");
            }
        }
    } // fin fetch coordinates
}



/*
// backup de lo que anda gps
  btnBuscarParadas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                dialogBuscandoUbicacion = new ProgressDialog(ParadasCercanasActivity.this);
                dialogBuscandoUbicacion.setMessage("Detectando ubicación..");
                dialogBuscandoUbicacion.show();

                System.out.println("----------------------se esta obteniendo la ubicacion----------------------------------");
                presenter.obtenerUbicacionPasajero();


                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        dialogBuscandoUbicacion.cancel();

                        if (getLatitutd().equals("0")) {
                            Toast toast1 = Toast.makeText(getApplicationContext(), "No se pudo obtener su ubicación actual", Toast.LENGTH_LONG);
                            toast1.show();
                        } else {

                            System.out.println("---------------------- ubicacion obtenida con exito----------------------------------");
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
                                        if (listaParadasExistentes.size() == 0) {

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
                                        // esto lo retorna en string
//                                        eleccionRadioParadas = presenter.obtenerRadio(itemSeleccionRadio.getSelectedItem().toString());

//                                        presenter.getListaParadasCercanas(listaParadasExistentes, eleccionRadioParadas, getLatitutd(), getLongitud());
                                        presenter.getListaParadasCercanas(listaParadasExistentes, String.valueOf(eleccionRadioParadas), getLatitutd(), getLongitud());

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


                                            intent.putExtra("lat", getLatitutd());
                                            intent.putExtra("lng", getLongitud());
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
 */
