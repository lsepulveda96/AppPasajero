package com.example.lucianodsepulveda.apppasajero.model;

public class Parada {
    private String codigo;

    public Parada() {
    }

    public Parada(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
