package com.jk.rcp.main.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.adapter.EventListAdapter;
import com.jk.rcp.main.data.model.course.Student;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventListRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;

public class EstadisticasActivity extends AppCompatActivity {
    private static final String TAG = "EstadisticasActivity";
    private User globalUser;
    private ListView eventList;
    private EventListAdapter eventListAdapter;

    //En esto usar fragmentos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        globalUser = (User) getApplicationContext();

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("Student") != null) {
            Student student = (Student) getIntent().getSerializableExtra("Student");
            Log.d(TAG, student.toString());
            // Obtengo los eventos de la API, con el token
            getEventsByPracticant(student.getName().toLowerCase(), globalUser.getBearerToken());
        } else {
            // Obtengo los eventos de la API, con el token
            getEvents(globalUser.getBearerToken());
        }
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Estadisticas");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void getEvents(String token) {
        Request request = new Request();
        request.getEvents(token, new EventListRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final List<Event> eventos) {
                eventList = findViewById(R.id.eventsList);
                eventListAdapter = new EventListAdapter(getApplicationContext(), eventos);
                eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(getApplicationContext(), ActividadActivity.class);
                        intent.putExtra("event", eventos.get(position));
                        startActivity(intent);
                    }
                });
                eventList.setAdapter(eventListAdapter);
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

    public void getEventsByPracticant(String practicant, String token) {
        Request request = new Request();
        request.getEventsByPracticant(practicant.toLowerCase(), token, new EventListRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final List<Event> eventos) {
                eventList = findViewById(R.id.eventsList);
                eventListAdapter = new EventListAdapter(getApplicationContext(), eventos);
                Collections.sort(eventos);
                eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(getApplicationContext(), ActividadActivity.class);
                        intent.putExtra("event", eventos.get(position));
                        startActivity(intent);
                    }
                });
                eventList.setAdapter(eventListAdapter);
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
