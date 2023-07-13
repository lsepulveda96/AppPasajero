package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.fragment.app.FragmentActivity;
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

public class ParadasCercanasActivity extends FragmentActivity implements ParadasCercanasInterface.View {


    private Button btnObtenerUbicacion, btnEncontrarUbicacion;
//    private Button btnEncontrarUbicacion;
//    private TextView tvUbicacion;
//    private String miLatitud;
    private List<String> lineasDisponibles;
    private String miLongitud, eleccionRadioParadas, miLatitud;
//    private String eleccionRadioParadas;
    private List<ParadaCercana> listaParadasExistentes, listaParadasCercanas;
//    private List<ParadaCercana> listaParadasCercanas;
    private Spinner itemSeleccionRadio, itemSeleccionLinea;
//    private Spinner itemSeleccionLinea;
    private TextView tvRadio, tvUbicacion, tvDisponibilidadRed, tvDisponibilidadInternet, tvNetwork;
    private ArrayAdapter<String> adapter;
    private ProgressBar progressBar;
//    private TextView tvDisponibilidadRed, tvDisponibilidadInternet;
//    private TextView tvDisponibilidadInternet;
    private ProgressDialog dialogBuscandoUbicacion, dialogBuscandoParadas, dialogCargandoLineas;
//    private ProgressDialog dialogBuscandoParadas;
//    private ProgressDialog dialogCargandoLineas;

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
                case ConnectivityManager.TYPE_WIFI:Toast.makeText(getApplicationContext(),"wifi encenidido", Toast.LENGTH_SHORT).show();
                    tvNetwork.setVisibility(View.GONE);
                    break;
                case ConnectivityManager.TYPE_MOBILE:Toast.makeText(getApplicationContext(),"mobile encenidido", Toast.LENGTH_SHORT).show();
                    tvNetwork.setVisibility(View.GONE);
                    break;
            }
        }else {
            tvNetwork.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"internet apagado", Toast.LENGTH_SHORT).show();
            // si no hay internet no permite continuar, ver cuando volver a habilitarlo (cuando este cargada lista de lineas)
            btnEncontrarUbicacion.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParadasCercanasPresenter(this, this, this);
        setContentView(R.layout.activity_paradas_cercanas);

        presenter.obtenerPermisos();
        inicializarElementos();
        try {
            obtenerParadasCercanas();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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
                    adapter.clear();
                    for(String opcion: hs){
                        adapter.add(opcion);
                    }
                    itemSeleccionLinea.setAdapter(adapter);

                    // si no se pudo cargar las lineas
                    if(adapter.isEmpty()) {
                        Toast.makeText( ParadasCercanasActivity.this,"No se pudo cargar el listado de lineas", Toast.LENGTH_SHORT ).show();
                    }else{
                        btnEncontrarUbicacion.setEnabled( true );
                        itemSeleccionLinea.setEnabled(true);
                    }
                }
            };
            handler2.postDelayed(r2,5000);
        });
    }

    private void inicializarElementos() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        itemSeleccionLinea = (Spinner) findViewById(R.id.spinnerItemSeleccionLinea);
        itemSeleccionLinea.setEnabled(false);

        tvUbicacion = (TextView) findViewById(R.id.tvUbicacion);
        tvRadio = (TextView) findViewById(R.id.tvSeleccionRadio);
        itemSeleccionRadio = (Spinner) findViewById(R.id.spinnerItemSeleccionRadio);

        //TODO:quitar!! no se esta usando
        btnObtenerUbicacion = (Button) findViewById(R.id.btnObtenerUbicacion);

        btnEncontrarUbicacion = (Button) findViewById(R.id.btnBuscarParadas);
        btnEncontrarUbicacion.setEnabled(false);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.GONE);
//        tvDisponibilidadRed = (TextView) findViewById(R.id.tv_net);
//        tvDisponibilidadInternet = (TextView) findViewById(R.id.tv_access);
        setLatitud("0");
        lineasDisponibles = new ArrayList<>();
        tvNetwork = (TextView)findViewById(R.id.tv_network);

        //necesario para comprobar internet en tiempo real
        IntentFilter intentFilter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        //



//        boolean redHabilitada = presenter.isOnlineNet();
//        boolean redHabilitada = isOnlineNet();
//        if (presenter.isOnlineNet()) {
//            tvDisponibilidadRed.setText("Red habilitada");
//        } else {
//            tvDisponibilidadRed.setText("Red deshabilitada");
//        }
//
////        boolean accesoInternet = presenter.isNetDisponible();
////        boolean accesoInternet = isNetDisponible();
//        if (presenter.isNetDisponible()) {
//            tvDisponibilidadInternet.setText("Conectado a internet");
//        } else {
//            tvDisponibilidadInternet.setText("Sin conexion a internet");
//        }
    }

    private void obtenerParadasCercanas() throws InterruptedException {
        String[] opciones = {"5 cuadras", "10 cuadras", "20 cuadras", "50 cuadras"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(R.layout.textview_spinner_selected);
        itemSeleccionRadio.setAdapter(adapter);
        listaParadasCercanas = new ArrayList<ParadaCercana>();

        btnEncontrarUbicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                dialogBuscandoUbicacion = new ProgressDialog(ParadasCercanasActivity.this);
                dialogBuscandoUbicacion.setMessage("Detectando ubicación..");
                dialogBuscandoUbicacion.show();

                System.out.println("----------------------se esta obteniendo la ubicacion----------------------------------");
                //TODO pasar a model
//                obtenerUbicacionPasajero();
                presenter.obtenerUbicacionPasajero();


                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        dialogBuscandoUbicacion.cancel();

                        if (getLatitutd().equals("0")) {
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
                                        eleccionRadioParadas = presenter.obtenerRadio(itemSeleccionRadio.getSelectedItem().toString());
//
                                        //cambiar este return, deberia llamarse desde el presenter, showListaParadasCercanas
//                                        listaParadasCercanas = presenter.getListaParadasCercanas(listaParadasExistentes, eleccionRadioParadas, getLatitutd(), getLongitud());

                                        presenter.getListaParadasCercanas(listaParadasExistentes, eleccionRadioParadas, getLatitutd(), getLongitud());

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
    }

    public String getLatitutd(){return this.miLatitud; }

    public String getLongitud(){
        return this.miLongitud;
    }

    public void setLatitud(String lat){
        this.miLatitud = lat;
    }

    public void setLongitud(String lng){
        this.miLongitud = lng;
    }


    //metodo para mostrar resultados desde el model, si no lo necesito lo borro
    @Override
    public void showResult(String result) {

    }

    @Override
    public void showUbicacionPasajero(String latitudeStr, String longitudeStr) {
        tvUbicacion.setText(latitudeStr + " " + longitudeStr);
        setLatitud(latitudeStr);
        setLongitud(longitudeStr);
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
        String seleccionLinea = itemSeleccionLinea.getSelectedItem().toString();
        eleccionRadioParadas = presenter.obtenerRadio(itemSeleccionRadio.getSelectedItem().toString());
        List<ParadaCercana> listaTodasParadas = presenter.hacerConsultaParadasRecorrido(seleccionLinea);
        return listaTodasParadas;
    }

    public List<String> getLineasDisponibles() {
        return this.lineasDisponibles;
    }


}
