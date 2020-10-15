package com.jk.rcp.main.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.adapter.EventListAdapter;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventPatch;
import com.jk.rcp.main.data.model.event.EventPatchRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ActividadActivity extends AppCompatActivity {
    private static final String TAG = "ActividadActivity";
    private User globalUser;
    private String name;
    private ListView eventList;
    private EventListAdapter eventListAdapter;
    private Event event = null;
    private Button buttonVerObservaciones;
    private TextView tiempoTotalManiobra;
    private TextView tiempoInactividad;
    private TextView porcentajeSobrevida;
    private TextView porcentajeInsuflacionesCorrectas;
    private TextView porcentajeCompresionesCorrectas;
    private TextView fuerzaPromedioAplicada;
    private TextView cantInsIncorrectasMalaPosicion;
    private TextView calidadInsuflaciones;
    private ProgressBar progressBarActividad;

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

        tiempoTotalManiobra = findViewById(R.id.valueTiempoTotalManiobra);
        porcentajeSobrevida = findViewById(R.id.valuePorcentajeSobrevida);
        tiempoInactividad = findViewById(R.id.valueTiempoInactividad);
        porcentajeInsuflacionesCorrectas = findViewById(R.id.valuePorcentajeInsuflacionesCorrectas);
        porcentajeCompresionesCorrectas = findViewById(R.id.valuePorcentajeCompresionesCorrectas);
        fuerzaPromedioAplicada = findViewById(R.id.valueFuerzaPromedioAplicada);
        calidadInsuflaciones = findViewById(R.id.valueCalidadInsuflasiones);
        cantInsIncorrectasMalaPosicion = findViewById(R.id.valueCantidadInsuflacionesIncorrectasPorMalaPosicion);
        buttonVerObservaciones = findViewById(R.id.btnVerObs);
        progressBarActividad = findViewById(R.id.progressBarActividad);

        if (getIntent().getExtras() != null) {
            event = (Event) getIntent().getSerializableExtra("event");
            name = event.getUser().getName();
            getEvent(event.getId(), globalUser.getBearerToken());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEvent(event.getId(), globalUser.getBearerToken());
    }

    public void getEvent(String eventId, String token) {
        progressBarActividad.setVisibility(View.VISIBLE);
        Request request = new Request();
        request.getEvent(eventId, token, new EventPatchRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final EventPatch event) {
                progressBarActividad.setVisibility(View.GONE);

                tiempoTotalManiobra.setText(String.valueOf(Instant.tiempoTotalManiobra(event.getInstants())));

                porcentajeSobrevida.setText(String.valueOf(Instant.porcentajeTotalSobreVida(event.getInstants())));

                tiempoInactividad.setText(String.valueOf(Instant.tiempoTotalInactividad(event.getInstants())));

                porcentajeInsuflacionesCorrectas.setText(String.valueOf(Instant.porcentajeInsuflacionesCorrectas(event.getInstants())));

                porcentajeCompresionesCorrectas.setText(String.valueOf(Instant.porcentajeCompresionesCorrectas(event.getInstants())));

                cantInsIncorrectasMalaPosicion.setText(String.valueOf(Instant.cantidadInsuflacionesCorrectasPosicionCabeza(event.getInstants())));

                fuerzaPromedioAplicada.setText(String.valueOf(Instant.fuerzaPromedioAplicada(event.getInstants())));

                calidadInsuflaciones.setText(Instant.calidadInsuflaciones(event.getInstants()));

                buttonVerObservaciones.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(ActividadActivity.this, ObservacionesActivity.class);
                        intent.putExtra("event", event);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                progressBarActividad.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Ocurrió un error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorBody(@NonNull ResponseBody errorBody) {
                progressBarActividad.setVisibility(View.GONE);
                if (errorBody != null) {
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;
                    try {
                        mJson = parser.parse(errorBody.string());
                        Gson gson = new Gson();
                        EventPost errorResponse = gson.fromJson(mJson, EventPost.class);

                        Toast.makeText(getApplicationContext(), "Ocurrió un error", Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
