package com.mauro.figurettisapp.rest;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by usuario on 1/9/2017.
 */

public class ApiClient {
    public static final String BASE_URL_FIGURETTIS = "http://192.168.1.4:80/Figurettis/";
    private static Retrofit retrofit = null;
    /*private String ipServer;
    private String portServer;*/

    public void ApiClient(){

    }

    public static Retrofit getClient(){
        /*Properties p = new Properties();
        try {
            p.load(new FileReader("C:/Figurettis_conf/FigurettisConf.properties"));
            String ipServer = p.getProperty("ipServer");
            String portServer = p.getProperty("portServer");
            Log.e("Properties: ", ipServer + " " + portServer);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_FIGURETTIS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;
    }
}
