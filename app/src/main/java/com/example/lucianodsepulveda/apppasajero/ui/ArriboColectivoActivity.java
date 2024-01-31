package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;
import com.example.lucianodsepulveda.apppasajero.presenter.ScannerQRCodePresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ArriboColectivoActivity extends FragmentActivity implements ScannerQRCodeInterface.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,GoogleMap.OnMapLongClickListener,View.OnClickListener{

    private GoogleMap mMap;
//    List<ParadaCercana> paradas = new ArrayList<>();
    MainFragment fragment;
//    Button atras;

    TextView TvTiempoArriboCole;
    TextView TvFechaParadaActual;
    String idLinea, idRecorrido, idParada, arriboColectivo, latParadaActualColectivo, lngParadaActualColectivo, fechaParadaActualString;
    Double latParadaPasajero;
    Double lngParadaPasajero;
    List<Marker> markers;

    ScannerQRCodeInterface.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        presenter = new ScannerQRCodePresenter(this, this);

        setContentView(R.layout.activity_arribo_colectivo);
//        paradas = getIntent().getParcelableArrayListExtra("key");

        // para volver a consultar
        // tambien para traer la ubicacion del pasajero. crear metodo para consultar
        idLinea = getIntent().getExtras().getString("idLinea");
        idRecorrido = getIntent().getExtras().getString("idRecorrido");
        idParada = getIntent().getExtras().getString("idParada");
        arriboColectivo = getIntent().getExtras().getString("arriboColectivo");
        latParadaActualColectivo = getIntent().getExtras().getString("latParadaActualColectivo");
        lngParadaActualColectivo = getIntent().getExtras().getString("lngParadaActualColectivo");
        fechaParadaActualString = getIntent().getExtras().getString("fechaParadaActualString");

//        Toast.makeText(this, "datos recuperados" + idLinea +" "+ idRecorrido +" "+ idParada +" "+ arriboColectivo, Toast.LENGTH_SHORT).show();
        presenter.makeRequestGetUbicacionParadaRecorrido(idLinea,idRecorrido,idParada);


        TvTiempoArriboCole = findViewById(R.id.tv_tiempoArriboColectivo);
        TvTiempoArriboCole.setText("Tiempo arribo colectivo: " + arriboColectivo);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(fechaParadaActualString));
        DateFormat formateador= new SimpleDateFormat("dd/M/yy hh:mm");

        TvFechaParadaActual = findViewById(R.id.tv_fechaParadaActual);
        TvFechaParadaActual.setText("Fecha parada actual cole: " + formateador.format(cal.getTime()));

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

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        googleMap.clear();
        mMap.clear();

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // hay mapa satelital, normal, de terreno o sin capas
        mMap.clear();

        UiSettings uiSettings = mMap.getUiSettings();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng ubicacionParadaActualColectivo = new LatLng(Double.parseDouble(latParadaActualColectivo), Double.parseDouble(lngParadaActualColectivo)); // marcador por defecto
        mMap.addMarker(new MarkerOptions().position(ubicacionParadaActualColectivo).title("Parada actual colectivo").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
        float zoomLevel = 15;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionParadaActualColectivo,zoomLevel)); // para agregarle zoom
    }


    public void atras(View view){
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
    public void showArriboColectivo(String tiempoArriboColectivo, String latParadaActualColectivo, String lngParadaActualColectivo, String fechaParadaActualString) {
        // aca tengo que mostrar los datos nuevos!!
    }

    @Override
    public void showUbicacionParadaPasajero(String ubicacionParadaPasajero) {
        System.out.println("informacion obtenerUbicacionParadaRecorrido en arribo cole activity: " + ubicacionParadaPasajero);
        latParadaPasajero = Double.valueOf(ubicacionParadaPasajero.split(",")[0]);
        lngParadaPasajero = Double.valueOf(ubicacionParadaPasajero.split(",")[1]);

        LatLng latlngubicacionParadaPasajero = new LatLng(latParadaPasajero, lngParadaPasajero); // marcador por defecto
//            Marker marcador = mMap.addMarker(new MarkerOptions().position(ubicacionParadaPasajero).title(item.getDireccion()).snippet("Distancia: " + item.getDistancia() +" metros").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
        Marker marcador = mMap.addMarker(new MarkerOptions().position(latlngubicacionParadaPasajero).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.man_img)));
        markers.add(marcador);


//        Toast.makeText(this, "coordenadas ubicacion pasajero! " + latParadaPasajero + " - " + lngParadaPasajero, Toast.LENGTH_LONG).show();


    }
}
