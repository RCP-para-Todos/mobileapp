package com.jk.rcp.main.activities.practicante.juego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.HomeActivityPracticante;

public class JuegoPaso2Activity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso2Activity";
    private int puntaje = 0;
    private TextView tvPuntaje;
    private Button btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_paso2);

        tvPuntaje = findViewById(R.id.puntaje);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("puntaje") != null) {
            int puntaje = (int) getIntent().getSerializableExtra("puntaje");
            this.puntaje = puntaje;
        }

        if (puntaje > 0) {
            tvPuntaje.setText("Puntaje: " + puntaje);
        } else {
            tvPuntaje.setText("Puntaje: 0");
        }

        btnAtras = findViewById(R.id.btnBackHome);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(JuegoPaso2Activity.this, HomeActivityPracticante.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");

        finish();
        return true;
    }

}
