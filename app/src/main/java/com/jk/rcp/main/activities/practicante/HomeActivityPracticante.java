package com.jk.rcp.main.activities.practicante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.user.User;

public class HomeActivityPracticante extends AppCompatActivity {
    private static final String TAG = "HomeActivityPracticante";
    private User globalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content_practicante);
        globalUser = (User) getApplicationContext();

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inicio");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton buttonVerEstadisticas = (ImageButton) findViewById(R.id.btn_ver_estadisticas);
        buttonVerEstadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivityPracticante.this, EstadisticasActivity.class);
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
