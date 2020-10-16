package com.jk.rcp.main.activities.practicante.simulacion;

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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.event.EventRequestCallbacks;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;
import com.jk.rcp.main.utils.Constants;
import com.jk.rcp.main.utils.Conversor;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.ResponseBody;

public class SimulacionPaso3Activity extends AppCompatActivity {
    private static final String TAG = "SimulacionPaso3Activity";
    // Bluetooth
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 200;
    BluetoothGattCharacteristic mGattChar;
    //Countdown
    Timer timer;
    int totalSeconds = Constants.SIMULACION_DURACION_SEGUNDOS_PASO3;
    //Compresiones e Insuflaciones correctas.
    int compresionesCorrectas = 0;
    int insuflacionesCorrectas = 0;
    private CountDownTimer countDownTimer;
    private TextView labelTimerCountdown;
    private ImageView corazon;
    private ImageView viento;
    private TextView labelCountCompresiones;
    private TextView labelCountInsuflasiones;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private ProgressDialog progressDialog;
    private int progress;
    private int endTime;
    //Manejo de instantes.
    private List<Instant> instantes;
    private int mediosSegundos = 0;

    // Opciones seleccionadas desde paso 2
    private boolean elEntornoEsSeguro = false;
    private boolean ambulanciaClicked = false;
    private boolean entornoNoSeguroClicked = false;

    private User globalUser;
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

                        fn_countdown();

                    } else {
                        Log.i("onServicesDiscovered",
                                "characteristic not found for UUID: " + mGattChar.getUuid().toString());

                    }

                } else {
                    Log.i("onServicesDiscovered",
                            "Service characteristic not found for UUID: " + mGattService.getUuid().toString());

                }

                if (progressDialog.isIndeterminate()) progressDialog.dismiss();
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
        setContentView(R.layout.activity_simulacion_paso3);

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        globalUser = (User) getApplicationContext();

        labelTimerCountdown = findViewById(R.id.labelTimerCountdown);
        labelCountCompresiones = findViewById(R.id.labelCountCompresiones);
        labelCountInsuflasiones = findViewById(R.id.labelCountInsfulaciones);
        corazon = findViewById(R.id.corazon);
        viento = findViewById(R.id.viento);

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
        if (getIntent().getExtras() != null &&
                getIntent().getSerializableExtra("elEntornoEsSeguro") != null &&
                getIntent().getSerializableExtra("ambulanciaClicked") != null &&
                getIntent().getSerializableExtra("entornoNoSeguroClicked") != null
        ) {
            this.entornoNoSeguroClicked = (boolean) getIntent().getSerializableExtra("entornoNoSeguroClicked");
            this.elEntornoEsSeguro = (boolean) getIntent().getSerializableExtra("elEntornoEsSeguro");
            this.ambulanciaClicked = (boolean) getIntent().getSerializableExtra("ambulanciaClicked");
        }

        instantes = new ArrayList<Instant>();

        beginBLE();
    }

    private void fn_countdown() {
        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }

        endTime = this.totalSeconds;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000) % 60;

                        if (seconds == 0) {
                            labelTimerCountdown.setText("00:00:00 (E.T.A.)");
                        } else {
                            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                            String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                            labelTimerCountdown.setText(formatted + " (E.T.A.)");
                        }
                    }

                    @Override
                    public void onFinish() {
                        labelTimerCountdown.setText("00:00:00 (E.T.A.)");
                        subirEvento();

                    }
                }.start();
            }
        });
    }

    private void subirEvento() {
        //DATOS DEL EVENTO
        String usuarioActivo = globalUser.getUsername();
        String curso = globalUser.getCourses().get(0).getId();
        int tiempoTotalManiobra = Instant.tiempoTotalManiobra(this.instantes);
        String tipo = "juego";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String event_date = dateFormat.format(date);

        Request request = new Request();
        request.crearEvento(
                usuarioActivo,
                curso,
                Instant.tiempoTotalManiobra(this.instantes),
                tipo,
                event_date,
                instantes,
                Instant.tiempoTotalInactividad(this.instantes),
                Instant.porcentajeTotalSobreVida(this.instantes),
                Instant.calidadInsuflaciones(this.instantes),
                Instant.porcentajeInsuflacionesCorrectas(this.instantes),
                Instant.porcentajeCompresionesCorrectas(this.instantes),
                Instant.cantidadInsuflacionesCorrectasPosicionCabeza(this.instantes),
                Instant.fuerzaPromedioAplicada(this.instantes),
                globalUser.getBearerToken(),

                new EventRequestCallbacks() {
                    @Override
                    public void onSuccess(@NonNull final Event event) {
//                        Toast.makeText(getApplicationContext(), "Evento creado correctamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SimulacionPaso3Activity.this, SimulacionEstadisticasActivity.class);

                        intent.putExtra("instantes", (Serializable) instantes);

                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Ocurrió un error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onErrorBody(@NonNull ResponseBody errorBody) {
                        if (errorBody != null) {
                            JsonParser parser = new JsonParser();
                            JsonElement mJson = null;
                            try {
                                mJson = parser.parse(errorBody.string());
                                Gson gson = new Gson();
                                EventPost errorResponse = gson.fromJson(mJson, EventPost.class);

                                Toast.makeText(getApplicationContext(), "Ocurrió un error", Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
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
            this.progressDialog = ProgressDialog.show(this, "Escaneando", "Aguarde un momento por favor");

            bluetoothLeScanner.startScan(mLeScanCallback);
            Log.i("scanLeDevice", "Start scan");

        } else {
            // if (progressDialog.isIndeterminate()) progressDialog.dismiss();

            bluetoothLeScanner.stopScan(mLeScanCallback);
            Log.i("scanLeDevice", "Stop scan");
        }
    }

    private void tratamientoRecepcionBluetooth(String[] datosCorrectos) {
        Log.d(TAG, "ReciboBluetoothPaso3Simulacion");
        String insuflacion = Conversor.insuflacionToString(Integer.valueOf(datosCorrectos[0]));
        String compresion = Conversor.compresionToString(Integer.valueOf(datosCorrectos[1]));
        String posicion = Conversor.posicionToString(Integer.valueOf(datosCorrectos[2]));
        String posicionCabeza = Conversor.posicionCabezaToString(Integer.valueOf(datosCorrectos[3]));

        final Instant instante = new Instant(insuflacion, compresion, posicion, posicionCabeza);

        //Agregado de instante al vector.
        this.instantes.add(instante);

        //Manejo de variables.
        this.mediosSegundos += 1;

        logicaEntornoNoSeguro(instante);

        //Manejo de variables.
        this.mediosSegundos += 1;
        final Runnable modifyIcons = new Runnable() {
            public void run() {
                //Manejo de graficos.
                manejarGrafico(instante);
            }
        };

        TimerTask task = new TimerTask() {
            public void run() {
                runOnUiThread(modifyIcons);
            }
        };

        timer = new Timer();
        timer.schedule(task, 5);
    }

    /*
    Si el entorno es no seguro, y el usuario no lo declara como no seguro se llega a esta pantalla de todas formas.
    Se desprenden 4 posibles flujos:
    1)El entorno no es seguro, no se marca como no seguro, no se llama a la ambulancia y se realiza alguna accion, se sale inmediatamente de esta pantalla.
    2)El entorno no es seguro, no se marca como no seguro, se llama a la ambulancia y se realiza alguna accion,
       se sale inmediatamente de esta pantalla.
    3) El entorno no es seguro, no se marca como no seguro, no se llama a la ambulancia y no se realiza ninguna accion (raro), se espera el tiempo y al final se informa.
    4) El entorno no es seguro, no se marca como no seguro, se llama a la ambulancia y no se realiza ninguna accion (raro), se espera el tiempo y al final se informa.
    */
    private void logicaEntornoNoSeguro(Instant instante) {
        if (this.entornoNoSeguroClicked && !this.elEntornoEsSeguro) {
            if (!instante.getCompresion().equals("Nula") || !instante.getInsuflacion().equals("Nula")) {
                // Finalizo el bluetooth
                if (mBluetoothGatt == null) {
                    return;
                }
                mBluetoothGatt.close();
                mBluetoothGatt = null;

                //Voy a la otra activity
                Intent intent = new Intent(SimulacionPaso3Activity.this, SimulacionEstadisticasActivity.class);
                startActivity(intent);
            }
        }
    }

    private void manejarGrafico(Instant instante) {
        this.corazon.setVisibility(View.INVISIBLE);
        this.viento.setVisibility(View.INVISIBLE);

        if (instante.getCompresion().equals("Nula")) {
            this.corazon.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else if (instante.getCompresion().equals("Insuficiente")) {
            this.corazon.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            this.corazon.setVisibility(View.VISIBLE);
        } else if (instante.getCompresion().equals("Correcta")) {
            this.corazon.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            this.compresionesCorrectas = this.compresionesCorrectas + 1;
            this.labelCountCompresiones.setText(this.compresionesCorrectas + " Compresiones");
        } else if (instante.getCompresion().equals("Excesiva")) {
            this.corazon.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else {
            this.corazon.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        if (instante.getCompresion().equals("Nula")) {
            this.viento.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else if (instante.getCompresion().equals("Insuficiente")) {
            this.viento.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        } else if (instante.getCompresion().equals("Correcta")) {
            this.viento.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            this.insuflacionesCorrectas = this.insuflacionesCorrectas + 1;
            this.labelCountInsuflasiones.setText(this.insuflacionesCorrectas + " Insuflaciones");
        } else if (instante.getCompresion().equals("Excesiva")) {
            this.viento.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else {
            this.viento.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        final Runnable hideIcons = new Runnable() {
            public void run() {
                corazon.setVisibility(View.INVISIBLE);
                viento.setVisibility(View.INVISIBLE);
            }
        };

        TimerTask task = new TimerTask() {
            public void run() {
                runOnUiThread(hideIcons);
            }
        };

        timer = new Timer();
        timer.schedule(task, 250);
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
