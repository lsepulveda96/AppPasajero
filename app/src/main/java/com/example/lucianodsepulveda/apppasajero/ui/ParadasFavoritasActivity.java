package com.example.lucianodsepulveda.apppasajero.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;
import com.example.lucianodsepulveda.apppasajero.model.Parada;
import com.example.lucianodsepulveda.apppasajero.model.ParadaCercana;
import com.example.lucianodsepulveda.apppasajero.presenter.ParadasFavoritasPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParadasFavoritasActivity extends Activity implements ParadasFavoritasInterface.View {

    //cambiar nombres variables
    ListView listaParadasAdapter;
    ArrayAdapter<String> arrayAdapter;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNet, tvAccess, tvNetwork;
    private String responseArriboColectivo = "";
    NetworkInfo activeNetwork;

    List<Parada> listaParadas = new ArrayList<Parada>();

    //para interface
    ParadasFavoritasInterface.Presenter presenter;
    //necesario para comprobar internet en tiempo real
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    //necesario para comprobar internet en tiempo real
    private void checkStatus(){

        activeNetwork = presenter.isNetAvailable();
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
//            Toast.makeText(getApplicationContext(),"Internet apagado", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ParadasFavoritasPresenter(this, this, this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_paradas_favoritas);
        initViews();
    }


    public void initViews() {
        listaParadasAdapter = (ListView) findViewById(R.id.lvFav);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        tvNetwork = (TextView)findViewById(R.id.tv_network);

        sharedPreferences = getApplicationContext().getSharedPreferences("Codigos", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : allEntries.entrySet()){
            arrayAdapter.add(entry.getValue().toString());
            listaParadas.add(new Parada(entry.getKey()));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final MainFragment fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_fragment) ;


        recyclerViewAdapter = new RecyclerViewAdapter(listaParadas,this, fragment, responseArriboColectivo);
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

        //necesario para comprobar internet en tiempo real
        IntentFilter intentFilter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        //
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

    @Override
    public void showArriboColectivo(String fechaParadaActualString, String tiempoArriboColProximoString, String latParadaActualColectivo, String lngParadaActualColectivo, String latParadaActualPasajero, String lngParadaActualPasajero, String paradaActualColeDire, String codigoError, List<ParadaCercana> paradasPorRecorrerList, String paradaActualPasajeroDire) {

        System.out.println("informacion: devolvio resultado en activity y debo pasarlo al view holder show arribo: " + tiempoArriboColProximoString);
        SharedPreferences sharedPreferencesArriboCole;

        sharedPreferencesArriboCole = getApplicationContext().getSharedPreferences("DataArriboCole", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferencesArriboCole.edit();
        myEditor.putString("TiempoArribo", tiempoArriboColProximoString);
        myEditor.putString("codigoError", codigoError);
        myEditor.putString("fechaParadaActual", fechaParadaActualString);
        myEditor.putString("latParadaActualColectivo", latParadaActualColectivo);
        myEditor.putString("lngParadaActualColectivo", lngParadaActualColectivo);
        myEditor.putString("latParadaActualPasajero", latParadaActualPasajero);
        myEditor.putString("lngParadaActualPasajero", lngParadaActualPasajero);
        myEditor.putString("paradaActualColeDire", paradaActualColeDire);
        myEditor.putString("paradaActualPasajeroDire", paradaActualPasajeroDire);

        if(paradasPorRecorrerList != null){
            Set<String> set = new HashSet<String>();
            for (ParadaCercana parada: paradasPorRecorrerList) {
                set.add(parada.getLatitud() + "," + parada.getLongitud()+ "," + parada.getDireccion());
            }
            myEditor.putStringSet("paradasPorRecorrerList", set);
        }

        myEditor.commit();
        recyclerViewAdapter.notifyDataSetChanged();

        responseArriboColectivo = tiempoArriboColProximoString;
    }

    @Override
    public void showMsajeSinColectivos(String responseTiempoArriboColectivo, String codigoError) {
        // recibo los datos cuando hay codigo 400 y envio a recycler view!
        System.out.println("informacion: devolvio resultado en activity y debo pasarlo al view holder sin colectivos: " + responseTiempoArriboColectivo );
        SharedPreferences sharedPreferencesArriboCole;

        sharedPreferencesArriboCole = getApplicationContext().getSharedPreferences("DataArriboCole", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferencesArriboCole.edit();
        myEditor.putString("TiempoArribo", responseTiempoArriboColectivo);
        myEditor.putString("codigoError", codigoError);
        // aca agregar los otros valores para leerlos dentro del recyvler view
        myEditor.commit();
        recyclerViewAdapter.notifyDataSetChanged();

        responseArriboColectivo = responseTiempoArriboColectivo;
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


}
