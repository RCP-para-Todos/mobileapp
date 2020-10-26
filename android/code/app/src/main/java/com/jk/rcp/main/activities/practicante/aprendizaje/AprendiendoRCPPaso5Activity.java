package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.bluetooth.SerialListener;
import com.jk.rcp.main.bluetooth.SerialService;
import com.jk.rcp.main.bluetooth.SerialSocket;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.utils.AlarmManager;
import com.jk.rcp.main.utils.Constants;
import com.jk.rcp.main.utils.Conversor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static androidx.lifecycle.Lifecycle.State.STARTED;

public class AprendiendoRCPPaso5Activity extends AppCompatActivity implements ServiceConnection, SerialListener {
    private static final String TAG = "AprendiendoRCPActivity";
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
    private List<Instant> instantes;
    private int mediosSegundos = 0;
    private int contadorX = 0;
    private ProgressDialog progress;
    private int iteracion = 0;

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
        setContentView(R.layout.activity_aprender_rcp_paso5);
        bindService(new Intent(getApplicationContext(), SerialService.class), this, Context.BIND_AUTO_CREATE);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        corazon1 = findViewById(R.id.corazon1);
        corazon2 = findViewById(R.id.corazon2);
        corazon3 = findViewById(R.id.corazon3);
        corazon4 = findViewById(R.id.corazon4);
        corazon5 = findViewById(R.id.corazon5);
        progressBarView = findViewById(R.id.view_progress_bar);
        tv_time = findViewById(R.id.tv_timer);
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fn_countdown();
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

        instantes = new ArrayList<Instant>();
        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("device") != null) {
            deviceAddress = (String) getIntent().getSerializableExtra("device");
        }
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

        if (this.instantes.size() > 0 && (compresionesCorrectas / this.instantes.size()) > Constants.APRENDIZAJE_PORCENTAJE_COMPRESIONES_VALIDAS) {
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
                            intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                            disconnect();
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
                            intent.putExtra("device", (String) getIntent().getSerializableExtra("device"));
                            disconnect();
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
        ImageView corazonSelecto = null;
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
                corazonSelecto = corazon5;
                break;
        }
        iteracion++;
        if (corazonSelecto != null) {
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

    /// COPIA

    @Override
    public void onDestroy() {
        try {
            unbindService(this);
        } catch (Exception ignored) {
        }
        if (connected != Connected.False)
            disconnect();
        stopService(new Intent(this, SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "en el start");
        if (service != null) {
            Log.d(TAG, "No es null");
            service.attach(this);
        } else {
            Log.d("SERVICE", "Arranco el serviico");
            startService(new Intent(this, SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
        }
    }

    @Override
    public void onStop() {
        if (service != null && !isChangingConfigurations())
            service.detach();
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (initialStart && service != null) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart) {
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
        tratamientoRecepcionBluetooth(splitted);
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
