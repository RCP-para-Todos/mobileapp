package com.jk.rcp.main.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jk.rcp.main.sensors.accelerometer.ShakeDetector;
import com.jk.rcp.main.sensors.gyroscope.RotationDetector;
import com.jk.rcp.main.sensors.light.LightDetector;
import com.jk.rcp.main.utils.AlarmManager;
import com.jk.rcp.main.utils.Constants;
import com.jk.rcp.main.utils.EventManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ShakeService extends Service implements ShakeDetector.Listener, LightDetector.Listener, RotationDetector.Listener {
    private final static String TAG = "SERVICE";
    private boolean noHayRotacion;
    private boolean luzApagada;
    private boolean hayMovimiento;
    private ShakeDetector sd;
    private LightDetector ld;
    private RotationDetector rd;
    private SharedPreferences prefs;
    private AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        alarmManager = AlarmManager.getInstance();
        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        sd = new ShakeDetector(this);
        sd.start(sensorManager);

        ld = new LightDetector(this);
        ld.start(sensorManager);

        rd = new RotationDetector(this);
        rd.start(sensorManager);
    }

    @Override
    public void onDestroy() {
        sd.stop();
        ld.stop();
        rd.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void hearShake() {
        this.hayMovimiento = true;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hayMovimiento = false;
            }
        }, 2000);
        verificacionSismo();
    }

    @Override
    public void senseNoLight() {
        this.luzApagada = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                luzApagada = false;
            }
        }, 2000);
        verificacionSismo();
    }

    @Override
    public void noRotation() {
        this.noHayRotacion = true;
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                noHayRotacion = false;
            }
        }, 2000);
        verificacionSismo();
    }


    public void verificacionSismo() {
//        Log.d(TAG, "noHayRotacion " + noHayRotacion + " - luzApagada " + luzApagada + " - hayMovimiento " + hayMovimiento);

        if (noHayRotacion && luzApagada && hayMovimiento) {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            //Con esto traigo a primer plano la app
            Intent intent = packageManager.getLaunchIntentForPackage("com.jk.sismos");
            if (intent != null) {
                intent.setPackage(null);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                getApplicationContext().startActivity(intent);

                //Aviso que hay que mostrar la pantalla de alerta
                Intent retIntent = new Intent("earthquake");
                sendBroadcast(retIntent);
            }

            Gson gson = new Gson();
            ArrayList<String> textList = null;

            if (prefs.contains("history")) {
                String jsonText = prefs.getString("history", null);
//                Log.d(TAG, "HISTORIAL: " + jsonText);
                textList = new ArrayList<>(Arrays.asList(gson.fromJson(jsonText, String[].class)));
            } else {
                textList = new ArrayList<String>();
            }

            if (textList.size() > 0) {
                //Compruebo cuando sucedio el ultimo, para diferenciar si se trata o no del mismo evento
                String item = textList.get(textList.size() - 1);
                long lastEpochTime = Long.parseLong(item.split("-")[0]);
                long actualTime = System.currentTimeMillis();

                //Si el ultimo registro fue hace menos de un minuto, se trata del mismo evento
                if (actualTime - lastEpochTime > 30 * 1000) {
                    crearRegistro(textList, gson);
                }
            } else {
                crearRegistro(textList, gson);
            }
//            alarmManager.startSound(this, "alerta.mp3", true, false);
        }
    }

    public void crearRegistro(ArrayList<String> list, Gson gson) {
        //Creo un nuevo registro
        long actualTime = System.currentTimeMillis();
        String data = actualTime + "-Movimiento detectado";
        list.add(data);
        String jsonText = gson.toJson(list);
        prefs.edit().putString("history", jsonText).apply();
        EventManager.registerEvent(Constants.EARTHQUAKE_DETECTED);
    }


}
