package com.jk.rcp.main.activities.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.user.User;

public class HomeActivityInstructor extends AppCompatActivity {
    private static final String TAG = "HomeActivityInstructor";
    private User globalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content_instructor);
        globalUser = (User) getApplicationContext();

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inicio");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton buttonVerMisCursos = findViewById(R.id.btn_mis_cursos);
        buttonVerMisCursos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivityInstructor.this, MisCursosActivity.class);
                startActivity(intent);
            }
        });

        Button btnCrearCurso = findViewById(R.id.btn_crear_curso);
        btnCrearCurso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivityInstructor.this, CrearCursoActivity.class);
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
