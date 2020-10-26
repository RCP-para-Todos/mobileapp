package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.utils.Conversor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class AprendiendoRCPPaso2Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPPaso_2_Activity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 200;
    ProgressDialog progress;
    BluetoothGattCharacteristic mGattChar;
    Timer timer;
    private Button btnVictimaNoResponde;
    private Button btnVictimaRespondioFinalizarProtocolo;
    private CheckBox cbPreguntarVozAlta;
    private CheckBox cbSacudirVictima;
    private CheckBox cbArrodillateAlLado;
    private Boolean primerEstado = false;
    private Boolean segundoEstado = false;
    private Boolean tercerEstado = false;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso2);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnVictimaNoResponde = findViewById(R.id.btnVictimaNoResponde);
        btnVictimaNoResponde.setEnabled(false);
        btnVictimaNoResponde.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnVictimaNoResponde.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBluetoothGatt.close();
                Intent intent = new Intent(AprendiendoRCPPaso2Activity.this, AprendiendoRCPPaso3Activity.class);
                intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                startActivity(intent);
            }
        });

        btnVictimaRespondioFinalizarProtocolo = findViewById(R.id.btnVictimaRespondioFinalizarProtocolo);
        btnVictimaRespondioFinalizarProtocolo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBluetoothGatt.close();
                Intent intent = new Intent(AprendiendoRCPPaso2Activity.this, AprendiendoRCPPaso8Activity.class);
                startActivity(intent);
            }
        });

        cbArrodillateAlLado = findViewById(R.id.cbArrodillateAlLado);
        cbSacudirVictima = findViewById(R.id.cbSacudirVictima);
        cbPreguntarVozAlta = findViewById(R.id.cbPreguntarVozAlta);

        CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbArrodillateAlLado.isChecked() && cbSacudirVictima.isChecked() && cbPreguntarVozAlta.isChecked()) {
                    btnVictimaNoResponde.setEnabled(true);
                    btnVictimaNoResponde.getBackground().setColorFilter(null);
                } else {
                    btnVictimaNoResponde.setEnabled(false);
                    btnVictimaNoResponde.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        };

        cbArrodillateAlLado.setOnCheckedChangeListener(onCheckedChangedListener);
        cbSacudirVictima.setOnCheckedChangeListener(onCheckedChangedListener);
        cbPreguntarVozAlta.setOnCheckedChangeListener(onCheckedChangedListener);
        // toggleButton.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
                return;
            }
        }
    }

    private void tratamientoRecepcionBluetooth(String posicion) {

        if (posicion.equals("Reclinado")) {
            final Runnable sacudirVictima = new Runnable() {
                public void run() {
                    cbSacudirVictima.setChecked(true);
                    if (cbSacudirVictima.isChecked() && cbArrodillateAlLado.isChecked() && cbPreguntarVozAlta.isChecked()) {
                        btnVictimaNoResponde.setEnabled(true);
                    }
                }
            };

            TimerTask task = new TimerTask() {
                public void run() {
                    runOnUiThread(sacudirVictima);
                }
            };
            task.run();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
