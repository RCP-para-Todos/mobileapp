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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.jk.rcp.main.utils.AlarmManager;
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

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class AprendiendoRCPPaso7Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPPaso7Activity";
    // Bluetooth
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 200;
    BluetoothGattCharacteristic mGattChar;
    //Countdown
    Timer timer;
    int totalSeconds = Constants.APRENDIZAJE_DURACION_SEGUNDOS_FINAL;
    //Compresiones e Insuflaciones correctas.
    int compresionesCorrectas = 0;
    int insuflacionesCorrectas = 0;
    private ProgressBar progressBarView;
    private CountDownTimer countDownTimer;
    private ImageView corazon;
    private TextView tv_time;
    private ImageView viento;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private ProgressDialog progressDialog;
    private int progress;
    private int endTime;
    //Manejo de instantes.
    private List<Instant> instantes;
    private int mediosSegundos = 0;
    private int progressCount = 0;

    private AlarmManager alarmManager;

    // Opciones seleccionadas desde paso 2
    private boolean elEntornoEsSeguro = false;
    private boolean ambulanciaClicked = false;
    private boolean entornoNoSeguroClicked = false;

    private User globalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso7);
        tv_time = findViewById(R.id.tv_timer_paso7);
        progressBarView = findViewById(R.id.view_progress_bar_paso7);

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        globalUser = (User) getApplicationContext();

//        labelTimerCountdown = findViewById(R.id.labelTimerCountdown);
//        labelCountCompresiones = findViewById(R.id.labelCountCompresiones);
//        labelCountInsuflasiones = findViewById(R.id.labelCountInsfulaciones);
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

        instantes = new ArrayList<Instant>();
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
        alarmManager.startSound(this, "AprendizajeAudio.mp3", false, true);
    }

    private void fn_countdown() {
        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }

        this.progressCount = 1;
        endTime = this.totalSeconds;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        setProgress(progressCount, endTime);
                        progressCount = progressCount + 1;
                        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                        int seconds = (int) (millisUntilFinished / 1000) % 60;
                        if (minutes > 0) {
                            tv_time.setText(minutes + " mins, " + seconds + " secs");
                        } else {
                            if (seconds == 0) {
                                tv_time.setText("0 secs");
                            } else {
                                tv_time.setText(seconds + " secs");
                            }
                        }
                    }

                    @Override
                    public void onFinish() {

                        tv_time.setText("0 secs");
                        setProgress(progressCount, endTime);
                        subirEvento();

                    }
                }.start();
            }
        });
    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);
        Log.d(TAG, "progreso" + startTime);
    }

    private void subirEvento() {
        //DATOS DEL EVENTO
        String usuarioActivo = globalUser.getUsername();
        String curso = globalUser.getCourses().get(0).getId();
        int tiempoTotalManiobra = Instant.tiempoTotalManiobra(this.instantes);
        String tipo = "aprendizaje";
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
                        Intent intent = new Intent(AprendiendoRCPPaso7Activity.this, AprendiendoRCPPaso8Activity.class);

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
                Intent intent = new Intent(AprendiendoRCPPaso7Activity.this, AprendiendoRCPPaso8Activity.class);
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
        } else if (instante.getCompresion().equals("Correcta")) {
            this.corazon.setVisibility(View.VISIBLE);
            corazon.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            this.compresionesCorrectas = this.compresionesCorrectas + 1;
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
