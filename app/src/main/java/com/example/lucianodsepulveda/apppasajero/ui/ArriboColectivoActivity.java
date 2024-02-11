package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;
import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;
import com.example.lucianodsepulveda.apppasajero.presenter.ScannerQRCodePresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ArriboColectivoActivity extends FragmentActivity implements ScannerQRCodeInterface.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,GoogleMap.OnMapLongClickListener,View.OnClickListener{

    private GoogleMap mMap;
    private Button btnGuardarCodigo, btnVolverAConsultar;
    private String dataC;
    private String dataQrCode;
    private List<ParadaCercana> paradasPorRecorrerList = new ArrayList<>();
    MainFragment fragment;
//    Button atras;

    TextView TvParadaActualColeDire;
    TextView TvTiempoArriboCole;
    TextView TvFechaParadaActual;
    String idLinea, idRecorrido, idParada, arriboColectivo, latParadaActualColectivo, lngParadaActualColectivo, fechaParadaActualString, latParadaActualPasajero, lngParadaActualPasajero, paradaActualColeDire, codShow;
    List<Marker> markers;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    ScannerQRCodeInterface.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        presenter = new ScannerQRCodePresenter(this, this);

        setContentView(R.layout.activity_arribo_colectivo);

        btnGuardarCodigo = findViewById(R.id.btnGuardarCodigoFav);
        btnVolverAConsultar = findViewById(R.id.btnVolverAConsultar);

        // para volver a consultar
        // tambien para traer la ubicacion del pasajero. crear metodo para consultar
        idLinea = getIntent().getExtras().getString("idLinea");
        idRecorrido = getIntent().getExtras().getString("idRecorrido");
        idParada = getIntent().getExtras().getString("idParada");
        arriboColectivo = getIntent().getExtras().getString("arriboColectivo");
        latParadaActualColectivo = getIntent().getExtras().getString("latParadaActualColectivo");
        lngParadaActualColectivo = getIntent().getExtras().getString("lngParadaActualColectivo");
        latParadaActualPasajero = getIntent().getExtras().getString("latParadaActualPasajero");
        lngParadaActualPasajero = getIntent().getExtras().getString("lngParadaActualPasajero");
        fechaParadaActualString = getIntent().getExtras().getString("fechaParadaActualString");
        paradaActualColeDire = getIntent().getExtras().getString("paradaActualColeDire");
        paradasPorRecorrerList = getIntent().getParcelableArrayListExtra("paradasPorRecorrerList");
        codShow = getIntent().getExtras().getString("codShow");
        dataQrCode = getIntent().getExtras().getString("dataQrCode");
        setData(dataQrCode);
        TvTiempoArriboCole = findViewById(R.id.tv_tiempoArriboColectivo);
        TvTiempoArriboCole.setText(arriboColectivo);
        TvParadaActualColeDire = findViewById(R.id.tv_direccionParadaActual);
        TvParadaActualColeDire.setText(paradaActualColeDire);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(fechaParadaActualString));
        DateFormat formateador= new SimpleDateFormat("dd/M/yy hh:mm");

        TvFechaParadaActual = findViewById(R.id.tv_fechaParadaActual);
        TvFechaParadaActual.setText(formateador.format(cal.getTime()));

//        atras = (Button)findViewById(R.id.btnAtras);
        markers = new ArrayList<Marker>();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if(status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map_arribo_colectivo);
            mapFragment.getMapAsync(this);
        }else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10); // si los servicios de google play no estan funcionando correctamente envia un error
            dialog.show();
        }


        btnVolverAConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.makeRequestLlegadaCole(idLinea, idRecorrido, idParada);
            }
        });


        btnGuardarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences preferences = ArriboColectivoActivity.this.getSharedPreferences("Codigos", Context.MODE_PRIVATE);
                boolean codigoExiste = false;


                Map<String, ?> allEntries = preferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//                    if (getData().equals(entry.getValue().toString())) {
//                        codigoExiste = true;
//                    }
                    if (codShow.equals(entry.getKey())) {
                        codigoExiste = true;
                    }
                }

                // si el codigo no existe, lo guarda
                if (!codigoExiste) {
                    SharedPreferences.Editor myEditor = preferences.edit();
                    myEditor.putString(codShow, getData()); // para que guarde el mismo valor como clave ( que sea unica );
                    myEditor.commit();
                    Toast t5 = Toast.makeText(getApplicationContext(), "Guardado con exito!", Toast.LENGTH_SHORT);
                    t5.show();
                } else {
                    Toast t6 = Toast.makeText(getApplicationContext(), "El codigo QR ya existe!", Toast.LENGTH_SHORT);
                    t6.show();
                }

            }
        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {



                //Your code where the exception occurred goes here
                mMap = googleMap;
                googleMap.clear();
                mMap.clear();

                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // hay mapa satelital, normal, de terreno o sin capas
                mMap.clear();

                UiSettings uiSettings = mMap.getUiSettings();
                mMap.getUiSettings().setZoomControlsEnabled(true);





                // Add a marker in Sydney and move the camera
                LatLng ubicacionParadaActualColectivo = new LatLng(Double.parseDouble(latParadaActualColectivo), Double.parseDouble(lngParadaActualColectivo)); // marcador por defecto
                //mMap.addMarker(new MarkerOptions().position(ubicacionParadaActualColectivo).title("Parada actual colectivo").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
                mMap.addMarker(new MarkerOptions().position(ubicacionParadaActualColectivo).title("Parada actual colectivo").icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus_stop_icon_blue)));
//        float zoomLevel = 15;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionParadaActualColectivo,zoomLevel)); // para agregarle zoom


                LatLng latlngubicacionParadaPasajero = new LatLng(Double.parseDouble(latParadaActualPasajero), Double.parseDouble(lngParadaActualPasajero)); // marcador por defecto
//            Marker marcador = mMap.addMarker(new MarkerOptions().position(ubicacionParadaPasajero).title(item.getDireccion()).snippet("Distancia: " + item.getDistancia() +" metros").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
                Marker marcador = mMap.addMarker(new MarkerOptions().position(latlngubicacionParadaPasajero).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.man_img)));
                markers.add(marcador);

                LatLngBounds myBounds = new LatLngBounds(ubicacionParadaActualColectivo,latlngubicacionParadaPasajero);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(latlngubicacionParadaPasajero);
                builder.include(ubicacionParadaActualColectivo);
                // Add your locations to bounds using builder.include, maybe in a loop


                if(paradasPorRecorrerList != null){
                    System.out.println("llega la respuesta aca paradasPorRecorrer dentro de on map ready: "  + paradasPorRecorrerList.toString() );
                    // anade paradas por recorrer
                    for (ParadaCercana paradaPorRecorrer: paradasPorRecorrerList) {
                        LatLng ubicacionparadaPorRecorrer = new LatLng(paradaPorRecorrer.getLatitud(), paradaPorRecorrer.getLongitud()); // marcador por defecto
                        System.out.println("ubicacion parada por recorrer:" + ubicacionparadaPorRecorrer.toString());
                        //mMap.addMarker(new MarkerOptions().position(ubicacionParadaActualColectivo).title("Parada actual colectivo").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
                        mMap.addMarker(new MarkerOptions().position(ubicacionparadaPorRecorrer).title(paradaPorRecorrer.getDireccion()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icono_bus_stop_gris)));
                    }
                }


                LatLngBounds bounds = builder.build();

//Then construct a cameraUpdate
                CameraUpdate cameraUpdate;
                cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
//Then move the camera
                mMap.animateCamera(cameraUpdate);

//        Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(-42.77202233341884, -65.02974987030031), new LatLng(-42.77405224276057, -65.03590553998949))
//                .width(5)
//                .color(Color.BLUE));
                mMap.getUiSettings().setMapToolbarEnabled(false);
            }
        });


    }


//    public void atras(View view){
    public void salir(){
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // hay mapa satelital, normal, de terreno o sin capas
        for(Marker item: markers){
            item.setVisible(false);
            item.remove();
        }
//        paradas = new ArrayList<ParadaCercana>();
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList) {
        //       Toast.makeText(this, "llego la consulta" + fechaParadaActualString, Toast.LENGTH_LONG).show();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(fechaParadaActualString));
        DateFormat formateador= new SimpleDateFormat("dd/M/yy hh:mm");
        TvFechaParadaActual.setText(formateador.format(cal.getTime()));
        this.fechaParadaActualString = fechaParadaActualString;

        TvTiempoArriboCole.setText(tiempoArriboColProximoString);
        this.arriboColectivo = tiempoArriboColProximoString;

        this.latParadaActualColectivo = latParadaActualColectivo;
        this.lngParadaActualColectivo = lngParadaActualColectivo;

        this.latParadaActualPasajero = latParadaActualPasajero;
        this.lngParadaActualPasajero = lngParadaActualPasajero;

        TvParadaActualColeDire.setText(paradaActualColeDire);
        this.paradaActualColeDire = paradaActualColeDire;

        this.paradasPorRecorrerList = paradasPorRecorrerList;

        System.out.println("llega la respuesta aca paradasPorRecorrer show arribo cole: "  + paradasPorRecorrerList.toString() );
        this.onMapReady(mMap);
        // llamar a onMapReady?
    }

    @Override
    public void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError) {

        if(Integer.parseInt(codigoError)==400) {
            this.arriboColectivo = responseTiempoArriboColectivo;
            AlertDialog.Builder builder = new AlertDialog.Builder(ArriboColectivoActivity.this);
            builder.setTitle("Informacion:");
            builder.setMessage(responseTiempoArriboColectivo);
            builder.setCancelable(false);
            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    public void setData(String data) {
        this.dataC = data;
    }

    public String getData(){
        return this.dataC;
    }

/*    @Override
    public void showUbicacionParadaPasajero(String ubicacionParadaPasajero) {
//        System.out.println("informacion obtenerUbicacionParadaRecorrido en arribo cole activity: " + ubicacionParadaPasajero);
//        latParadaPasajero = Double.valueOf(ubicacionParadaPasajero.split(",")[0]);
//        lngParadaPasajero = Double.valueOf(ubicacionParadaPasajero.split(",")[1]);

//        LatLng latlngubicacionParadaPasajero = new LatLng(latParadaPasajero, lngParadaPasajero); // marcador por defecto
////            Marker marcador = mMap.addMarker(new MarkerOptions().position(ubicacionParadaPasajero).title(item.getDireccion()).snippet("Distancia: " + item.getDistancia() +" metros").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
//        Marker marcador = mMap.addMarker(new MarkerOptions().position(latlngubicacionParadaPasajero).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.man_img)));
//        markers.add(marcador);


//        Toast.makeText(this, "coordenadas ubicacion pasajero! " + latParadaPasajero + " - " + lngParadaPasajero, Toast.LENGTH_LONG).show();


    }*/


}