package com.jk.rcp.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;
import com.jk.rcp.main.utils.AlarmManager;
import com.jk.rcp.main.utils.Constants;
import com.jk.rcp.main.utils.EventManager;

public class HomeAlertActivity extends AppCompatActivity {
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alert_content);

        alarmManager = AlarmManager.getInstance();

        Button stopAlarmBtn = findViewById(R.id.btn_stop_alarm);

        // Al tocar el boton de stop, se detiene la alarma y se inicia la activity del home, reiniciando el ciclo
        stopAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager.stopSound();
                EventManager.registerEvent(Constants.EARTHQUAKE_DETECTED_FINISHED);
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}

