package com.mauro.figurettisapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Guille on 10/11/2017.
 */

public class Publicaciones {
    @SerializedName("codigo")
    private Integer codigo;
    @SerializedName("nicknameUsuario")
    private String nicknameUsuario;
    @SerializedName("numeroFigurita")
    private Integer numeroFigurita;
    @SerializedName("fechaHora")
    private String fechaHora;
    @SerializedName("cantidad")
    private Integer cantidad;
    @SerializedName("estado")
    private Integer estado;

    public Publicaciones(Integer codigo, String nicknameUsuario, Integer numeroFigurita, String fechaHora, Integer cantidad, Integer estado){
        this.codigo = codigo;
        this.nicknameUsuario = nicknameUsuario;
        this.numeroFigurita = numeroFigurita;
        this.fechaHora = fechaHora;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNicknameUsuario() {
        return nicknameUsuario;
    }

    public void setNicknameUsuario(String nicknameUsuario) {
        this.nicknameUsuario = nicknameUsuario;
    }

    public Integer getNumeroFigurita() {
        return numeroFigurita;
    }

    public void setNumeroFigurita(Integer numeroFigurita) {
        this.numeroFigurita = numeroFigurita;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

}
