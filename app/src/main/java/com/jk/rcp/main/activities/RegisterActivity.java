package com.jk.rcp.main.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.model.user.LoginPost;
import com.jk.rcp.main.data.model.user.LoginRequestCallbacks;
import com.jk.rcp.main.data.remote.Request;
import com.jk.rcp.main.utils.DeviceUtils;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @Bind(R.id.input_user)
    EditText _usernameText;

    @Bind(R.id.input_password)
    EditText _passwordText;

    @Bind(R.id.btn_create_user)
    Button _signupButton;

    private RadioGroup toggle;
    private RadioButton practicante;
    private RadioButton instructor;
    private String rol = "practicante";
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Registro");
        }

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        progressBar = findViewById(R.id.progressBarRegister);
        progressBar.setVisibility(View.GONE);
        practicante = findViewById(R.id.on);
        instructor = findViewById(R.id.off);
        toggle = (RadioGroup) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              @Override
                                              public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                  switch (checkedId) {
                                                      case R.id.off:
                                                          rol = "practicante";
                                                          break;
                                                      case R.id.on:
                                                          rol = "instructor";
                                                          break;
                                                  }
                                              }
                                          }
        );
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        String name = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        sendRegister(name, password, rol);
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Revise los datos ingresados", Toast.LENGTH_LONG).show();
    }


    public void sendRegister(final String username, final String password, final String rol) {
        progressBar.setVisibility(View.VISIBLE);
        _usernameText.setEnabled(false);
        _signupButton.setEnabled(false);
        _passwordText.setEnabled(false);
        toggle.setEnabled(false);
        practicante.setEnabled(false);
        instructor.setEnabled(false);

        if (DeviceUtils.isDeviceOnline(this)) {
            Request request = new Request();
            request.sendRegister(username, password, rol, new LoginRequestCallbacks() {
                @Override
                public void onSuccess(@NonNull LoginPost value) {
                    progressBar.setVisibility(View.GONE);
                    _usernameText.setEnabled(true);
                    _signupButton.setEnabled(true);
                    _passwordText.setEnabled(true);
                    toggle.setEnabled(true);
                    practicante.setEnabled(true);
                    instructor.setEnabled(true);


                    setResult(RESULT_OK, null);
                    finish();
                }

                @Override
                public void onError(@NonNull Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                    _usernameText.setEnabled(true);
                    _signupButton.setEnabled(true);
                    _passwordText.setEnabled(true);
                    toggle.setEnabled(true);
                    practicante.setEnabled(true);
                    instructor.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Ocurrió un error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onErrorBody(@NonNull ResponseBody errorBody) {
                    progressBar.setVisibility(View.GONE);
                    _usernameText.setEnabled(true);
                    _signupButton.setEnabled(true);
                    _passwordText.setEnabled(true);
                    toggle.setEnabled(true);
                    practicante.setEnabled(true);
                    instructor.setEnabled(true);
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
        } else {
            Toast.makeText(this, "No hay internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 4) {
            _usernameText.setError("Al menos 4 caracteres");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("La contraseña debe tener entre 4 y 20 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}