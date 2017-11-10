package com.mauro.figurettisapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.CookieManager;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.mauro.figurettisapp.adapter.PublicacionesAdapter;
import com.mauro.figurettisapp.model.Publicaciones;
import com.mauro.figurettisapp.model.PublicacionesResponse;
import com.mauro.figurettisapp.rest.ApiClient;
import com.mauro.figurettisapp.rest.ApiInterface;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Guille on 10/11/2017.
 */

public class PublicacionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = PublicacionesActivity.class.getSimpleName();
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicaciones);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.publicaciones_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<PublicacionesResponse> callData = apiService.getAllPublicaciones();
        callData.enqueue(new Callback<PublicacionesResponse>(){
            @Override
            public void onResponse(Call<PublicacionesResponse> call, Response<PublicacionesResponse> response){
                int statusCode = response.code();
                final List<Publicaciones> publicaciones = response.body().getResults();

                recyclerView.setAdapter(new PublicacionesAdapter(publicaciones, R.layout.list_item_publicacion, getApplicationContext()));
                Log.d(TAG, "Number of publicaciones received: " + publicaciones.size());
            }

            @Override
            public void onFailure(Call<PublicacionesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //return NoLogueadoMenu.onNavigationItemSelected(item);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_inicio) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_iniciarSesion) {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        } else if (id == R.id.nav_registrarse) {

        } else if (id == R.id.nav_realidadAumentada) {
            Intent ar = getPackageManager().getLaunchIntentForPackage("com.Figurettis.FigurettisAR");
            if (ar != null) {
                getApplicationContext().startActivity(ar);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Para ejecutar esta opción, se necesita tener instalado la aplicación 'Figurettis AR' ¿Desea descargarla?")
                        .setTitle("Aplicación no encontrada")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("http://www.mediafire.com/file/nme8m18irarkwkk/FigurettisAR.apk");
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else if (id == R.id.nav_figuritas) {
            Intent i = new Intent(this, Figuritas.class);
            startActivity(i);
        } else if (id == R.id.nav_intercambios) {

        } else if (id == R.id.nav_eventos) {

        } else if (id == R.id.nav_perfil) {

        } else if (id == R.id.nav_notificaciones) {

        } else if (id == R.id.nav_ayuda) {
            Intent i = new Intent(this, Ayuda.class);
            startActivity(i);
        } else if (id == R.id.nav_cerrar_sesion) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            String aplicacion = sharedPref.getString("aplicacion", "");
            if (aplicacion == "twitter") {
                TwitterSession twitterSession1 = TwitterCore.getInstance().getSessionManager().getActiveSession();

                if (twitterSession1 != null) {
                    CookieManager cookieManager = CookieManager.getInstance();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cookieManager.removeSessionCookies(null);
                    } else {
                        cookieManager.removeSessionCookie();
                    }

                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }
            }
            if (aplicacion == "facebook") {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }
            }
            editor.remove("session");
            editor.remove("aplicacion");
            editor.commit();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_salir) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("¿Está seguro/a de que desea salir de Figurettis?")
                    .setTitle("Confirmación")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(id == R.id.nav_publicaciones){
            Intent i = new Intent(this, PublicacionesActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}