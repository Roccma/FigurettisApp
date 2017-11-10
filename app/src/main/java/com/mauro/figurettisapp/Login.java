package com.mauro.figurettisapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mauro.figurettisapp.model.LoginResponse;
import com.mauro.figurettisapp.model.Usuario;
import com.mauro.figurettisapp.rest.ApiClient;
import com.mauro.figurettisapp.rest.ApiInterface;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Response;

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

    public String nickname;

    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
                if (txtNickname.getText().toString().isEmpty()) {
                    txtNickname.setError("No se ha ingresado nickname");
                    txtNickname.requestFocus();
                    return;
                }

                if (txtContrasenia.getText().toString().isEmpty()) {
                    txtContrasenia.setError("No se ha ingresado contraseña");
                    txtContrasenia.requestFocus();
                    return;
                }


                Call<LoginResponse> call = apiService.login(txtNickname.getText().toString(), txtContrasenia.getText().toString());
                call.enqueue(new retrofit2.Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        boolean resultado = response.body().getResult();
                        if (resultado) {
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.remove("session");
                            editor.putString("session", txtNickname.getText().toString());
                            editor.putString("aplicacion", "figurettis");
                            editor.commit();
                            Intent intentFiguritas = new Intent(Login.this, Figuritas.class);
                            startActivity(intentFiguritas);
                        } else {
                            Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            txtNickname.requestFocus();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("Login: ", t.toString());
                    }
                });
            }
        });

        btnTwitter.setText("INICIAR SESIÓN CON TWITTER");
        btnTwitter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btnTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //Toast.makeText(getApplicationContext(), "Aca", Toast.LENGTH_SHORT).show();

                TwitterSession session = Twitter.getSessionManager().getActiveSession();

                Call<User> userResult=Twitter.getApiClient(session).getAccountService().verifyCredentials(true,false);
                userResult.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        TwitterSession session = Twitter.getSessionManager().getActiveSession();
                        String userid = session.getUserName().toString();
                        User user = result.data;
                        String profileImage= user.profileImageUrl;

                        nickname= String.valueOf(user.id);
                        String nombre= replaceCharacters(user.name);
                        String apellido = "";
                        //String contacto = user.url;
                        String contacto = "http://twitter.com/" + userid;
                        String fotoPerfil = user.profileImageUrl.toString().replace("normal", "400x400");
                        String aplicacion = "twitter";
                        //Log.e("Hola: ", nickname + " " + nombre + " " + apellido + " " + contacto + " " + fotoPerfil + " " + aplicacion);

                        Call<LoginResponse> call = apiService.addUserSocialNetwork(nickname, nombre, apellido, contacto, fotoPerfil, aplicacion);
                        call.enqueue(new retrofit2.Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove("session");
                                editor.putString("session", nickname);
                                editor.putString("aplicacion", "twitter");
                                editor.commit();
                                Intent intentFiguritas = new Intent(Login.this, Figuritas.class);
                                startActivity(intentFiguritas);
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                Log.e("Twitter: ", t.toString());
                            }
                        });
                        //Log.e("Resultado: ", String.valueOf(nickname) + " " + userid);
                        //Toast.makeText(getApplication(), profileImage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });

                //Toast.makeText(userResult.)

                /*new TwitterDataClient(session).getUsersService().show(12L, null, true,
                        new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                Log.d("twittercommunity", "user's profile url is "
                                        + result.data.profileImageUrlHttps);
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.d("twittercommunity", "exception is " + exception);
                            }
                        });*/
                /*new TwitterDataClient(tSession).getUsersService().show(12L, null, true, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        Log.e("Resultado: ", result.data.profileBannerUrl + " " + result.data.profileImageUrl + " " + result.data.id + " " + result.data.name);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e("Twitter: ", exception.toString());
                    }
                });*/
                //TwitterSession session = Twitter.getSessionManager().getActiveSession();

               /* Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {

                                User user = result.data;
                                String ruta = user.profileImageUrl;
                            }

                            @Override
                            public void failure(TwitterException e) {

                            }

                        });*/
                //Log.e("Twitter: " + result.data;
                /*nombre.setText(result.data.getUserName().toString());
                String userid = result.data.getUserId() + "";
                iniciarSesion.setVisibility(View.INVISIBLE);
                cerrarSesion.setVisibility(View.VISIBLE);*/
            }

            @Override
            public void failure(TwitterException exception) {
                new AlertDialog.Builder(Login.this)
                        .setTitle("Error")
                        .setMessage(exception.getLocalizedMessage())
                        .setNeutralButton("Aceptar", null)
                        .show();
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
                        //Toast.makeText(getApplicationContext(), "Logueado como: " + currentProfile.getName(), Toast.LENGTH_SHORT).show();
                        if(currentProfile != null){
                            nickname= String.valueOf(currentProfile.getId());
                            String nombre= replaceCharacters(currentProfile.getFirstName());
                            String apellido = replaceCharacters(currentProfile.getLastName());
                            //String contacto = user.url;
                            String contacto = String.valueOf(currentProfile.getLinkUri());
                            String fotoPerfil = String.valueOf(currentProfile.getProfilePictureUri(400, 400));
                            String aplicacion = "facebook";
                            //Log.e("Hola: ", nickname + " " + nombre + " " + apellido + " " + contacto + " " + fotoPerfil + " " + aplicacion);

                            Call<LoginResponse> call = apiService.addUserSocialNetwork(nickname, nombre, apellido, contacto, fotoPerfil, aplicacion);
                            call.enqueue(new retrofit2.Callback<LoginResponse>() {
                                @Override
                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.remove("session");
                                    editor.putString("session", nickname);
                                    editor.putString("aplicacion", "facebook");
                                    editor.commit();
                                    Intent intentFiguritas = new Intent(Login.this, Figuritas.class);
                                    startActivity(intentFiguritas);
                                }

                                @Override
                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                    Log.e("Facebook: ", t.toString());
                                }
                            });
                        }

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
                Log.e("Facebook error: ", error.toString());
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){
            super.onActivityResult(requestCode, resultCode, data);
            btnTwitter.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
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
        getMenuInflater().inflate(R.menu.no_logueado_menu, menu);
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
        } else if (id == R.id.nav_ayuda) {
            Intent i = new Intent(this, Ayuda.class);
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
                .setName("Login Page") // TODO: Define a title for the content shown.
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

    public String replaceCharacters(String palabra){
        palabra = palabra.replace("á", "a")
                        .replace("é", "e")
                        .replace("í", "i")
                        .replace("ó", "o")
                        .replace("ú", "u")
                        .replace("Á", "A")
                        .replace("É", "E")
                        .replace("Í", "I")
                        .replace("Ó", "O")
                        .replace("Ú", "U");
        return palabra;
    }

    public void datosFacebook(Profile perfil){

    }
}
