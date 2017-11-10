package com.mauro.figurettisapp.rest;

import com.mauro.figurettisapp.model.LoginResponse;
import com.mauro.figurettisapp.model.Usuario;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("usuarios.php/login")
    Call<LoginResponse> login(@Query("nickname") String nickname, @Query("contrasenia") String contrasenia);

    @GET("usuarios.php/getSessionData")
    Call<Usuario> getSessionData(@Query("nickname") String nickname);

    @GET("usuarios.php/addUserSocialNetwork")
    Call<LoginResponse> addUserSocialNetwork(@Query("nickname") String nickname, @Query("nombre") String nombre, @Query("apellido") String apellido, @Query("contacto") String contacto, @Query("fotoPerfil") String fotoPerfil, @Query("aplicacion") String aplicacion);
}
