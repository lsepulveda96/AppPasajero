package com.example.lucianodsepulveda.apppasajero.interfaces;

//Interface que vincula View (activity) y el Presenter
public interface MainInterface {

    //si vamos a tener muchos mas metodos, hacemos las interfaces en clases diferentes
//    void onGetAppName(String string);
    interface View{
        // la vista muestra el dato una vez calculado
        void showResult(String result);
    }

    interface Presenter{
        //se comunica con la vista, necesita el mismo metodo
        void showResultPresenter(String result);
        //se comunica con el modelo, se ejecuta en el presenter
        void alCuadrado(String data);
    }

    interface Model{
        //mismo metodo para el modelo, se ejecuta en el model
        void alCuadrado(String data);
    }

}
