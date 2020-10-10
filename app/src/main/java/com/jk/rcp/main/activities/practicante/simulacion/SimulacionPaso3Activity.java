package com.jk.rcp.main.activities.practicante.simulacion;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.utils.AlarmManager;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class SimulacionPaso3Activity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso2Activity";
    private AlarmManager alarmManager;
    private ProgressBar progressBarView;
    private TextView tv_time;
    private int progress;
    private CountDownTimer countDownTimer;
    private int endTime = 250;
    private int myProgress = 0;
    private AlertDialog alert11;
    private ImageView imagen;

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
        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar_paso2);
        tv_time = (TextView) findViewById(R.id.tv_timer_paso2);
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fn_countdown();
            }
        };

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
    }

    private void elegirEscenarioRandom(int escenario) {
        switch (escenario) {
            case 0:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario1, getApplicationContext().getTheme()));
                break;
            case 1:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario2, getApplicationContext().getTheme()));
                break;
            case 2:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario3, getApplicationContext().getTheme()));
                break;
            case 3:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario4, getApplicationContext().getTheme()));
                break;
            case 4:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario5, getApplicationContext().getTheme()));
                break;
            case 5:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.escenario6, getApplicationContext().getTheme()));
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
                if (getApplicationContext() == SimulacionPaso3Activity.this) {
                    tv_time.setText("0 secs");
                    setProgress(progress, endTime);
                    logicaSimulacion();
                }
            }
        };
        Log.d(TAG, "arranca");
        countDownTimer.start();
    }

    private void logicaSimulacion(){


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
