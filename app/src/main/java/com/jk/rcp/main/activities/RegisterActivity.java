package com.jk.rcp.main.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;
import com.jk.rcp.main.data.remote.Request;
import com.jk.rcp.main.utils.DeviceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_surname)
    EditText _surnameText;
    @Bind(R.id.input_user)
    EditText _emailText;
    @Bind(R.id.input_dni)
    EditText _dniText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_repeat_password)
    EditText _confirmPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    private Request request;

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

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppThemeNoActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String dni = _dniText.getText().toString();
        String password = _passwordText.getText().toString();

        sendRegister(name, surname, dni, email, password);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Revise los datos ingresados", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void sendRegister(String name, String surname, String dni, String email, String password) {
        if (DeviceUtils.isDeviceOnline(this)) {
            Request request = new Request();
            request.sendRegister(name, surname, dni, email, password);
        } else {
            Toast.makeText(this, "No hay internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String dni = _dniText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmPassword = _confirmPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 4) {
            _nameText.setError("Al menos 4 caracteres");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (surname.isEmpty() || surname.length() < 4) {
            _surnameText.setError("Al menos 4 caracteres");
            valid = false;
        } else {
            _surnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Ingrese una dirección de correo electrónico válida");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        String regex = "\\d+";
        if (dni.isEmpty() || dni.length() < 7 || dni.length() > 8 || !dni.matches(regex)) {
            _dniText.setError("Ingrese un DNI válido");
            valid = false;
        } else {
            _dniText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 20) {
            _passwordText.setError("La contraseña debe tener entre 8 y 20 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (confirmPassword.isEmpty() || confirmPassword.length() < 8 || confirmPassword.length() > 20 || !confirmPassword.equals(password)) {
            _confirmPasswordText.setError("Las contraseñas no coinciden");
            valid = false;
        } else {
            _confirmPasswordText.setError(null);
        }
        return valid;
    }
}