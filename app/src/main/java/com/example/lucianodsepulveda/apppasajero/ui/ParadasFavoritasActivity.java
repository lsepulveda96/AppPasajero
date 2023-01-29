package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;
import com.example.lucianodsepulveda.apppasajero.presenter.ParadasFavoritasPresenter;
import com.example.lucianodsepulveda.apppasajero.utilities.Parada;
import com.example.lucianodsepulveda.apppasajero.utilities.RecyclerViewAdapter;
import com.example.lucianodsepulveda.apppasajero.utilities.ScannerQRCodeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParadasFavoritasActivity extends FragmentActivity implements View.OnClickListener, ParadasFavoritasInterface.View {

    //cambiar nombres variables
    Button btnScanBarcode;
    ListView listaParadasAdapter;
    ArrayAdapter<String> arrayAdapter;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNet;
    TextView tvAccess;

    List<Parada> listaParadas = new ArrayList<Parada>();

    //para interface
    ParadasFavoritasInterface.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParadasFavoritasPresenter(this, this);
        setContentView(R.layout.activity_paradas_favoritas);
        initViews();
    }


    public void initViews() {
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnScanBarcode.setOnClickListener(this);
        listaParadasAdapter = (ListView) findViewById(R.id.lvFav);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tvNet = (TextView)findViewById(R.id.tv_net);
        tvAccess = (TextView)findViewById(R.id.tv_access);

        // Consulta la disponiblidad de la red
        if(presenter.isOnlineNet()) {
            tvNet.setText( "Conectado a internet" );
        }else{
            tvNet.setText( "Sin conexion a internet" );
        }

        // Consulta la disponiblidad de la red
        if(presenter.isNetAvailable()) {
            tvAccess.setText( "Red habilitada" );
        }else{
            tvAccess.setText("Red deshabilitada");
        }

        //idea -- List<Parada> listaParadas = getListaParadas();
        sharedPreferences = getApplicationContext().getSharedPreferences("Codigos", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : allEntries.entrySet()){
            arrayAdapter.add(entry.getValue().toString());
            listaParadas.add(new Parada(entry.getKey()));
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final MainFragment fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_fragment) ;

        recyclerViewAdapter = new RecyclerViewAdapter(listaParadas,this,fragment);
        recyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            // Metodo para mover elementos de la lista arriba y abajo
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //Para eliminar parada de lista de favoritos
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ParadasFavoritasActivity.this);
                builder.setTitle("Eliminar codigo Qr");
                builder.setMessage("Esta seguro que desea eliminar el codigo Qr de favoritos?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    //esto tiene que ir en model
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // elminarPos(pos, listaParadas,sharedPreferences,viewHolder)
                        int pos = viewHolder.getAdapterPosition();
                        Parada aux = listaParadas.get(pos);
                        SharedPreferences.Editor myEditor = sharedPreferences.edit();
                        myEditor.remove(aux.getCodigo());
                        myEditor.commit();
                        listaParadas.remove(pos);
                        recyclerViewAdapter.notifyDataSetChanged();
                        Toast t10 = Toast.makeText(getApplicationContext(),aux.getCodigo()+" eliminado!",Toast.LENGTH_SHORT);
                        t10.show();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        listaParadasAdapter.setAdapter(arrayAdapter);
    }


    //este metodo queda, porque solo llama a otro acitiviy
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnScanBarcode:
                startActivity(new Intent(ParadasFavoritasActivity.this, ScannerQRCodeActivity.class));
                finish();
                break;
        }
    }

    public List<Parada> getListaParadas() {
        return listaParadas;
    }

    public void setListaParadas(List<Parada> listaParadas) {
        this.listaParadas = listaParadas;
    }

    @Override
    public void showResult(String result) {

    }
}
