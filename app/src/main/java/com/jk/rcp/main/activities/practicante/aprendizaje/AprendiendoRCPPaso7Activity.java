package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.utils.AlarmManager;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class AprendiendoRCPPaso7Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRC_paso_7_PActivity";
    private AlarmManager alarmManager;
    private ProgressBar progressBarView;
    private TextView tv_time;
    private int progress;
    private CountDownTimer countDownTimer;
    private int endTime = 250;
    private int myProgress = 0;
    private AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso7);

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar_paso7);
        tv_time = (TextView) findViewById(R.id.tv_timer_paso7);
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

        alarmManager = new AlarmManager(onCompletionListener);
        alarmManager.startSound(this, "AprendizajeAudio.mp3", false, true);
    }

    private void fn_countdown() {
        myProgress = 0;

        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }

        progress = 1;
        endTime = 68;
        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgress(progress, endTime);
                progress = progress + 1;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                if (seconds == 0) {
                    tv_time.setText("0 secs");
                } else if (seconds > 60) {

                    tv_time.setText(minutes + " min, " + seconds + " secs");
                } else {
                    tv_time.setText(seconds + " secs");
                }
            }

            @Override
            public void onFinish() {
                tv_time.setText("0 secs");
                setProgress(progress, endTime);
                Intent intent = new Intent(AprendiendoRCPPaso7Activity.this, AprendiendoRCPPaso8Activity.class);
                startActivity(intent);

            }
        };
        Log.d(TAG, "arranca");
        countDownTimer.start();
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
        finish();
        return true;
    }
}
