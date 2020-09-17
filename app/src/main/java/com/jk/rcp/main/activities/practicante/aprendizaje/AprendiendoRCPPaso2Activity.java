package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

public class AprendiendoRCPPaso2Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPPaso_2_Activity";
    private Button btnVictimaNoResponde;
    private Button btnVictimaRespondioFinalizarProtocolo;
    private CheckBox cbPreguntarVozAlta;
    private CheckBox cbSacudirVictima;
    private CheckBox cbArrodillateAlLado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso2);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnVictimaNoResponde = findViewById(R.id.btnVictimaNoResponde);
        btnVictimaNoResponde.setEnabled(false);
        btnVictimaNoResponde.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnVictimaNoResponde.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AprendiendoRCPPaso2Activity.this, AprendiendoRCPPaso3Activity.class);
                startActivity(intent);
            }
        });

        btnVictimaRespondioFinalizarProtocolo = findViewById(R.id.btnVictimaRespondioFinalizarProtocolo);
        btnVictimaRespondioFinalizarProtocolo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AprendiendoRCPPaso2Activity.this, AprendiendoRCPPaso8Activity.class);
                startActivity(intent);
            }
        });

        cbArrodillateAlLado = findViewById(R.id.cbArrodillateAlLado);
        cbSacudirVictima = findViewById(R.id.cbSacudirVictima);
        cbPreguntarVozAlta = findViewById(R.id.cbPreguntarVozAlta);

        CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbArrodillateAlLado.isChecked() && cbSacudirVictima.isChecked() && cbPreguntarVozAlta.isChecked()) {
                    btnVictimaNoResponde.setEnabled(true);
                    btnVictimaNoResponde.getBackground().setColorFilter(null);
                } else {
                    btnVictimaNoResponde.setEnabled(false);
                    btnVictimaNoResponde.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        };

        cbArrodillateAlLado.setOnCheckedChangeListener(onCheckedChangedListener);
        cbSacudirVictima.setOnCheckedChangeListener(onCheckedChangedListener);
        cbPreguntarVozAlta.setOnCheckedChangeListener(onCheckedChangedListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
