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
    public final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String s = new String(data);
            String converted = s.substring(0, 8);
            String[] splitted = converted.split(";");
            Log.d(TAG, "Recibo ESP32: " + converted);
            String posicion = Conversor.posicionToString(Integer.valueOf(splitted[2]));
            tratamientoRecepcionBluetooth(posicion);
            // mBluetoothGatt.disconnect();
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt,
                                            int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService mGattService =
                        mBluetoothGatt.getService(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"));
                if (mGattService != null) {

                    Log.i("onServicesDiscovered",
                            "Service characteristic UUID found: " + mGattService.getUuid().toString());

                    mGattChar =
                            mGattService.getCharacteristic(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"));

                    if (mGattChar != null) {

                        if (gatt.setCharacteristicNotification(mGattChar, true) == true) {
                            Log.d("gatt.setCharacteristicNotification", "SUCCESS!");
                        } else {
                            Log.d("gatt.setCharacteristicNotification", "FAILURE!");
                        }
                        BluetoothGattDescriptor descriptor = mGattChar.getDescriptors().get(0);
                        if (0 != (mGattChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE)) {
                            // It's an indicate characteristic
                            Log.d("onServicesDiscovered", "Characteristic (" + mGattChar.getUuid() + ") is INDICATE");
                            if (descriptor != null) {
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                gatt.writeDescriptor(descriptor);
                            }
                        } else {
                            // It's a notify characteristic
                            Log.d("onServicesDiscovered", "Characteristic (" + mGattChar.getUuid() + ") is NOTIFY");
                            if (descriptor != null) {
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                gatt.writeDescriptor(descriptor);
                            }
                        }
                        Log.i("onServicesDiscovered",
                                "characteristic UUID found: " + mGattChar.getUuid().toString());

                    } else {
                        Log.i("onServicesDiscovered",
                                "characteristic not found for UUID: " + mGattChar.getUuid().toString());

                    }

                } else {
                    Log.i("onServicesDiscovered",
                            "Service characteristic not found for UUID: " + mGattService.getUuid().toString());

                }

                if (progress.isIndeterminate()) progress.dismiss();
            }
        }

    };
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.i("onScanResult", result.getDevice().getAddress());
            Log.i("onScanResult", result.getDevice().getName());

            scanLeDevice(false);

            mBluetoothGatt = result.getDevice().connectGatt(getApplicationContext(), false, mBluetoothGattCallback);
        }
    };

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
        beginBLE();
    }

    public void beginBLE() {
        // BLE
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            scanLeDevice(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        if (timer != null) timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User chose not to enable Bluetooth.
        Log.i("onActivityResult", "requestCode = " + requestCode);
        Log.i("onActivityResult", "resultCode = " + resultCode);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                finish();
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("onRequestPermis...", "coarse location permission granted");
                    beginBLE();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void scanLeDevice(final boolean enable) {
        final BluetoothLeScanner bluetoothLeScanner =
                mBluetoothAdapter.getBluetoothLeScanner();

        if (enable) {
            progress = ProgressDialog.show(this, "Escaneando", "Aguarde un momento por favor");

            bluetoothLeScanner.startScan(mLeScanCallback);
            Log.i("scanLeDevice", "Start scan");

        } else {
            // if (progress.isIndeterminate()) progress.dismiss();

            bluetoothLeScanner.stopScan(mLeScanCallback);
            Log.i("scanLeDevice", "Stop scan");
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
