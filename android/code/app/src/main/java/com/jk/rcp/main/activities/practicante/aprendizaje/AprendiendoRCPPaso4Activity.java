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

public class AprendiendoRCPPaso4Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPPaso_4_Activity";
    private Button btnComenzarRCP;
    private CheckBox cbManosPosicion;
    private CheckBox cbUbicarEsternon;
    private CheckBox cbObservarSiRespira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso4);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnComenzarRCP = findViewById(R.id.btnComenzarRCP);
        btnComenzarRCP.setEnabled(false);
        btnComenzarRCP.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnComenzarRCP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AprendiendoRCPPaso4Activity.this, AprendiendoRCPPaso5Activity.class);
                intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                startActivity(intent);
            }
        });


        cbObservarSiRespira = findViewById(R.id.cbObservarSiRespira);
        cbUbicarEsternon = findViewById(R.id.cbUbicarEsternon);
        cbManosPosicion = findViewById(R.id.cbManosPosicion);

        CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbObservarSiRespira.isChecked() && cbUbicarEsternon.isChecked() && cbManosPosicion.isChecked()) {
                    btnComenzarRCP.setEnabled(true);
                    btnComenzarRCP.getBackground().setColorFilter(null);
                } else {
                    btnComenzarRCP.setEnabled(false);
                    btnComenzarRCP.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        };

        cbObservarSiRespira.setOnCheckedChangeListener(onCheckedChangedListener);
        cbUbicarEsternon.setOnCheckedChangeListener(onCheckedChangedListener);
        cbManosPosicion.setOnCheckedChangeListener(onCheckedChangedListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
