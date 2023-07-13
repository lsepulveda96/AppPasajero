package com.example.lucianodsepulveda.apppasajero.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;
import com.example.lucianodsepulveda.apppasajero.presenter.ScannerQRCodePresenter;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Map;

public class ScannerQRCodeActivity extends FragmentActivity implements ScannerQRCodeInterface.View {

    private SurfaceView surfaceView;
    private TextView txtBarcodeValue, tvNetwork;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private Button btnFav, btnAtras;
    private boolean isEmail = false, scannerIniciado = false;
    private String dataC, idLineaQr, idParadaQr, denomQr, direccionQr, responseArriboColectivo = "", intentData = "";
    private int control;
    private ProgressDialog dialog2;


    ScannerQRCodeInterface.Presenter presenter;


    //necesario para comprobar internet en tiempo real
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                checkStatus();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };


    //necesario para comprobar internet en tiempo real
    private void checkStatus() throws IOException {


        NetworkInfo activeNetwork = presenter.isNetAvailable();
        if (null != activeNetwork) {

            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    Toast.makeText(getApplicationContext(), "wifi encenidido", Toast.LENGTH_SHORT).show();
                    tvNetwork.setVisibility(View.GONE);

//                    if (ActivityCompat.checkSelfPermission(ScannerQRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
//                        cameraSource.start(surfaceView.getHolder());
                    //para volver a activar la camara
                    if(scannerIniciado)
                        onResume();
                    surfaceView.setVisibility(View.VISIBLE);
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    Toast.makeText(getApplicationContext(), "mobile encenidido", Toast.LENGTH_SHORT).show();
                    tvNetwork.setVisibility(View.GONE);

//                    if (ActivityCompat.checkSelfPermission(ScannerQRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
//                        cameraSource.start(surfaceView.getHolder());
                    //para volver a activar la camara
                    if(scannerIniciado)
                        onResume();
                    surfaceView.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            tvNetwork.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "internet apagado, debe conectarse a una red para continuar", Toast.LENGTH_SHORT).show();
            surfaceView.setActivated(false);
//          setVisibility(View.GONE);
//            cameraSource.stop(); // para no permitirle escanear cuando no haya internet
            //para pausar camara
            if(scannerIniciado)
                onPause();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        presenter = new ScannerQRCodePresenter(this, this);
        initViews();


    }

    private void initViews() {
        tvNetwork = (TextView) findViewById(R.id.tv_network);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnFav = findViewById(R.id.btnFav);
        btnAtras = findViewById(R.id.btnAtras);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ParadasFavoritasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Codigos", Context.MODE_PRIVATE);
                boolean bandera = true;

                Map<String, ?> allEntries = preferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    if (getData().equals(entry.getValue().toString())) {
                        bandera = false;
                    }
                }

                if (bandera) {
                    SharedPreferences.Editor myEditor = preferences.edit();
                    myEditor.putString(getData(), getData()); // para que guarde el mismo valor como clave ( que sea unica );
                    myEditor.commit();
                    Toast t5 = Toast.makeText(getApplicationContext(), "Guardado con exito!", Toast.LENGTH_SHORT);
                    t5.show();
                } else {
                    Toast t6 = Toast.makeText(getApplicationContext(), "El codigo ya existe!", Toast.LENGTH_SHORT);
                    t6.show();
                }

            }
        });


        //necesario para comprobar internet en tiempo real
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
        //
    }

    private void initialiseDetectorsAndSources() {

        //para saber si fue iniciado por primera vez el scanner, para utilizar el onPause de la camara
        scannerIniciado = true;

        Toast.makeText(getApplicationContext(), "Scanner Codigo QR iniciado", Toast.LENGTH_SHORT).show();
        control = 0;

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannerQRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannerQRCodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

//                    if (ActivityCompat.checkSelfPermission(ScannerQRCodeActivity.this, Manifest.permission.CAMERA)
//                            == PackageManager.PERMISSION_DENIED) {
//                        cameraSource.start(surfaceView.getHolder());
//                    }else {
//                        ActivityCompat.requestPermissions(ScannerQRCodeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        //reveer funcion, borrar cosas que se utilizaban en la funcion de ejemplo

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "Scanner Codigo QR detenido", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                            } else {
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;

                                setData(intentData);

                                if(control == 0) {

                                    String codigo = getData();
                                    String[] nuevoc = codigo.split(",");
                                    for(int i=0;i<nuevoc.length;i++) {
                                        if (i == 0) {
                                            idLineaQr = nuevoc[i];
                                        }
                                        if (i == 1) {
                                            denomQr = nuevoc[i];
                                        }
                                        if (i == 2) {
                                            idParadaQr = nuevoc[i];
                                        }
                                        if (i == 3) {
                                            direccionQr = nuevoc[i];
                                        }
                                    }

                                    final String codShow = denomQr + " - "  + direccionQr;
                                    txtBarcodeValue.setText(codShow);

                                    responseArriboColectivo = presenter.makeRequestLlegadaCole( idLineaQr,idParadaQr );

                                    dialog2 = new ProgressDialog( ScannerQRCodeActivity.this );
                                    dialog2.setTitle( "Codigo detectado" );
                                    dialog2.setMessage( "Leyendo información.." );
                                    dialog2.show();


                                    final Handler handler = new Handler();
                                    final Runnable r = new Runnable(){
                                        public void run(){

                                            dialog2.cancel();
                                            String resp1 = "";
                                            resp1 = getResponseArriboColectivo().replaceAll( "\"","" );

                                            if(resp1.equals("")) {
                                                Toast t2 = Toast.makeText( getApplicationContext(), "No es posible realizar la consulta", Toast.LENGTH_SHORT );
                                                t2.show();
                                            }else{

                                                AlertDialog.Builder builder = new AlertDialog.Builder( ScannerQRCodeActivity.this );
                                                builder.setTitle( "Arribo proximo colectivo:" );
                                                builder.setMessage( resp1 );
                                                builder.setPositiveButton( "Guardar codigo", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        //TODO reemplaza boton fav
                                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences( "Codigos", Context.MODE_PRIVATE );
                                                        boolean codigoNoExiste = true;

                                                        Map<String, ?> allEntries = preferences.getAll();
                                                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                                                            if (codShow.equals( entry.getKey() )) {
                                                                codigoNoExiste = false;
                                                            }
                                                        }

                                                        // porque no habia otro codigo igual
                                                        if (codigoNoExiste) {
                                                            SharedPreferences.Editor myEditor = preferences.edit();
                                                            myEditor.putString( codShow, getData() );
                                                            myEditor.commit();
                                                            Toast t5 = Toast.makeText( getApplicationContext(), "Guardado con exito!", Toast.LENGTH_SHORT );
                                                            t5.show();
                                                        } else {
                                                            Toast t6 = Toast.makeText( getApplicationContext(), "El codigo ya existe!", Toast.LENGTH_SHORT );
                                                            t6.show();
                                                        }

                                                        finish();
                                                        //TODO fin boton fav
                                                    }
                                                } );

                                                builder.setNegativeButton( "Salir", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                } );

                                                Dialog dialog = builder.create();
                                                dialog.show();
                                                //TODO fin cartel
                                            }
                                        }
                                    };
                                    handler.postDelayed(r,4000);
                                }
                                control++;
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void setData(String data) {
        this.dataC = data;
    }

    public String getData(){
        return this.dataC;
    }

    public String getResponseArriboColectivo(){
        return this.responseArriboColectivo;
    }

}
