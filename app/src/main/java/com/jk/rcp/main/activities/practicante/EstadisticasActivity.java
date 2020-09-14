package com.jk.rcp.main.activities.practicante;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.event.EventRequestCallbacks;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class EstadisticasActivity extends AppCompatActivity {
    private static final String TAG = "EstadisticasActivity";
    private User globalUser;

    //En esto usar fragmentos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        globalUser = (User) getApplicationContext();
        Log.d(TAG, globalUser.toString());

        // Obtengo los eventos de la API, con el token
        getEvents(globalUser.getBearerToken());
    }

    public void getEvents(String token) {
        Request request = new Request();
        request.getEvents(token, new EventRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull List<Event> value) {
                for (Event evento : value
                ) {
                    Log.d(TAG, evento.toString());
                }
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
}
