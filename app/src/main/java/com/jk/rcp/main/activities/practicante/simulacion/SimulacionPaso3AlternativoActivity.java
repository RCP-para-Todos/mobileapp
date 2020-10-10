package com.jk.rcp.main.activities.practicante.simulacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.HomeActivityPracticante;

public class SimulacionPaso3AlternativoActivity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso3AltActivity";
    private Button btnAtras;
    private TextView resultado;
    private Boolean ambulanciaClicked = false;
    private Boolean elEntornoEsSeguro = false;
    private Boolean entornoNoSeguroClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulacion_paso3_alternativo);

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null
                && getIntent().getSerializableExtra("entornoNoSeguroClicked") != null
                && getIntent().getSerializableExtra("ambulanciaClicked") != null
                && getIntent().getSerializableExtra("elEntornoEsSeguro") != null) {
            this.entornoNoSeguroClicked = (Boolean) getIntent().getSerializableExtra("entornoNoSeguroClicked");
            this.ambulanciaClicked = (Boolean) getIntent().getSerializableExtra("ambulanciaClicked");
            this.elEntornoEsSeguro = (Boolean) getIntent().getSerializableExtra("elEntornoEsSeguro");
        }
        resultado = findViewById(R.id.resultado);
        btnAtras = findViewById(R.id.btnBackHome);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SimulacionPaso3AlternativoActivity.this, HomeActivityPracticante.class);
                startActivity(intent);
            }
        });
        logicaResultado();
    }

    private void logicaResultado() {
        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        if (this.elEntornoEsSeguro && !this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            this.resultado.setText("Lo sentimos, el entorno era seguro, correspondía realizar la maniobra RCP y llamar a la ambulancia");
        }
        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        if (this.elEntornoEsSeguro && this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            this.resultado.setText("Lo sentimos, el entorno era seguro, correspondía realizar la maniobra RCP y esperar a la ambulancia");
        }
        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion correctamente.
        else if (!this.elEntornoEsSeguro && this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            this.resultado.setText("Felicitaciones, el entorno no era seguro y no correspondía realizar la maniobra RCP");
        }
        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion correctamente.
        else if (!this.elEntornoEsSeguro && !this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            this.resultado.setText("Lo sentimos, el entorno no era seguro y no correspondía realizar la maniobra RCP pero se debía llamar a la ambulancia");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
