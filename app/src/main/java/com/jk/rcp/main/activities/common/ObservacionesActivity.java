package com.jk.rcp.main.activities.common;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
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
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.event.EventListRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventRequestCallbacks;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class ObservacionesActivity extends AppCompatActivity {
    private static final String TAG = "ObservacionesActivity";
    private User globalUser;
    private ListView eventList;
    private EventListAdapter eventListAdapter;
    private Event event = null;
    private CheckBox cbDisponeAyuda;
    private CheckBox cbBrazosFlexionados;
    private CheckBox cbDemora;
    private CheckBox cbNoConsulta;
    private CheckBox cbNoAtento;
    private EditText multilineText;

    //En esto usar fragmentos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_observaciones);
        globalUser = (User) getApplicationContext();

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registro de observaciones");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            event = (Event) getIntent().getSerializableExtra("event");
            Log.d(TAG, event.toString());
            TextView practicante = (TextView) findViewById(R.id.practicanteTextView);
            practicante.setText(event.getUser().getName());

            TextView fecha = (TextView) findViewById(R.id.fechaTextView);
            fecha.setText(event.getEventDate());

            cbBrazosFlexionados = (CheckBox) findViewById(R.id.cbBrazosFlexionados);
            if (event.getBrazosFlexionados() != null) {
                cbBrazosFlexionados.setChecked(event.getBrazosFlexionados());
            }

            cbDisponeAyuda = (CheckBox) findViewById(R.id.cbDisponeAyuda);
            if (event.getDisponeAyudaNoSolicita() != null) {
                cbDisponeAyuda.setChecked(event.getDisponeAyudaNoSolicita());
            }

            cbDemora = (CheckBox) findViewById(R.id.cbDisponeAyuda2);
            if (event.getDemoraTomaDesiciones() != null) {
                cbDemora.setChecked(event.getDemoraTomaDesiciones());
            }

            cbNoConsulta = (CheckBox) findViewById(R.id.cbNoConsulta);
            if (event.getNoConsultaEstadoVictima() != null) {
                cbNoConsulta.setChecked(event.getNoConsultaEstadoVictima());
            }

            cbNoAtento = (CheckBox) findViewById(R.id.cbNoAtento);
            if (event.getNoEstaAtentoAlEscenario() != null) {
                cbNoAtento.setChecked(event.getNoEstaAtentoAlEscenario());
            }
            multilineText = (EditText) findViewById(R.id.observationsText);
            if (event.getObservations() != null) {
                multilineText.setText(event.getObservations());
            }

            Button btnRegistrar = (Button) findViewById(R.id.btnRegistrarObservaciones);
            btnRegistrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    event.setBrazosFlexionados(cbBrazosFlexionados.isChecked());
                    event.setNoConsultaEstadoVictima(cbNoConsulta.isChecked());
                    event.setDisponeAyudaNoSolicita(cbDisponeAyuda.isChecked());
                    event.setDemoraTomaDesiciones(cbDemora.isChecked());
                    event.setNoEstaAtentoAlEscenario(cbNoAtento.isChecked());
                    event.setObservations(multilineText.getText().toString());

                    Log.d(TAG, event.toString());
                    updateObservation(event, globalUser.getBearerToken());
                }
            });
        }
    }

    private void updateObservation(Event event, String token) {
        Request request = new Request();
        request.updateObservations(event, token, new EventRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final EventPatch event) {
                Log.d(TAG, event.toString());
                Toast.makeText(getApplicationContext(), "Observación registrada correctamente", Toast.LENGTH_LONG).show();
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
