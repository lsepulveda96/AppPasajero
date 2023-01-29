package com.example.lucianodsepulveda.apppasajero.presenter;

import android.content.Context;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasFavoritasInterface;
import com.example.lucianodsepulveda.apppasajero.utilities.ParadasFavoritasModel;

public class ParadasFavoritasPresenter implements ParadasFavoritasInterface.Presenter {
    //esta en contacto con el modelo y con la vista
    //interface
    private ParadasFavoritasInterface.View view;
    //model
    private ParadasFavoritasInterface.Model model;

    public ParadasFavoritasPresenter(ParadasFavoritasInterface.View view, Context mContext){
        this.view = view;
        //nuevo model
        model = new ParadasFavoritasModel(this, mContext);
    }



    // TODO 5- recibe el resultado que viene del model, para enviarselo a la vista (activiy)
    @Override
    public void showResultPresenter(String result) {
        //llama al result de la vista
        if(view!=null){
            view.showResult(result);
        }
    }

    @Override
    //TODO 2 -luego llama a este metodo, y le pide al model el dato que necesita
    public boolean isNetAvailable() {
        boolean result = false;

        //este es cuando envia el dato que necesita
        if(view != null){
            result = model.isNetAvailable();
        }
        return result;
    }

    @Override
    public boolean isOnlineNet() {
        boolean result = false;

        //este es cuando envia el dato que necesita
        if(view != null){
            result = model.isOnlineNet();
        }
        return result;
    }

}
