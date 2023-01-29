package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.MainInterface;
import com.example.lucianodsepulveda.apppasajero.presenter.AppPresenter;
import com.example.lucianodsepulveda.apppasajero.utilities.ScannerQRCodeActivity;

public class MainActivity extends Activity implements MainInterface.View {

    Button btnParadasCercanas;
    Button btnScannerQR;
    Button btnFavoritos;

    //para usar interface
//    AppPresenter appPresenter;
    private TextView textViewMVP;
    private EditText editTextMVP;
    //para llamar a la interface
    private MainInterface.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParadasCercanas = (Button) findViewById( R.id.btnParadasCercanas);
        btnFavoritos = (Button) findViewById( R.id.btnFavoritos);
        btnScannerQR = (Button) findViewById( R.id.btnScannerQR);

        // para MVP
        textViewMVP = (TextView)findViewById(R.id.TextViewMVP);
        editTextMVP = (EditText)findViewById(R.id.EditTextMVP);


        //instancia del presenter. le envio la vista cuando lo utilice desde el presentador
        presenter = new AppPresenter(this);


        btnParadasCercanas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //para usar appPresenter
//                appPresenter.getAppName();
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

//        // prueba para patron MVC
//        public MyModel GetAppFromModel(){
//            return new MyModel("appPasajero" , 2, 900);
//        }


//        btnMVP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               calcular(view);
//            }
//        });

    }

        //esto es cuando pulsa el boton calcular
        //TODO 1 -le envia el dato que inserto el usuario - la vista se pone en contacto con el presenter. se abstrae de la implementacion
        public void calcular (View view){
            presenter.alCuadrado(editTextMVP.getText().toString());
        }

        // TODO 6 - llega el dato solicitado denuevo a la vista, donde lo muestra en pantalla
        @Override
        public void showResult(String result) {
            //muestra el dato que recibe en el result
            textViewMVP.setText(result);
            System.out.println("el resultado que trae es: "+ result);
        }
//
//    //usando la interface
//    @Override
//    public void onGetAppName(String string) {
//        //textView.setText(string);
//    }
}
