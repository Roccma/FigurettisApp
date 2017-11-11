package com.mauro.figurettisapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Guille on 10/11/2017.
 */

public class Publicaciones {
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("imagen")
    private String imagen;
    @SerializedName("numero")
    private Integer numero;
    @SerializedName("fechaHora")
    private String fechaHora;
    @SerializedName("cantidad")
    private Integer cantidad;
    @SerializedName("bandera")
    private String bandera;

    public Publicaciones(String nombre, String imagen, Integer numero, String fechaHora, Integer cantidad, String bandera) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.numero = numero;
        this.fechaHora = fechaHora;
        this.cantidad = cantidad;
        this.bandera = bandera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getBandera() {
        return bandera;
    }

    public void setBandera(String bandera) {
        this.bandera = bandera;
    }
}

