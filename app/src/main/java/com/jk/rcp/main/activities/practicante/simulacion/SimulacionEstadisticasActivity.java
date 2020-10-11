package com.jk.rcp.main.activities.practicante.simulacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.HomeActivityPracticante;
import com.jk.rcp.main.activities.practicante.juego.JuegoPaso2Activity;
import com.jk.rcp.main.data.model.event.Event;

public class SimulacionEstadisticasActivity extends AppCompatActivity {
    private static final String TAG = "SimulacionEstadisticasActivity";
    private Button btnAtras;
    private Event evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulacion_paso4);
        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("evento") != null) {
            this.evento = (Event) getIntent().getSerializableExtra("evento");
        }

        btnAtras = findViewById(R.id.btnBackHome);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SimulacionEstadisticasActivity.this, HomeActivityPracticante.class);
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
