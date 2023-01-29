package com.example.lucianodsepulveda.apppasajero.interfaces;

public interface ParadasFavoritasInterface {

    interface View{
        // la vista muestra el dato una vez calculado
        void showResult(String result);
    }

    interface Presenter{
        //se comunica con la vista, necesita el mismo metodo
        void showResultPresenter(String result);
        //se comunica con el modelo, se ejecuta en el presenter
        public boolean isNetAvailable();
        boolean isOnlineNet();
    }

    interface Model{
        //mismo metodo para el modelo, se ejecuta en el model
//        void alCuadrado(String data);
        public boolean isNetAvailable();
        public boolean isOnlineNet();
    }


}
