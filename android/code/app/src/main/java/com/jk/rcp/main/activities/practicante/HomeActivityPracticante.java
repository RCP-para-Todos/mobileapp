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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.common.EstadisticasActivity;
import com.jk.rcp.main.activities.practicante.aprendizaje.AprendiendoRCPPaso1Activity;
import com.jk.rcp.main.activities.practicante.simulacion.SimulacionPaso1Activity;
import com.jk.rcp.main.data.model.user.User;

import java.util.ArrayList;

public class HomeActivityPracticante extends AppCompatActivity {
    private static final String TAG = "HomeActivityPracticante";
    private User globalUser;
    private static final int REQUEST_ENABLE_BT = 200;

    private enum ScanState {NONE, LESCAN, DISCOVERY, DISCOVERY_FINISHED}

    private ScanState scanState = ScanState.NONE;
    private static final long LESCAN_PERIOD = 10000; // similar to bluetoothAdapter.startDiscovery
    private Handler leScanStopHandler = new Handler();
    private BluetoothAdapter.LeScanCallback leScanCallback;
    private BroadcastReceiver discoveryBroadcastReceiver;
    private IntentFilter discoveryIntentFilter;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();

    private BluetoothDevice ESP32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content_practicante);
        globalUser = (User) getApplicationContext();

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inicio");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton buttonVerEstadisticas = findViewById(R.id.btn_ver_estadisticas);
        buttonVerEstadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivityPracticante.this, EstadisticasActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnAprenderRCP = findViewById(R.id.btn_aprender_rcp);
        btnAprenderRCP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                if (ESP32 == null) {
                    startScan();
                } else {
                    Intent intent = new Intent(HomeActivityPracticante.this, AprendiendoRCPPaso1Activity.class);
                    stopScan();
                    intent.putExtra("device", ESP32.getAddress());
                    startActivity(intent);
                }
            }
        });

        ImageButton btnSimulacion = findViewById(R.id.btn_simulacion);
        btnSimulacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                if (ESP32 == null) {
                    startScan();
                } else {
                    Intent intent = new Intent(HomeActivityPracticante.this, SimulacionPaso1Activity.class);
                    stopScan();
                    intent.putExtra("device", ESP32.getAddress());
                    startActivity(intent);
                }
            }
        });

        ImageButton btnJuego = findViewById(R.id.btn_modo_juego);
        btnJuego.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                if (ESP32 == null) {
                    startScan();
                } else {
                    Intent intent = new Intent(HomeActivityPracticante.this, ModoJuegoActivity.class);
                    stopScan();
                    intent.putExtra("device", ESP32.getAddress());
                    startActivity(intent);
                }
            }
        });

        leScanCallback = (device, rssi, scanRecord) -> {
            if (device != null) {
                runOnUiThread(() -> {
                    updateScan(device);
                });
            }
        };

        discoveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getType() != BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                        runOnUiThread(() -> updateScan(device));
                    }
                }
                if (intent.getAction().equals((BluetoothAdapter.ACTION_DISCOVERY_FINISHED))) {
                    scanState = ScanState.DISCOVERY_FINISHED; // don't cancel again
                    stopScan();
                }
            }
        };
        discoveryIntentFilter = new IntentFilter();
        discoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        discoveryIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                if (ESP32 == null) {
                    startScan();
                }
            }
        }
        startScan();
    }

    @SuppressLint("StaticFieldLeak") // AsyncTask needs reference to this fragment
    private void startScan() {
        if (scanState != ScanState.NONE)
            return;
        scanState = ScanState.LESCAN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                scanState = ScanState.NONE;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.location_permission_title);
                builder.setMessage(R.string.location_permission_message);
                builder.setPositiveButton(android.R.string.ok,
                        (dialog, which) -> requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0));
                builder.show();
                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean locationEnabled = false;
            try {
                locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }
            try {
                locationEnabled |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ignored) {
            }
            if (!locationEnabled)
                scanState = ScanState.DISCOVERY;
            // Starting with Android 6.0 a bluetooth scan requires ACCESS_COARSE_LOCATION permission, but that's not all!
            // LESCAN also needs enabled 'location services', whereas DISCOVERY works without.
            // Most users think of GPS as 'location service', but it includes more, as we see here.
            // Instead of asking the user to enable something they consider unrelated,
            // we fall back to the older API that scans for bluetooth classic _and_ LE
            // sometimes the older API returns less results or slower
        }
        listItems.clear();

        if (scanState == ScanState.LESCAN) {
            leScanStopHandler.postDelayed(this::stopScan, LESCAN_PERIOD);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void[] params) {
                    bluetoothAdapter.startLeScan(null, leScanCallback);
                    return null;
                }
            }.execute(); // start async to prevent blocking UI, because startLeScan sometimes take some seconds
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // ignore requestCode as there is only one in this fragment
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new Handler(Looper.getMainLooper()).postDelayed(this::startScan, 1); // run after onResume to avoid wrong empty-text
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getText(R.string.location_denied_title));
            builder.setMessage(getText(R.string.location_denied_message));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void updateScan(BluetoothDevice device) {
        if (scanState == ScanState.NONE)
            return;
        if (device != null) {
            if (listItems.indexOf(device) < 0 && device.getName() != null) {
                listItems.add(device);
                if (device.getName().equals("ESP32")) {
                    this.ESP32 = device;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getText(R.string.esp32_found_title));
                    builder.setMessage(getText(R.string.esp32_found_message));
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    stopScan();
                }
            }
        }
        stopScan();
        if (this.ESP32 == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getText(R.string.error_esp32_title));
            builder.setMessage(getText(R.string.error_esp32_message));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void stopScan() {
        if (scanState == ScanState.NONE)
            return;

        switch (scanState) {
            case LESCAN:
                leScanStopHandler.removeCallbacks(this::stopScan);
                bluetoothAdapter.stopLeScan(leScanCallback);
                break;
            case DISCOVERY:
                bluetoothAdapter.cancelDiscovery();
                break;
            default:
                // already canceled
        }
        scanState = ScanState.NONE;

    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
        unregisterReceiver(discoveryBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(discoveryBroadcastReceiver, discoveryIntentFilter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
