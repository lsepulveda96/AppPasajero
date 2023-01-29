package com.example.lucianodsepulveda.apppasajero.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class ParadaCercana implements Parcelable {
    private double latitud;
    private double longitud;
    private String distancia;
    private String direccion;
    private String lineaDenom;

    public ParadaCercana(){}

    public ParadaCercana( double latitud, double longitud ) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    protected ParadaCercana(Parcel in) {
        latitud = in.readDouble();
        longitud = in.readDouble();
        distancia = in.readString();
        direccion = in.readString();
        lineaDenom = in.readString();
    }

    public static final Creator<ParadaCercana> CREATOR = new Creator<ParadaCercana>() {
        @Override
        public ParadaCercana createFromParcel(Parcel in) {
            return new ParadaCercana(in);
        }

        @Override
        public ParadaCercana[] newArray(int size) {
            return new ParadaCercana[size];
        }
    };

    public double getLatitud() { return this.latitud; }
    public void setLatitud( double l ) { this.latitud = l; }

    public double getLongitud() { return this.longitud; }
    public void setLongitud( double l ) { this.longitud = l; }

    public String getDireccion() { return direccion; }
    public void setDireccion( String direccion ) { this.direccion = direccion; }

    public String getDistancia() { return this.distancia; }
    public void setDistancia( String d ) { this.distancia = d; }

    public String getLineaDenom() { return lineaDenom; }
    public void setLineaDenom( String lineaDenom ) { this.lineaDenom = lineaDenom; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitud);
        parcel.writeDouble(longitud);
        parcel.writeString(distancia);
        parcel.writeString(direccion);
        parcel.writeString(lineaDenom);
    }
}
