package com.jk.rcp.main.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.activities.instructor.HomeActivityInstructor;
import com.jk.rcp.main.activities.practicante.HomeActivityPracticante;
import com.jk.rcp.main.data.model.user.LoginPost;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.APIService;
import com.jk.rcp.main.data.remote.ApiUtils;
import com.jk.rcp.main.data.remote.Request;
import com.jk.rcp.main.data.model.user.LoginRequestCallbacks;
import com.jk.rcp.main.utils.DeviceUtils;
import com.jk.rcp.main.utils.EventManager;

import java.io.IOException;

import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = "LoginActivity";
    private SharedPreferences preferences;
    private APIService mAPIService;
    private EventManager eventManager;
    private User globalUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalUser = (User) getApplicationContext();

        // Con esto levanta el HTTPS!
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }

        eventManager = new EventManager(this);
        final EditText username = findViewById(R.id.input_user);
        final EditText password = findViewById(R.id.input_password);
        final EditText rol = findViewById(R.id.input_password);
        final CheckBox rememberPassword = findViewById(R.id.rememberPassword);


        Button submitBtn = findViewById(R.id.btn_login);
        TextView register = findViewById(R.id.btn_create_user);
        mAPIService = ApiUtils.getAPIService();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        if (preferences.contains("user")) {
            username.setText(preferences.getString("user", null));
        } else {
            username.setText("");
        }

        if (preferences.contains("password")) {
            password.setText(preferences.getString("password", null));
        } else {
            password.setText("");
        }

        if (preferences.contains("rememberPassword")) {
            rememberPassword.setChecked(preferences.getString("rememberPassword", null).equals("true"));
        } else {
            rememberPassword.setChecked(false);
        }

        if (!DeviceUtils.isDeviceOnline(getApplicationContext())) {
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
        }

        //Solicito el permiso de ubicación para la localización de la distancia al sismo
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isDeviceOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                } else {
                    String trimmedEmail = username.getText().toString().trim();
                    String trimmedPassword = password.getText().toString().trim();
                    Boolean savePassword = rememberPassword.isChecked();
                    if (!TextUtils.isEmpty(trimmedEmail) && !TextUtils.isEmpty(trimmedPassword)) {
                        sendLogin(trimmedEmail, trimmedPassword, savePassword);
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final EditText email = findViewById(R.id.input_user);
        final EditText password = findViewById(R.id.input_password);
        final CheckBox rememberPassword = findViewById(R.id.rememberPassword);

        if (preferences.contains("user")) {
            email.setText(preferences.getString("user", null));
        } else {
            email.setText("");
        }

        if (preferences.contains("password")) {
            password.setText(preferences.getString("password", null));
        } else {
            password.setText("");
        }

        if (preferences.contains("rememberPassword")) {
            rememberPassword.setChecked(preferences.getString("rememberPassword", null).equals("true"));
        } else {
            rememberPassword.setChecked(false);
        }
        password.clearFocus();
    }

    public void sendLogin(final String username, final String password, final Boolean savePassword) {
        Request request = new Request();
        request.sendLogin(username, password, new LoginRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull LoginPost value) {
                if (value != null) {
                    Log.d(TAG, value.toString());
                    globalUser.setToken(value.getToken());
                    globalUser.setAuth(value.getAuth().equals("true") ? true : false);
                    globalUser.setRefreshToken(value.getRefreshToken());
                    globalUser.setRol(value.getRol());
                    globalUser.setCourses(value.getCourses());


                }
                // Recordar usuario y contraseña
                if (savePassword) {
                    preferences.edit().putString("user", username).commit();
                    preferences.edit().putString("password", password).commit();
                    preferences.edit().putString("rememberPassword", "true").commit();
                } else {
                    preferences.edit().putString("user", username).commit();
                    preferences.edit().remove("password").commit();
                    preferences.edit().remove("rememberPassword").commit();
                }

                if (globalUser.isPracticante()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivityPracticante.class);
                    startActivity(intent);
                } else if (!globalUser.isPracticante()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivityInstructor.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorBody(@NonNull ResponseBody errorBody) {
                if (errorBody != null) {
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;
                    try {
                        mJson = parser.parse(errorBody.string());
                        Gson gson = new Gson();
                        LoginPost errorResponse = gson.fromJson(mJson, LoginPost.class);
                        if (errorResponse.getAuth() == "false" && errorResponse.getToken() == null) {
                            Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ocurrió un error al intentar iniciar sesión", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
