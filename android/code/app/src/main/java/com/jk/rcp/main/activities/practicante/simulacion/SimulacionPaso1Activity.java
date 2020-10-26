package com.jk.rcp.main.activities.practicante.simulacion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

import java.util.Random;

public class SimulacionPaso1Activity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso1Activity";
    private Button btnIniciarSimulacion;
    private TextView labelDescripcion;
    private ImageView imagen;
    private int escenario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulacion_paso1);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnIniciarSimulacion = findViewById(R.id.btnIniciarSimulacion);
        btnIniciarSimulacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SimulacionPaso1Activity.this, SimulacionPaso2Activity.class);
                intent.putExtra("escenario", "escenario" + escenario);
                intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                startActivity(intent);
            }
        });
        labelDescripcion = findViewById(R.id.textView2);
        imagen = findViewById(R.id.imageView2);
        elegirEscenarioRandom();
    }

    private void elegirEscenarioRandom() {
        Random random = new Random();
        this.escenario = random.nextInt(6);

        switch (escenario) {
            case 0:
                labelDescripcion.setText("Durante tus vacaciones luego de una larga caminata, la persona que te acompaña se agarra el pecho y cae desplomada");
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario1, getApplicationContext().getTheme()));
                break;
            case 1:
                labelDescripcion.setText("Trabajando en una obra en la cual se realiza un zanjeo para el recambio de cables subterráneos, tu compañero luego de clavar la pala cae desplomado");
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario2, getApplicationContext().getTheme()));
                break;
            case 2:
                labelDescripcion.setText("Estabas en tu casa descansando cuando escuchás un fuerte ruido en la habitación de al lado. Al acercarte para ver que pasó, encontrás a un familiar en el suelo, no responde");
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario3, getApplicationContext().getTheme()));
                break;
            case 3:
                labelDescripcion.setText("Manejando por la ruta, a lo lejos ves un obstáculo en tu camino. Al acercarte un poco más te encontrás con un camión volcado y un fuerte olor a combustible. Muy cerca del camión ves al conductor y aparentemente no se mueve");
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario4, getApplicationContext().getTheme()));
                break;
            case 4:
                labelDescripcion.setText("Luego de escuchar un fuerte golpe, te acercás corriendo al lugar y encontrás a una mujer desmayada");
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario5, getApplicationContext().getTheme()));
                break;
            case 5:
                labelDescripcion.setText("Mientras charlabas con tu padre, de pronto deja de hablar y se desploma");
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario6, getApplicationContext().getTheme()));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
