package com.mauro.figurettisapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by usuario on 2/11/2017.
 */

public class Usuario {
    @SerializedName("nickname")
    private String nickname;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("contacto")
    private String contacto;

    @SerializedName("fotoPerfil")
    private String fotoPerfil;

    @SerializedName("contrasenia")
    private String contrasenia;

    @SerializedName("aplicacion")
    private String aplicacion;

    public Usuario(String nickname, String nombre, String apellido, String contacto, String fotoPerfil, String contrasenia, String aplicacion) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contacto = contacto;
        this.fotoPerfil = fotoPerfil;
        this.contrasenia = contrasenia;
        this.aplicacion = aplicacion;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }
}
