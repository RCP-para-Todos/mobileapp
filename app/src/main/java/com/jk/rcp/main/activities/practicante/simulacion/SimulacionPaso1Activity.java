package com.jk.rcp.main.activities.practicante.simulacion;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

public class SimulacionPaso1Activity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso1Activity";
    private Button btnIniciarSimulacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso1);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnIniciarSimulacion = findViewById(R.id.btnIniciarSimulacion);
        btnIniciarSimulacion.setEnabled(false);
        btnIniciarSimulacion.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnIniciarSimulacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SimulacionPaso1Activity.this, SimulacionPaso2Activity.class);
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
