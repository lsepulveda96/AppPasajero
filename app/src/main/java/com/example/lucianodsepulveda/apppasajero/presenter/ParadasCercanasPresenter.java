package com.example.lucianodsepulveda.apppasajero.presenter;

import android.app.Activity;
import android.content.Context;

import com.example.lucianodsepulveda.apppasajero.interfaces.ParadasCercanasInterface;
import com.example.lucianodsepulveda.apppasajero.utilities.ParadasCercanasModel;

public class ParadasCercanasPresenter implements ParadasCercanasInterface.Presenter {
    //esta en contacto con el modelo y con la vista
    //interface
    private ParadasCercanasInterface.View view;
    //model
    private ParadasCercanasInterface.Model model;

    public ParadasCercanasPresenter(ParadasCercanasInterface.View view, Context mContext, Activity mActivity){
        this.view = view;
        //nuevo model
        model = new ParadasCercanasModel(this, mContext, mActivity);
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
    public void obtenerPermisos() {
        model.obtenerPermisos();
    }
}
