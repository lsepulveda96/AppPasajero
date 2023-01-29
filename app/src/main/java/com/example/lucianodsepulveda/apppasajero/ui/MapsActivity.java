package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.ui.MainFragment;
import com.example.lucianodsepulveda.apppasajero.utilities.ParadaCercana;
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

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,GoogleMap.OnMapLongClickListener,View.OnClickListener{

    private GoogleMap mMap;
    List<ParadaCercana> paradas = new ArrayList<>();
    TextView tvParadas;
    MainFragment fragment;
    String myLat;
    String myLng;
    Button atras;
    List<Marker> markers;
    TextView tvLinea;
    String radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        paradas = getIntent().getParcelableArrayListExtra("key");
        myLat = getIntent().getExtras().getString("lat");
        myLng = getIntent().getExtras().getString("lng");
        radio = getIntent().getExtras().getString("radio");

        atras = (Button)findViewById(R.id.btnAtras);
        markers = new ArrayList<Marker>();
        tvLinea = (TextView)findViewById(R.id.tvLinea);

        tvParadas = (TextView)findViewById(R.id.tvParadas);
       // GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if(status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
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

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // hay mapa satelital, normal, de terreno o sin capas
        mMap.clear();

        UiSettings uiSettings = mMap.getUiSettings();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng miUbicacion = new LatLng(Double.parseDouble(myLat), Double.parseDouble(myLng)); // marcador por defecto

        mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.man_img)));
        float zoomLevel = 16;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion)); // original
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion,zoomLevel)); // para agregarle zoom

        for(ParadaCercana item: paradas) {
            LatLng coordenada = new LatLng(item.getLatitud(), item.getLongitud()); // marcador por defecto
            Marker marcador = mMap.addMarker(new MarkerOptions().position(coordenada).title(item.getDireccion()).snippet("Distancia: " + item.getDistancia() +" metros").icon(BitmapDescriptorFactory.fromResource(R.mipmap.parada_img)));
            markers.add(marcador);
            tvLinea.setText("Paradas " + item.getLineaDenom());
        }
    }

    public void atras(View view){
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // hay mapa satelital, normal, de terreno o sin capas
        for(Marker item: markers){
            item.setVisible(false);
            item.remove();
        }
        paradas = new ArrayList<ParadaCercana>();
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
}
