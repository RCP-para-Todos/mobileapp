package com.jk.rcp.main.activities.practicante.juego;

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
import com.jk.rcp.main.bluetooth.SerialListener;
import com.jk.rcp.main.bluetooth.SerialService;
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

import okhttp3.ResponseBody;

public class JuegoPaso1Activity extends AppCompatActivity implements ServiceConnection, SerialListener {
    private static final String TAG = "JuegoPaso1Activity";
    public TextView textView;
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

    private enum Connected {False, Pending, True}

    private String deviceAddress;
    private SerialService service;

    private TextView receiveText;
    private TextView sendText;

    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean hexEnabled = false;
    private boolean pendingNewline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, SerialService.class), this, Context.BIND_AUTO_CREATE);
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

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("tiempo") != null) {
            int tiempo = (int) getIntent().getSerializableExtra("tiempo");
            this.count = tiempo;
        }

        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("device") != null) {
            deviceAddress =  (String) getIntent().getSerializableExtra("device");
        }

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
            disconnect();
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
        if(this.instantes.size() > 0) {
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
    protected void onPause() {
        super.onPause();

        if (timer != null) timer.cancel();
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
