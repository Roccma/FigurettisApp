package com.mauro.figurettisapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class Login extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Para Twitter
    private String keyTwitter = "BdHILX7dxIYjOOaYNemxbexop";
    private String secretTwitter = "nvkj1a30bVcjOgRL2ZKWeJds2gShyjiVNsHx4ogL3v7qUjfZFM";

    //Para Facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private EditText txtNickname;
    private EditText txtContrasenia;
    private Button btnAceptar;
    private LoginButton btnFacebook;
    private TwitterLoginButton btnTwitter;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig twitterAuthConfig = new TwitterAuthConfig(keyTwitter, secretTwitter);
        Fabric.with(this, new Twitter(twitterAuthConfig));
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callbackManager = CallbackManager.Factory.create();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        txtNickname = (EditText) findViewById(R.id.txtNickname);
        txtContrasenia = (EditText) findViewById(R.id.txtContrasenia);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        //btnTwitter = (Button) findViewById(R.id.btnTwitter);
        btnTwitter = (TwitterLoginButton) findViewById(R.id.btnTwitter);
        Button btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNickname.getText().toString().isEmpty()){
                    txtNickname.setError("No se ha ingresado nickname");
                    txtNickname.requestFocus();
                    return;
                }

                if(txtContrasenia.getText().toString().isEmpty()){
                    txtContrasenia.setError("No se ha ingresado contraseña");
                    txtContrasenia.requestFocus();
                    return;
                }
            }
        });

        btnTwitter.setText("INICIAR SESIÓN CON TWITTER");
        btnTwitter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btnTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                /*nombre.setText(result.data.getUserName().toString());
                String userid = result.data.getUserId() + "";
                iniciarSesion.setVisibility(View.INVISIBLE);
                cerrarSesion.setVisibility(View.VISIBLE);*/
            }

            @Override
            public void failure(TwitterException exception) {
                /*new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(exception.getLocalizedMessage())
                        .setNeutralButton("Aceptar", null)
                        .show();*/
            }
        });

        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                //datos(profile);
                accessTokenTracker = new AccessTokenTracker() {

                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                    }
                };

                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        //datos(currentProfile);
                        Toast.makeText(getApplicationContext(), "Logueado como: " + currentProfile.getName(), Toast.LENGTH_SHORT).show();
                    }
                };

                accessTokenTracker.startTracking();
                profileTracker.startTracking();

                btnFacebook.setReadPermissions("user_friends");
                btnFacebook.setReadPermissions("public_profile");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_inicio) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_iniciarSesion) {
            Intent i = new Intent(Login.this, Login.class);
            startActivity(i);
        } else if (id == R.id.nav_registrarse) {

        } else if (id == R.id.nav_realidadAumentada) {

        }  else if (id == R.id.nav_ayuda) {

        } else if (id == R.id.nav_salir) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this)
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
}
