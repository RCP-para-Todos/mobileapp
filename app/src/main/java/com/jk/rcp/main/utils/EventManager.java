package com.jk.rcp.main.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jk.rcp.main.data.remote.Request;

import static android.content.Context.MODE_PRIVATE;

public class EventManager {
    private static SharedPreferences prefs;
    private static Request request;
    private Context context;

    public EventManager(Context context) {
        this.context = context;
        prefs = this.context.getSharedPreferences("preferences", MODE_PRIVATE);
        request = new Request();
    }

    public static void registerEvent(Integer event) {
        String typeEvents = "";
        String description = "";
        String state = "";
        String token = prefs.getString("token", "");

        switch (event) {
            case Constants.LOGIN_CORRECT:
                typeEvents = "Login";
                description = "Usuario logueado correctamente";
                state = "ACTIVO";
                break;
            case Constants.LOGOUT_CORRECT:
                typeEvents = "Login";
                description = "Usuario deslogueado correctamente";
                state = "INACTIVO";
                break;
            case Constants.USER_REGISTERED:
                typeEvents = "Registro";
                description = "Usuario registrado";
                state = "INACTIVO";
                break;
            case Constants.USER_COULDNT_REGISTER:
                typeEvents = "Registro";
                description = "Usuario tuvo un error al registrarse";
                state = "INACTIVO";
                break;
            case Constants.EARTHQUAKE_BROADCAST_RECEIVER_INITIATED:
                typeEvents = "Broadcast Receiver";
                description = "Broadcast Receiver para iniciar la pantalla de alerta activado";
                state = "ACTIVO";
                break;
            case Constants.EARTHQUAKE_DETECTED:
                typeEvents = "Sensor";
                description = "Se detecta un movimiento utilizando el acelerometro, giroscopio y el sensor de luz";
                state = "ACTIVO";
                break;
            case Constants.EARTHQUAKE_DETECTED_FINISHED:
                typeEvents = "Sensor";
                description = "Se apaga la alarma de sismo";
                state = "INACTIVO";
                break;
            case Constants.SERVICE_ACTIVATED:
                typeEvents = "Service";
                description = "Se activa el service de deteccion de sismos";
                state = "ACTIVO";
                break;
            case Constants.GPS_ACQUIRED:
                typeEvents = "Sensor";
                description = "Se utiliza el GPS para obtener las coordenadas";
                state = "ACTIVO";
                break;
            case Constants.GET_INPRES_DATA_ACTIVATED:
                typeEvents = "Conexión";
                description = "Se obtienen datos de la web de INPRES";
                state = "ACTIVO";
                break;
            case Constants.GET_INPRES_DATA_FINISHED:
                typeEvents = "Conexión";
                description = "Finaliza la obtencion de datos de la web de INPRES";
                state = "INACTIVO";
                break;
            case Constants.DETECT_DEVICE_ONLINE:
                typeEvents = "Internet";
                description = "Se detecta si el dispositivo esta conectado a internet";
                state = "ACTIVO";
                break;
        }

        if (!typeEvents.equals("") && !description.equals("") && !state.equals("") && !token.equals("")) {
            request.registerEvent(token, typeEvents, state, description);
        }
    }
}
