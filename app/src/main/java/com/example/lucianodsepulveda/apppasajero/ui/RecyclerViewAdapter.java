package com.example.lucianodsepulveda.apppasajero.ui;

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

import com.example.lucianodsepulveda.apppasajero.R;
import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;
import com.example.lucianodsepulveda.apppasajero.model.Parada;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView txtCodigo;

        public RecyclerViewHolder(View itemView) {
        super(itemView);
        txtCodigo = (TextView) itemView.findViewById(R.id.txtCodigo);
    }
}

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  implements ScannerQRCodeInterface.View {

    public ParadasFavoritasActivity ma;
    public MainFragment mf;
    private List<Parada> listData = new ArrayList<Parada>();


    public RecyclerViewAdapter(List<Parada> listData, ParadasFavoritasActivity mainActivity, MainFragment mainFragment) {
        this.listData = listData;
        this.ma = mainActivity;
        this.mf = mainFragment;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final MainFragment mf;
        private final ParadasFavoritasActivity ma2;
        private String mItem;
        private TextView txtCodigo;
        private String responseArriboColectivo = "";
        private String idLineaQr;
        private String idParadaQr;
        SharedPreferences preferences;
        ProgressDialog dialog2;
        ScannerQRCodeInterface.Presenter presenter;


        public ViewHolder(View itemView, ParadasFavoritasActivity ma, MainFragment mf2) {
            super(itemView);
            itemView.setOnClickListener(this);
            ma2 = ma;
            mf = mf2;
            txtCodigo = (TextView) itemView.findViewById(R.id.txtCodigo);
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
                if (i == 2) {
                    idParadaQr = nuevoc[i];
                }
            }

            responseArriboColectivo = presenter.makeRequestLlegadaCole(idLineaQr, idParadaQr, idParadaQr);
//            respuesta = mf.makeRequestLlegadaCole( idLineaQr,idParadaQr );


            dialog2 = new ProgressDialog( ma2 );
            dialog2.setMessage( "Buscando el proximo colectivo.." );
            dialog2.show();


            crearDialogo(getPosition(), mItem);
            Log.d(TAG, "onClick" + getPosition() + " " + mItem);
            final Handler handler = new Handler();
            final Runnable r = new Runnable(){
                public void run(){

                    dialog2.cancel();
                    String resp1 = "";
                    resp1 = getResponseArriboColectivo().replaceAll( "\"","" );
//                    resp1 = mf.getRespuesta().replaceAll( "\"","" );

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
        ViewHolder vh = new ViewHolder(itemView,ma,mf);
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
