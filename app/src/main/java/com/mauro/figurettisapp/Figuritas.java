package com.mauro.figurettisapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mauro.figurettisapp.model.Usuario;
import com.mauro.figurettisapp.rest.ApiClient;
import com.mauro.figurettisapp.rest.ApiInterface;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.mauro.figurettisapp.rest.*;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Figuritas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String BASE_URL_FIGURETTIS = "http://192.168.1.4:80/Figurettis/";
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private String session;

    //private RelativeLayout imagenPerfil;
    private TextView txtNombreSession;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figuritas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*URL url = null;
        try {
            url = new URL(BASE_URL_FIGURETTIS);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection(10000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //HttpUrlConnection conn = (HttpURLConnection) url.openConnection();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //imagenPerfil = (RelativeLayout) findViewById(R.id.layoutImagenPerfil);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        session = sharedPref.getString("session", "");
        Call<Usuario> callData = apiService.getSessionData(session.toString());
        callData.enqueue(new Callback<Usuario>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                CircularImageView imgPerfil = (CircularImageView) findViewById(R.id.imgFotoPerfil);
                String rutaFotoPerfil = "";
                if (response.body().getAplicacion().equals("figurettis"))
                    rutaFotoPerfil = rutaFotoPerfil +  BASE_URL_FIGURETTIS;
                rutaFotoPerfil = rutaFotoPerfil + response.body().getFotoPerfil();
                Glide.with(getApplicationContext()).load(rutaFotoPerfil).into(imgPerfil);
                Log.e("Login: ", response.body().getNombre() + " " + response.body().getApellido());
                txtNombreSession = (TextView) findViewById(R.id.txtNombreSession);
                txtNombreSession.setText(response.body().getNombre() + " " + response.body().getApellido());
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("Login: ", t.toString());
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logueado_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_figuritas) {
            Intent i = new Intent(this, Figuritas.class);
            startActivity(i);
        } else if (id == R.id.nav_intercambios) {

        } else if (id == R.id.nav_eventos) {

        } else if (id == R.id.nav_perfil) {

        } else if (id == R.id.nav_realidadAumentada) {
            Intent ar = getPackageManager().getLaunchIntentForPackage("com.Figurettis.FigurettisAR");
            if (ar != null) {
                getApplicationContext().startActivity(ar);
            } else {
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Figuritas Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
