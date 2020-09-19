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

public class AprendiendoRCPPaso3Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPPaso_3_Activity";
    private Button btnAmbulanciaIncoming;
    private CheckBox cbCalleEntreCalle;
    private CheckBox cbBarrio;
    private CheckBox cbIndicarInicioRCP;
    private CheckBox cbAguardarConfirmacionOperador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso3);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAmbulanciaIncoming = findViewById(R.id.btnAmbulanciaIncoming);
        btnAmbulanciaIncoming.setEnabled(false);
        btnAmbulanciaIncoming.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnAmbulanciaIncoming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AprendiendoRCPPaso3Activity.this, AprendiendoRCPPaso4Activity.class);
                startActivity(intent);
            }
        });

        cbCalleEntreCalle = findViewById(R.id.cbCalleEntreCalle);
        cbBarrio = findViewById(R.id.cbBarrio);
        cbIndicarInicioRCP = findViewById(R.id.cbIndicarInicioRCP);
        cbAguardarConfirmacionOperador = findViewById(R.id.cbAguardarConfirmacionOperador);

        CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbCalleEntreCalle.isChecked() && cbBarrio.isChecked() && cbAguardarConfirmacionOperador.isChecked() && cbIndicarInicioRCP.isChecked()) {
                    btnAmbulanciaIncoming.setEnabled(true);
                    btnAmbulanciaIncoming.getBackground().setColorFilter(null);
                } else {
                    btnAmbulanciaIncoming.setEnabled(false);
                    btnAmbulanciaIncoming.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        };

        cbCalleEntreCalle.setOnCheckedChangeListener(onCheckedChangedListener);
        cbBarrio.setOnCheckedChangeListener(onCheckedChangedListener);
        cbIndicarInicioRCP.setOnCheckedChangeListener(onCheckedChangedListener);
        cbAguardarConfirmacionOperador.setOnCheckedChangeListener(onCheckedChangedListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
