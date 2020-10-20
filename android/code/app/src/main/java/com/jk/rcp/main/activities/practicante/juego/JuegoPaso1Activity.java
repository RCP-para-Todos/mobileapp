package com.jk.rcp.main.activities.practicante.juego;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.jk.rcp.main.utils.Conversor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.ResponseBody;

public class JuegoPaso1Activity extends AppCompatActivity {
    private static final String TAG = "JuegoPaso1Activity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public ToggleButton toggleButton;
    public TextView textView;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    BluetoothGattCharacteristic mGattChar;
    Timer timer;
    ProgressDialog progress;
    private Button btnJugar;
    private BluetoothGatt mBluetoothGatt;
    private List<Instant> instantes;
    private int mediosSegundos = 0;
    private int count = 1 * 60 * 1000;
    private int antiCount = 0;
    private int puntaje = 0;
    private Boolean inicio = false;
    private Boolean insuflacionesMomento = false;
    private ImageView corazon1;
    private ImageView corazon2;
    private ImageView corazon3;
    private ImageView corazon4;
    private ImageView viento1;
    private ImageView viento2;
    private ImageView viento3;
    private ImageView viento4;
    private TextView puntajeTextView;
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

                        final Runnable modifyIcons = new Runnable() {
                            public void run() {
                                sistemaIconos();
                            }
                        };

                        TimerTask task = new TimerTask() {
                            public void run() {
                                runOnUiThread(modifyIcons);
                            }
                        };

                        //Set timer, read temperature every 2S
                        timer = new Timer();
                        timer.scheduleAtFixedRate(task, 5, 125);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_paso1);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Modo Juego");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        globalUser = (User) getApplicationContext();

        corazon1 = findViewById(R.id.corazon1);
        corazon2 = findViewById(R.id.corazon2);
        corazon3 = findViewById(R.id.corazon3);
        corazon4 = findViewById(R.id.corazon4);
        viento1 = findViewById(R.id.viento1);
        viento2 = findViewById(R.id.viento2);
        viento3 = findViewById(R.id.viento3);
        viento4 = findViewById(R.id.viento4);
        puntajeTextView = findViewById(R.id.textView);

        instantes = new ArrayList<Instant>();

        // toggleButton.setEnabled(false);

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("tiempo") != null) {
            int tiempo = (int) getIntent().getSerializableExtra("tiempo");
            this.count = tiempo;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }

            }
        }
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE no soportado", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "No se banca blueetoth", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


    }

    private void sistemaIconos() {
        if (this.antiCount % 1000 == 0 && !this.insuflacionesMomento && this.antiCount != 0) {
            this.corazon1.setVisibility(View.INVISIBLE);
            this.corazon2.setVisibility(View.INVISIBLE);
            this.corazon3.setVisibility(View.INVISIBLE);
            this.corazon4.setVisibility(View.VISIBLE);
            this.logicaColorearCompresion();
        } else if (this.antiCount % 1000 == 875 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.INVISIBLE);
            this.corazon2.setVisibility(View.INVISIBLE);
            this.corazon3.setVisibility(View.VISIBLE);
            this.corazon4.setVisibility(View.INVISIBLE);
        } else if (this.antiCount % 1000 == 750 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.INVISIBLE);
            this.corazon2.setVisibility(View.VISIBLE);
            this.corazon3.setVisibility(View.INVISIBLE);
            this.corazon4.setVisibility(View.INVISIBLE);
        } else if (this.antiCount % 1000 == 625 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.VISIBLE);
            this.corazon2.setVisibility(View.INVISIBLE);
            this.corazon3.setVisibility(View.INVISIBLE);
            this.corazon4.setVisibility(View.INVISIBLE);
        } else if (this.antiCount % 1000 == 500 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.INVISIBLE);
            this.corazon2.setVisibility(View.INVISIBLE);
            this.corazon3.setVisibility(View.INVISIBLE);
            this.corazon4.setVisibility(View.VISIBLE);
        } else if (this.antiCount % 1000 == 375 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.INVISIBLE);
            this.corazon2.setVisibility(View.INVISIBLE);
            this.corazon3.setVisibility(View.VISIBLE);
            this.corazon4.setVisibility(View.INVISIBLE);
        } else if (this.antiCount % 1000 == 250 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.INVISIBLE);
            this.corazon2.setVisibility(View.VISIBLE);
            this.corazon3.setVisibility(View.INVISIBLE);
            this.corazon4.setVisibility(View.INVISIBLE);
        } else if (this.antiCount % 1000 == 125 && !this.insuflacionesMomento) {
            this.corazon1.setVisibility(View.VISIBLE);
            this.corazon2.setVisibility(View.INVISIBLE);
            this.corazon3.setVisibility(View.INVISIBLE);
            this.corazon4.setVisibility(View.INVISIBLE);
        }
        //Cada condicion representa 30 segundos, cada 30 segundos se paran las compresiones y se realizan las insuflaciones.
        if ((this.antiCount > 29000 && this.antiCount < 34000) || (this.antiCount > 64000 && this.antiCount < 69000) || (this.antiCount > 99000 && this.antiCount < 104000) || (this.antiCount > 134000 && this.antiCount < 139000) || (this.antiCount > 169000 && this.antiCount < 174000) || (this.antiCount > 204000 && this.antiCount < 209000) || (this.antiCount > 239000 && this.antiCount < 244000) || (this.antiCount > 274000 && this.antiCount < 279000) || (this.antiCount > 309000 && this.antiCount < 314000) || (this.antiCount > 344000 && this.antiCount < 349000) || (this.antiCount > 379000 && this.antiCount < 384000) || (this.antiCount > 414000 && this.antiCount < 419000) || (this.antiCount > 449000 && this.antiCount < 454000) || (this.antiCount > 484000 && this.antiCount < 489000)) {
            this.insuflacionesMomento = true;
            this.viento1.setVisibility(View.VISIBLE);
            this.viento2.setVisibility(View.VISIBLE);
            this.viento3.setVisibility(View.VISIBLE);
            this.viento4.setVisibility(View.VISIBLE);
            this.logicaColorearInsuflacion();
        } else {
            this.insuflacionesMomento = false;
            this.viento1.setVisibility(View.INVISIBLE);
            this.viento2.setVisibility(View.INVISIBLE);
            this.viento3.setVisibility(View.INVISIBLE);
            this.viento4.setVisibility(View.INVISIBLE);
        }
        this.antiCount = this.antiCount + 125;

        if (this.antiCount >= this.count) {
            Log.d(TAG, "Termine!");
            timer.cancel();
            if (mBluetoothGatt != null) {
                mBluetoothGatt.close();
            }
            subirEvento();
        }
    }

    private void logicaColorearInsuflacion() {
        Instant instante = this.instantes.get(this.instantes.size() - 1);

        if (instante.getInsuflacion() == "Nula") {
            corazon4.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje - 60;
        } else if (instante.getInsuflacion() == "Insuficiente") {
            corazon4.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje - 60;
        } else if (instante.getInsuflacion() == "Correcta") {
            corazon4.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje + 50;
        } else if (instante.getInsuflacion() == "Excesiva") {
            corazon4.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje - 60;
        }
    }

    private void logicaColorearCompresion() {
        Instant instante = this.instantes.get(this.instantes.size() - 1);
        if (instante.getCompresion() == "Nula") {
            corazon4.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje - 40;
        } else if (instante.getCompresion() == "Insuficiente") {
            corazon4.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje - 40;
        } else if (instante.getCompresion() == "Correcta") {
            corazon4.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje + 100;
        } else if (instante.getCompresion() == "Excesiva") {
            corazon4.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            this.puntaje = this.puntaje - 40;
        }
        if (this.puntaje < 0) {
            this.puntaje = 0;
        }
        puntajeTextView.setText("Juego RCP        " + String.valueOf(this.puntaje));
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
                        Intent intent = new Intent(JuegoPaso1Activity.this, JuegoPaso2Activity.class);
                        intent.putExtra("puntaje", puntaje);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        scanLeDevice(true);
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
        scanLeDevice(false);
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
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, device.getName().toString());
                            if (device.getName().equals("ESP32")) {
                                mBluetoothGatt = device.connectGatt(getApplicationContext(), false, mBluetoothGattCallback);
                            }
                        }
                    });
                }
            };

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
