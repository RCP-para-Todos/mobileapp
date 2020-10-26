package com.jk.rcp.main.activities.practicante;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.juego.JuegoPaso1Activity;

import java.util.ArrayList;

public class ModoJuegoActivity extends AppCompatActivity {
    private static final String TAG = "ModoJuegoActivity";
    private Button btnJugar;
    private Spinner spinner;
    private int tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_juego);

        btnJugar = findViewById(R.id.btnJugar);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ModoJuegoActivity.this, JuegoPaso1Activity.class);
                intent.putExtra("tiempo", tiempo);
                intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                startActivity(intent);
            }
        });

        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        btnJugar.setEnabled(true);
                        tiempo = 1 * 60 * 1000;
                        break;
                    case 2:
                        btnJugar.setEnabled(true);
                        tiempo = 3 * 60 * 1000;
                        break;
                    case 3:
                        btnJugar.setEnabled(true);
                        tiempo = 5 * 60 * 1000;
                        break;
                    default:
                        btnJugar.setEnabled(false);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }
}
