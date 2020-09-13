package com.jk.rcp.main.activities.instructor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;

public class EstadisticasActivity extends AppCompatActivity {
    private static final String TAG = "EstadisticasActivity";

    //En esto usar fragmentos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
    }
}
