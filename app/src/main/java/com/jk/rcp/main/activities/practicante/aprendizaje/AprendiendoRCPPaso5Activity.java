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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.utils.AlarmManager;
import com.jk.rcp.main.utils.Constants;
import com.jk.rcp.main.utils.Conversor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static androidx.lifecycle.Lifecycle.State.STARTED;

public class AprendiendoRCPPaso5Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 200;
    BluetoothGattCharacteristic mGattChar;
    Timer timer;
    private AlarmManager alarmManager;
    private ProgressBar progressBarView;
    private TextView tv_time;
    private int progressCount;
    private CountDownTimer countDownTimer;
    private int endTime = 250;
    private int myProgress = 0;
    private AlertDialog alert11;
    private ImageView corazon1;
    private ImageView corazon2;
    private ImageView corazon3;
    private ImageView corazon4;
    private ImageView corazon5;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private List<Instant> instantes;
    private int mediosSegundos = 0;
    private int contadorX = 0;
    private ProgressDialog progress;
    private int iteracion = 0;
    public final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String s = new String(data);
            String converted = s.substring(0, 8);
            String[] splitted = converted.split(";");
            Log.d(TAG, "Recibo ESP32: " + converted);
            tratamientoRecepcionBluetooth(splitted);
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
                        runOnUiThread(new Runnable() {
                            public void run() {
                                fn_countdown();
                            }
                        });
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


            scanLeDevice(false);

            mBluetoothGatt = result.getDevice().connectGatt(getApplicationContext(), false, mBluetoothGattCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso5);

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        corazon1 = findViewById(R.id.corazon1);
        corazon2 = findViewById(R.id.corazon2);
        corazon3 = findViewById(R.id.corazon3);
        corazon4 = findViewById(R.id.corazon4);
        corazon5 = findViewById(R.id.corazon3);
        progressBarView = findViewById(R.id.view_progress_bar);
        tv_time = findViewById(R.id.tv_timer);
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                beginBLE();
            }
        };

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        alarmManager = new AlarmManager(onCompletionListener);
        alarmManager.startSound(this, "CompresionManiobraAudio.mp3", false, true);

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

        instantes = new ArrayList<Instant>();
    }

    private void fn_countdown() {
        myProgress = 0;

        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }

        progressCount = 1;
        endTime = 30;
        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgress(progressCount, endTime);
                progressCount = progressCount + 1;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                if (seconds == 0) {
                    tv_time.setText("0 secs");
                } else {
                    tv_time.setText(seconds + " secs");
                }
            }

            @Override
            public void onFinish() {
                if (getLifecycle().getCurrentState().isAtLeast(STARTED)
                ) {
                    tv_time.setText("0 secs");
                    setProgress(progressCount, endTime);
                    logicaEvaluacionCompresiones();
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.close();
                    }
                } else {
                    cancel();
                }
            }
        };
        Log.d(TAG, "arranca");
        countDownTimer.start();
    }

    private void logicaEvaluacionCompresiones() {
        int compresionesCorrectas = 0;

        for (Instant i : instantes
        ) {
            if (i.getCompresion().equals("Correcta")) {
                compresionesCorrectas += 1;
            }
        }

        if ((compresionesCorrectas / this.instantes.size()) > Constants.APRENDIZAJE_PORCENTAJE_COMPRESIONES_VALIDAS) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Aprendizaje de compresiones realizado correctamente");
            builder1.setTitle("Compresiones Finalizadas");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            progressBarView.setVisibility(View.GONE);
                            countDownTimer.cancel();
                            Intent intent = new Intent(AprendiendoRCPPaso5Activity.this, AprendiendoRCPPaso6Activity.class);
                            startActivity(intent);
                        }
                    });


            alert11 = builder1.create();
            alert11.show();
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Aprendizaje de compresiones fallido, vuelva a intentar");
            builder1.setTitle("Compresiones Finalizadas");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            progressBarView.setVisibility(View.GONE);
                            countDownTimer.cancel();
                            Intent intent = new Intent(AprendiendoRCPPaso5Activity.this, AprendiendoRCPPaso6Activity.class);
                            startActivity(intent);
                        }
                    });


            alert11 = builder1.create();
            alert11.show();
        }

    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);
        Log.d(TAG, "progreso" + startTime);
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
            this.progress = ProgressDialog.show(this, "Escaneando", "Aguarde un momento por favor");

            bluetoothLeScanner.startScan(mLeScanCallback);
            Log.i("scanLeDevice", "Start scan");

        } else {
            // if (progress.isIndeterminate()) progress.dismiss();

            bluetoothLeScanner.stopScan(mLeScanCallback);
            Log.i("scanLeDevice", "Stop scan");
        }
    }

    private void tratamientoRecepcionBluetooth(String[] datosCorrectos) {
        Log.d(TAG, "ReciboBluetoothPaso2Juego");
        String insuflacion = Conversor.insuflacionToString(Integer.valueOf(datosCorrectos[0]));
        String compresion = Conversor.compresionToString(Integer.valueOf(datosCorrectos[1]));
        String posicion = Conversor.posicionToString(Integer.valueOf(datosCorrectos[2]));
        String posicionCabeza = Conversor.posicionCabezaToString(Integer.valueOf(datosCorrectos[3]));

        Instant instante = new Instant(insuflacion, compresion, posicion, posicionCabeza);

        //Agregado de instante al vector.
        this.instantes.add(instante);

        //Manejo de variables.
        this.mediosSegundos += 1;

        if (this.mediosSegundos < 60) {
            final Runnable manejarGraficos = new Runnable() {
                public void run() {
                    manejarGraficoAccion();
                }
            };

            TimerTask task = new TimerTask() {
                public void run() {
                    runOnUiThread(manejarGraficos);
                }
            };

            task.run();

        }
    }

    private void manejarGraficoAccion() {
        if (mediosSegundos % 10 == 0) {
            int instantesIndice = this.instantes.size() - 1;
            Instant i1 = this.instantes.get(instantesIndice - 1);
            Instant i2 = this.instantes.get(instantesIndice);
            tratamientoInstantes(i1, i2);
        }
    }

    private void tratamientoInstantes(Instant i1, Instant i2) {
        ImageView corazonSelecto;
        switch (this.iteracion) {
            case 1:
                corazonSelecto = corazon1;
                break;
            case 2:
                corazonSelecto = corazon2;
                break;
            case 3:
                corazonSelecto = corazon3;
                break;
            case 4:
                corazonSelecto = corazon4;
                break;
            case 5:
            default:
                corazonSelecto = corazon5;
                break;
        }
        iteracion++;

        if (i1.getCompresion() == "Nula") {
            corazonSelecto.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else if (i1.getCompresion() == "Insuficiente") {
            corazonSelecto.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        } else if (i1.getCompresion() == "Correcta") {
            corazonSelecto.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        } else if (i1.getCompresion() == "Excesiva") {
            corazonSelecto.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }

        corazonSelecto.setVisibility(View.VISIBLE);
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
