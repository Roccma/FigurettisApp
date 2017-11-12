package com.mauro.figurettisapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.mauro.figurettisapp.adapter.PublicacionesAdapter;
import com.mauro.figurettisapp.model.Publicaciones;
import com.mauro.figurettisapp.model.PublicacionesResponse;
import com.mauro.figurettisapp.model.Usuario;
import com.mauro.figurettisapp.rest.ApiClient;
import com.mauro.figurettisapp.rest.ApiInterface;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mauro.figurettisapp.R.id.publicaciones_recycler_view;
import static com.mauro.figurettisapp.R.id.text;

/**
 * Created by Guille on 10/11/2017.
 */

public class PublicacionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = PublicacionesActivity.class.getSimpleName();
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private NavigationView nav;
    private List<Publicaciones> Allpublicaciones;
    private Integer cant = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicaciones);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.publicaciones_recycler_view);
        final TextView searchFiled = (TextView) findViewById(R.id.search_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<PublicacionesResponse> callData = apiService.getAllPublicaciones();
        callData.enqueue(new Callback<PublicacionesResponse>(){
            @Override
            public void onResponse(Call<PublicacionesResponse> call, Response<PublicacionesResponse> response){
                int statusCode = response.code();
               Allpublicaciones = response.body().getResults();

                recyclerView.setAdapter(new PublicacionesAdapter(Allpublicaciones, R.layout.list_item_publicacion, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<PublicacionesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        searchFiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

        public void filter(String text){
            if (cant == 2){
                Log.e("asdas","asdasds");
            }
            text = text.toLowerCase();
            List<Publicaciones> temp = new ArrayList();
            for(Publicaciones d: Allpublicaciones){
                if(d.getNombre().toLowerCase().contains(text)){
                    temp.add(d);
                }
            }
            PublicacionesAdapter publicacionesAdapter = new PublicacionesAdapter(temp, R.layout.list_item_publicacion, getApplicationContext());

            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.publicaciones_recycler_view);
            recyclerView.setAdapter(publicacionesAdapter);
//            publicacionesAdapter.updateList(temp, R.layout.list_item_publicacion, getApplicationContext());
//            publicacionesAdapter.updateList(temp);
            cant ++;
        }

//        //////////////
//        nav = (NavigationView) findViewById(R.id.nav_view);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String session = sharedPref.getString("session", "no");
//        //Toast.makeText(getApplicationContext(), "Session: " + session, Toast.LENGTH_SHORT).show();
//        if(!session.equals("no")){
//            nav.inflateHeaderView(R.layout.nav_header_logueado_menu);
//            nav.inflateMenu(R.menu.activity_logueado_menu_drawer);
//            cargarDatosMenu();
//        }
//        else{
//            nav.inflateHeaderView(R.layout.nav_header_no_logueado_menu);
//            nav.inflateMenu(R.menu.activity_no_logueado_menu_drawer);
//        }
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(PublicacionesActivity.this);
//        ////////////
//    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String session = sharedPref.getString("session", "no");
//        //Toast.makeText(getApplicationContext(), "Session: " + session, Toast.LENGTH_SHORT).show();
//        if(!session.equals("no"))
//            getMenuInflater().inflate(R.menu.logueado_menu, menu);
//        else
//            getMenuInflater().inflate(R.menu.no_logueado_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

//    public void cargarDatosMenu(){
//        String session;
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        session = sharedPref.getString("session", "");
//        Call<Usuario> callData = apiService.getSessionData(session.toString());
//        callData.enqueue(new Callback<Usuario>() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
//                String base_url_figurettis = "http://192.168.1.106:80/Figurettis/";
//                TextView txtNombreSession;
//                CircularImageView imgPerfil = (CircularImageView) findViewById(R.id.imgFotoPerfil);
//                String rutaFotoPerfil = "";
//                if (response.body().getAplicacion().equals("figurettis"))
//                    rutaFotoPerfil = rutaFotoPerfil +  base_url_figurettis;
//                rutaFotoPerfil = rutaFotoPerfil + response.body().getFotoPerfil();
//                Glide.with(getApplicationContext()).load(rutaFotoPerfil).into(imgPerfil);
//                Log.e("Login: ", response.body().getNombre() + " " + response.body().getApellido());
//                txtNombreSession = (TextView) findViewById(R.id.txtNombreSession);
//                txtNombreSession.setText(response.body().getNombre() + " " + response.body().getApellido());
//            }
//
//            @Override
//            public void onFailure(Call<Usuario> call, Throwable t) {
//                Log.e("Login: ", t.toString());
//            }
//        });
//    }
}
