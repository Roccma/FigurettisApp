package com.mauro.figurettisapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.mauro.figurettisapp.model.Usuario;
import com.mauro.figurettisapp.rest.ApiClient;
import com.mauro.figurettisapp.rest.ApiInterface;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.Figurettis.FigurettisAR.UnityPlayerActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtPaso1;
    private TextView txtPaso2;
    private TextView txtPaso3;
    private Button btnComenzar;
    private NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        txtPaso1 = (TextView) findViewById(R.id.txtPaso1);
        txtPaso2 = (TextView) findViewById(R.id.txtPaso2);
        txtPaso3 = (TextView) findViewById(R.id.txtPaso3);
        Button btnComenzar = (Button) findViewById(R.id.btnComenzar);

        txtPaso1.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.icono_paso_1_landing, 0, 0);
        txtPaso2.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.icono_paso_2_landing, 0, 0);
        txtPaso3.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.icono_paso_3_landing, 0, 0);

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });
        nav = (NavigationView) findViewById(R.id.nav_view);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String session = sharedPref.getString("session", "no");
        //Toast.makeText(getApplicationContext(), "Session: " + session, Toast.LENGTH_SHORT).show();
        if(!session.equals("no")){
            nav.inflateHeaderView(R.layout.nav_header_logueado_menu);
            nav.inflateMenu(R.menu.activity_logueado_menu_drawer);
            cargarDatosMenu();
        }
        else{
            nav.inflateHeaderView(R.layout.nav_header_no_logueado_menu);
            nav.inflateMenu(R.menu.activity_no_logueado_menu_drawer);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String session = sharedPref.getString("session", "no");
        //Toast.makeText(getApplicationContext(), "Session: " + session, Toast.LENGTH_SHORT).show();
        if(!session.equals("no"))
            getMenuInflater().inflate(R.menu.logueado_menu, menu);
        else
            getMenuInflater().inflate(R.menu.no_logueado_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

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
            /*Intent ar = getPackageManager().getLaunchIntentForPackage("com.Figurettis.FigurettisAR");
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
            }*/
            Intent i = new Intent(this, UnityPlayerActivity.class);
            startActivity(i);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargarDatosMenu(){
        String session;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        session = sharedPref.getString("session", "");
        Call<Usuario> callData = apiService.getSessionData(session.toString());
        callData.enqueue(new Callback<Usuario>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                String base_url_figurettis = "http://192.168.10.129:8080/Figurettis/";
                TextView txtNombreSession;
                CircularImageView imgPerfil = (CircularImageView) findViewById(R.id.imgFotoPerfil);
                String rutaFotoPerfil = "";
                if (response.body().getAplicacion().equals("figurettis"))
                    rutaFotoPerfil = rutaFotoPerfil +  base_url_figurettis;
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
    }
}
