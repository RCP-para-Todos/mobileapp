package com.jk.rcp.main.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.data.adapter.EventListAdapter;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.User;

public class ActividadActivity extends AppCompatActivity {
    private static final String TAG = "ActividadActivity";
    private User globalUser;
    private ListView eventList;
    private EventListAdapter eventListAdapter;
    private Event event = null;

    //En esto usar fragmentos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        globalUser = (User) getApplicationContext();

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Actividad");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            event = (Event) getIntent().getSerializableExtra("event");

            TextView tiempoTotalManiobra = findViewById(R.id.valueTiempoTotalManiobra);
            tiempoTotalManiobra.setText(String.valueOf(Instant.tiempoTotalManiobra(event.getInstants())));

            TextView porcentajeSobrevida = findViewById(R.id.valuePorcentajeSobrevida);
            porcentajeSobrevida.setText(String.valueOf(Instant.porcentajeTotalSobreVida(event.getInstants())));

            TextView tiempoInactividad = findViewById(R.id.valueTiempoInactividad);
            tiempoInactividad.setText(String.valueOf(Instant.tiempoTotalInactividad(event.getInstants())));

            TextView porcentajeInsuflacionesCorrectas = findViewById(R.id.valuePorcentajeInsuflacionesCorrectas);
            porcentajeInsuflacionesCorrectas.setText(String.valueOf(Instant.porcentajeInsuflacionesCorrectas(event.getInstants())));

            TextView porcentajeCompresionesCorrectas = findViewById(R.id.valuePorcentajeCompresionesCorrectas);
            porcentajeCompresionesCorrectas.setText(String.valueOf(Instant.porcentajeCompresionesCorrectas(event.getInstants())));

            TextView cantInsIncorrectasMalaPosicion = findViewById(R.id.valueCantidadInsuflacionesIncorrectasPorMalaPosicion);
            cantInsIncorrectasMalaPosicion.setText(String.valueOf(Instant.cantidadInsuflacionesCorrectasPosicionCabeza(event.getInstants())));

            TextView fuerzaPromedioAplicada = findViewById(R.id.valueFuerzaPromedioAplicada);
            fuerzaPromedioAplicada.setText(String.valueOf(Instant.fuerzaPromedioAplicada(event.getInstants())));

            TextView calidadInsuflaciones = findViewById(R.id.valueCalidadInsuflasiones);
            calidadInsuflaciones.setText(Instant.calidadInsuflaciones(event.getInstants()));

            Button buttonVerObservaciones = (Button) findViewById(R.id.btnVerObs);
            buttonVerObservaciones.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ActividadActivity.this, ObservacionesActivity.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
