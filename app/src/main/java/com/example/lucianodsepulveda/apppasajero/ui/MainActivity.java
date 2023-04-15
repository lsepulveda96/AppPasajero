package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lucianodsepulveda.apppasajero.R;

public class MainActivity extends Activity {

    Button btnParadasCercanas;
    Button btnScannerQR;
    Button btnFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParadasCercanas = (Button) findViewById( R.id.btnParadasCercanas);
        btnFavoritos = (Button) findViewById( R.id.btnFavoritos);
        btnScannerQR = (Button) findViewById( R.id.btnScannerQR);

        btnParadasCercanas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ParadasCercanasActivity.class);
                startActivity(intent);
            }
        });

        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ParadasFavoritasActivity.class);
                startActivity(intent);
            }
        });

        btnScannerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerQRCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
