package com.jk.rcp.main.activities.practicante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.juego.JuegoPaso1Activity;
import com.jk.rcp.main.activities.practicante.juego.JuegoPaso2Activity;

public class ModoJuegoActivity extends AppCompatActivity {
    private static final String TAG = "ModoJuegoActivity";
    private Button btnJugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_juego);

        btnJugar = (Button) findViewById(R.id.btnJugar);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ModoJuegoActivity.this, JuegoPaso1Activity.class);
                startActivity(intent);
            }
        });
    }
}
