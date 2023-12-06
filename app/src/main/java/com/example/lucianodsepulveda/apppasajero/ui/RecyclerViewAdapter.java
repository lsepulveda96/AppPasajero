package com.example.lucianodsepulveda.apppasajero.ui;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;
import com.example.lucianodsepulveda.apppasajero.model.Parada;
import com.example.lucianodsepulveda.apppasajero.presenter.ParadasFavoritasPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView txtCodigo;

        public RecyclerViewHolder(View itemView) {
        super(itemView);
        txtCodigo = (TextView) itemView.findViewById(R.id.txtCodigo);

    }
}

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ParadasFavoritasActivity paradasFavoritasActivity;
    public MainFragment mf;
    private List<Parada> listData = new ArrayList<Parada>();
    static ParadasFavoritasInterface.Presenter presenter;
    String responseArriboColectivo;


    public RecyclerViewAdapter(List<Parada> listData, ParadasFavoritasActivity paradasFavoritasAcitivity, MainFragment mainFragment, String responseArriboColectivo) {
        this.listData = listData;
        this.paradasFavoritasActivity = paradasFavoritasAcitivity;
        this.mf = mainFragment;
        this.responseArriboColectivo = responseArriboColectivo;
        presenter = new ParadasFavoritasPresenter(paradasFavoritasActivity, paradasFavoritasActivity);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final MainFragment mf;
        private final ParadasFavoritasActivity ma2;
        private String mItem;
        private TextView txtCodigo;
        //private String responseArriboColectivo = "";
        private String idLineaQr;
        private String idParadaQr;
        private String denomLineaQr;
        private String direccionParadaQr;
        private String idRecorridoQr;
        private String denomRecorridoQr;

        SharedPreferences preferences;

        SharedPreferences preferencesArriboCole;
        ProgressDialog dialog2;
        String responseArriboColectivo;
        ParadasFavoritasInterface.Presenter presenter;

        public ViewHolder(View itemView, ParadasFavoritasActivity ma, MainFragment mf2) {
            super(itemView);
            itemView.setOnClickListener(this);
            ma2 = ma;
            mf = mf2;
            txtCodigo = (TextView) itemView.findViewById(R.id.txtCodigo);
            presenter = new ParadasFavoritasPresenter(ma,ma);

        }


        public void setmItem(String item){
            mItem = item;
            txtCodigo.setText(mItem);
        }

        @Override
        public void onClick(View view){

            String codigo = mItem;
            String codRes = "";

            preferences = ma2.getSharedPreferences("Codigos", Context.MODE_PRIVATE);
            Map<String, ?> allEntries = preferences.getAll();
            for(Map.Entry<String,?> entry : allEntries.entrySet()){
                if(entry.getKey().equals( mItem )){
                   codRes = entry.getValue().toString();
                }
            }

            String[] nuevoc = codRes.split(",");

            for(int i=0;i<nuevoc.length;i++) {
                if (i == 0) {
                    idLineaQr = nuevoc[i];
                }
                if (i == 1) {
                    denomLineaQr = nuevoc[i];
                }
                if (i == 2) {
                    idParadaQr = nuevoc[i];
                }
                if (i == 3) {
                    direccionParadaQr = nuevoc[i];
                }
                if (i == 4) {
                    idRecorridoQr = nuevoc[i];
                }
                if (i == 5) {
                    denomRecorridoQr = nuevoc[i];
                }
            }

            System.out.println("informacion en favoritos, datos del llamado llegada cole api: " + idLineaQr +", "+ idRecorridoQr+", "+ idParadaQr);
            // el llamado anda, pero no trae nada en responseArriboColectivo (problema de visibilidad, lo llama dentro de ese metodo con showArriboCole)
            // recibe la respuesta en paradas favoritas en el metodo showArriboCole y lo envia al recycler view con sharedPreference
            responseArriboColectivo = presenter.makeRequestLlegadaCole(idLineaQr, idRecorridoQr, idParadaQr);

            dialog2 = new ProgressDialog( ma2 );
            dialog2.setMessage( "Buscando el proximo colectivo.." );
            dialog2.show();


            crearDialogo(getPosition(), mItem);
            Log.d(TAG, "onClick" + getPosition() + " " + mItem);
            final Handler handler = new Handler();
            final Runnable r = new Runnable(){
                public void run(){

                    dialog2.cancel();

                    String codigoRespArriboCole = "";
                    String resp1 = "";

                    // obtiene el tiempo de arribo desde getSharedPreference
                    preferencesArriboCole = ma2.getSharedPreferences("TiempoArribo", Context.MODE_PRIVATE);
                    Map<String, ?> allEntries = preferencesArriboCole.getAll();
                    for(Map.Entry<String,?> entry : allEntries.entrySet()){
                        if (entry.getKey().equals("TiempoArribo"))
                            //System.out.println("La key dentro del recycler view: " + entry.getValue().toString());
                            codigoRespArriboCole = entry.getValue().toString();
                    }

                    System.out.println("informacion: la respuesta que trae la consulta tiempo de arribo desde favoritos +++++++++++++: " + codigoRespArriboCole + " fin+++");

                    //resp1 = responseArriboColectivo.replaceAll( "\"","" );
                    resp1 = codigoRespArriboCole.replaceAll( "\"","" );

                    if(resp1.equals("")) {
                        Toast t2 = Toast.makeText( ma2, "No es posible realizar la consulta", Toast.LENGTH_SHORT );
                        t2.show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ma2);
                        builder.setTitle( "Arribo proximo colectivo:" );
                        builder.setMessage( resp1 );
                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                }
            };
            handler.postDelayed(r,4000);
        }

        private void crearDialogo(int position, String mItem) {
        }

        public String getResponseArriboColectivo(){
            return this.responseArriboColectivo;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item,parent,false);
        ViewHolder vh = new ViewHolder(itemView, paradasFavoritasActivity,mf);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setmItem(listData.get(position).getCodigo());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
