package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;

public class AprendiendoRCPPaso8Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_resultado);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
