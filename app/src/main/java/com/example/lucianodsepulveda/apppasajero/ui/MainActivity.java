package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lucianodsepulveda.apppasajero.R;

public class MainActivity extends Activity {

    LinearLayout btnParadasCercanas;
    LinearLayout btnScannerQR;
    LinearLayout btnFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(getActionBar()!=null)
//            this.getActionBar().hide();
        setContentView(R.layout.activity_main);

        btnParadasCercanas = findViewById( R.id.btnParadasCercanas);
        btnFavoritos = findViewById( R.id.btnFavoritos);
        btnScannerQR = findViewById( R.id.btnScannerQR);

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
