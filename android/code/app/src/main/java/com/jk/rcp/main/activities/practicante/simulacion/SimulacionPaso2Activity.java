package com.jk.rcp.main.activities.practicante.simulacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class SimulacionPaso2Activity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso2Activity";
    private ProgressBar progressBarView;
    private TextView tv_time;
    private int progress;
    private CountDownTimer countDownTimer;
    private int endTime = 250;
    private int myProgress = 0;
    private ImageView imagen;
    private boolean elEntornoEsSeguro;
    private Button btnAmbulancia;
    private Button btnEntornoNoSerguro;
    private boolean ambulanciaClicked = false;
    private boolean entornoNoSeguroClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulacion_paso2);

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagen = findViewById(R.id.imagen_paso2);
        progressBarView = findViewById(R.id.view_progress_bar_paso2);
        tv_time = findViewById(R.id.tv_timer_paso2);



        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("escenario") != null) {
            String escenario = (String) getIntent().getSerializableExtra("escenario");
            elegirEscenarioRandom(escenario.charAt(escenario.length() - 1));
        }

        btnAmbulancia = findViewById(R.id.btnEntornoSeguro4);
        btnEntornoNoSerguro = findViewById(R.id.btnEntornoSeguro3);

        btnAmbulancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambulanciaClicked = true;
                btnAmbulancia.setEnabled(false);
            }
        });

        btnEntornoNoSerguro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entornoNoSeguroClicked = true;
                btnEntornoNoSerguro.setEnabled(false);
            }
        });

        fn_countdown();
    }

    private void elegirEscenarioRandom(char escenario) {
        int num = Integer.parseInt(String.valueOf(escenario));
        switch (num) {
            case 0:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario1, getApplicationContext().getTheme()));
                this.elEntornoEsSeguro = true;
                break;
            case 1:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario2, getApplicationContext().getTheme()));
                this.elEntornoEsSeguro = false;
                break;
            case 2:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario3, getApplicationContext().getTheme()));
                this.elEntornoEsSeguro = false;
                break;
            case 3:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario4, getApplicationContext().getTheme()));
                this.elEntornoEsSeguro = false;
                break;
            case 4:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario5, getApplicationContext().getTheme()));
                this.elEntornoEsSeguro = true;

                break;
            case 5:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario6, getApplicationContext().getTheme()));
                this.elEntornoEsSeguro = true;
                break;
        }
    }

    private void fn_countdown() {
        myProgress = 0;

        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }

        progress = 1;
        endTime = 10;
        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgress(progress, endTime);
                progress = progress + 1;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                if (seconds == 0) {
                    tv_time.setText("0 secs");
                } else {
                    tv_time.setText(seconds + " secs");
                }
            }

            @Override
            public void onFinish() {
                tv_time.setText("0 secs");
                setProgress(progress, endTime);
                logicaSimulacion();

            }
        };
        Log.d(TAG, "arranca");
        countDownTimer.start();
    }

    private void logicaSimulacion() {
        Log.d(TAG, "ACA!");
        //ENTORNO SEGURO

        //Si el entorno es seguro y se selecciona llamar a la ambulancia se realiza la simulacion.
        if (this.elEntornoEsSeguro && this.ambulanciaClicked && !this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3Activity.class);
            startActivity(intent);
        }
        //Si el entorno es seguro pero no selecciona para llamar a la ambulancia se realiza la simulacion pero sera invalidada finalmente porque la ambulancia nunca llegara.
        else if (this.elEntornoEsSeguro && !this.ambulanciaClicked && !this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3Activity.class);
            startActivity(intent);
        }

        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        else if (this.elEntornoEsSeguro && this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3AlternativoActivity.class);
            intent.putExtra("ambulanciaClicked", ambulanciaClicked);
            intent.putExtra("entornoNoSeguroClicked", entornoNoSeguroClicked);
            intent.putExtra("elEntornoEsSeguro", elEntornoEsSeguro);
            startActivity(intent);
        }

        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        else if (this.elEntornoEsSeguro && !this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3AlternativoActivity.class);
            intent.putExtra("ambulanciaClicked", ambulanciaClicked);
            intent.putExtra("entornoNoSeguroClicked", entornoNoSeguroClicked);
            intent.putExtra("elEntornoEsSeguro", elEntornoEsSeguro);
            startActivity(intent);
        }

        //ENTORNO NO SEGURO

        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion correctamente.
        else if (!this.elEntornoEsSeguro && this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3AlternativoActivity.class);
            intent.putExtra("ambulanciaClicked", ambulanciaClicked);
            intent.putExtra("entornoNoSeguroClicked", entornoNoSeguroClicked);
            intent.putExtra("elEntornoEsSeguro", elEntornoEsSeguro);
            startActivity(intent);
        }
        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que no se llamo a la ambulancia.
        else if (!this.elEntornoEsSeguro && !this.ambulanciaClicked && this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3AlternativoActivity.class);
            intent.putExtra("ambulanciaClicked", ambulanciaClicked);
            intent.putExtra("entornoNoSeguroClicked", entornoNoSeguroClicked);
            intent.putExtra("elEntornoEsSeguro", elEntornoEsSeguro);
            startActivity(intent);
        }
        //Si el entorno no es seguro pero no se selecciona el entorno no es seguro, se realiza la simulacion pero sera invalidada finalmente porque el entorno no era seguro.
        else if (!this.elEntornoEsSeguro && !this.ambulanciaClicked && !this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3Activity.class);
            startActivity(intent);
        }
        //Si el entorno no es seguro pero se selecciona llamar a la ambulancia, se realiza la simulacion pero sera invalidada finalmente porque el entorno no era seguro.
        else if (!this.elEntornoEsSeguro && this.ambulanciaClicked && !this.entornoNoSeguroClicked) {
            Intent intent = new Intent(SimulacionPaso2Activity.this, SimulacionPaso3Activity.class);
            startActivity(intent);
        }
    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);
        Log.d(TAG, "progreso" + startTime);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
