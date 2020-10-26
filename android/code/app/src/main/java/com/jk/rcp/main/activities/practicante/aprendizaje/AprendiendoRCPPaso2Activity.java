package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
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
import com.jk.rcp.main.bluetooth.SerialListener;
import com.jk.rcp.main.bluetooth.SerialService;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.bluetooth.SerialSocket;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.event.EventRequestCallbacks;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;
import com.jk.rcp.main.utils.Conversor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AprendiendoRCPPaso2Activity extends AppCompatActivity implements ServiceConnection, SerialListener {
    private static final String TAG = "AprendiendoRCPPaso_2_Activity";
    ProgressDialog progress;
    Timer timer;
    private Button btnVictimaNoResponde;
    private Button btnVictimaRespondioFinalizarProtocolo;
    private CheckBox cbPreguntarVozAlta;
    private CheckBox cbSacudirVictima;
    private CheckBox cbArrodillateAlLado;
    private Boolean primerEstado = false;
    private Boolean segundoEstado = false;
    private Boolean tercerEstado = false;
    private enum Connected {False, Pending, True}

    private String deviceAddress;
    private SerialService service;

    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean hexEnabled = false;
    private boolean pendingNewline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, SerialService.class), this, Context.BIND_AUTO_CREATE);
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
                Intent intent = new Intent(AprendiendoRCPPaso2Activity.this, AprendiendoRCPPaso3Activity.class);
                intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                disconnect();
                startActivity(intent);
            }
        });

        btnVictimaRespondioFinalizarProtocolo = findViewById(R.id.btnVictimaRespondioFinalizarProtocolo);
        btnVictimaRespondioFinalizarProtocolo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AprendiendoRCPPaso2Activity.this, AprendiendoRCPPaso8Activity.class);
                disconnect();
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

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("device") != null) {
            deviceAddress =  (String) getIntent().getSerializableExtra("device");
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

    /// COPIA

    @Override
    public void onDestroy() {
        try { unbindService(this); } catch(Exception ignored) {}
        if (connected != Connected.False)
            disconnect();
        stopService(new Intent(this, SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "en el start");
        if(service != null){
            Log.d(TAG, "No es null");
            service.attach(this);
        }
        else{
            Log.d("SERVICE", "Arranco el serviico");
            startService(new Intent(this, SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
        }
    }

    @Override
    public void onStop() {
        if(service != null && !isChangingConfigurations())
            service.detach();
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(initialStart && service != null) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart ) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    /*
     * Serial + UI
     */
    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("connecting...");
            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
    }

    private void send(String str) {
        if (connected != Connected.True) {
            Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String msg;
            byte[] data;
            msg = str;
            data = str.getBytes();

            service.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void receive(byte[] data) {
        String msg = new String(data);
        String converted = msg.substring(0, 8);
        String[] splitted = converted.split(";");
        Log.d(TAG, "Recibo ESP32: " + converted);
        String posicion = Conversor.posicionToString(Integer.valueOf(splitted[2]));
        tratamientoRecepcionBluetooth(posicion);
    }

    private void status(String str) {
        Log.d("STATUS", str);
    }

    /*
     * SerialListener
     */
    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }
}
