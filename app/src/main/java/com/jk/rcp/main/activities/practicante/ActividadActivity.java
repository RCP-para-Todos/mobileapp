package com.jk.rcp.main.activities.practicante;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.data.adapter.EventListAdapter;
import com.jk.rcp.main.data.model.event.Event;
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
        setContentView(R.layout.activity_estadisticas);
        globalUser = (User) getApplicationContext();
        Log.d(TAG, globalUser.toString());

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Actividad");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            event = (Event) getIntent().getSerializableExtra("event");
            Log.d(TAG, event.toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
