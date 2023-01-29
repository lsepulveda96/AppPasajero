package com.example.lucianodsepulveda.apppasajero.utilities;

import com.example.lucianodsepulveda.apppasajero.interfaces.MainInterface;

public class MyModel implements MainInterface.Model{

    private double resultado;
    //esta en contacto con el presenter y no con la vista
    private MainInterface.Presenter presenter;
    public MyModel(MainInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    //TODO 3 -metodo que contiene la logica, negocio
    @Override
    public void alCuadrado(String data) {
        // aca iria a buscar datos a la db o resultado de la api
        resultado = Double.valueOf(data)*Double.valueOf(data);
        //llamo al presenter para enviar el resultado devuelta
        //TODO 4 -cuando tiene el dato no se lo envia directo a la vista, pasa devuelta por el presenter
        presenter.showResultPresenter(String.valueOf(resultado));
    }

}
