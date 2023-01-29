package com.example.lucianodsepulveda.apppasajero.presenter;

import com.example.lucianodsepulveda.apppasajero.interfaces.MainInterface;
import com.example.lucianodsepulveda.apppasajero.utilities.MyModel;

// Clase que actua como presenter entre View y Model
public class AppPresenter implements MainInterface.Presenter {

    //esta en contacto con el modelo y con la vista
    //interface
    private MainInterface.View view;
    //model
    private MainInterface.Model model;

    public AppPresenter(MainInterface.View view){
        this.view = view;
        //nuevo model
        model = new MyModel(this);
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
    public void alCuadrado(String data) {
        //este es cuando envia el dato que necesita
        if(view != null){
            model.alCuadrado(data);
        }
    }
}
