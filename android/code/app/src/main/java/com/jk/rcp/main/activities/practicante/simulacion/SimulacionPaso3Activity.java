package com.jk.rcp.main.activities.practicante.simulacion;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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
import com.jk.rcp.main.bluetooth.SerialListener;
import com.jk.rcp.main.bluetooth.SerialService;
import com.jk.rcp.main.bluetooth.SerialSocket;
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

import okhttp3.ResponseBody;

public class SimulacionPaso3Activity extends AppCompatActivity implements ServiceConnection, SerialListener {
    private static final String TAG = "SimulacionPaso3Activity";
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

    private enum Connected {False, Pending, True}

    private String deviceAddress;
    private SerialService service;

    private Connected connected = Connected.False;
    private boolean initialStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulacion_paso3);
        bindService(new Intent(getApplicationContext(), SerialService.class), this, Context.BIND_AUTO_CREATE);

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
        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("device") != null) {
            deviceAddress = (String) getIntent().getSerializableExtra("device");
        }
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
                        disconnect();
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

    @Override
    protected void onPause() {
        super.onPause();

        disconnect();
        if (timer != null) timer.cancel();
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
                disconnect();
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
            fn_countdown();
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
