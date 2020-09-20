package com.jk.rcp.main.activities.instructor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.model.user.Users;
import com.jk.rcp.main.data.model.user.UsersRequestCallbacks;
import com.jk.rcp.main.data.remote.Request;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class CrearCursoActivity extends AppCompatActivity {
    private static final String TAG = "CrearCursoActivity";
    private static List<String> personas;
    private Autocomplete userAutocomplete;
    private User globalUser;
    private EditText edit;

    public static List<String> getPersonas() {
        return personas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_curso);
        globalUser = (User) getApplicationContext();
    personas = new ArrayList<String>();
        // Obtengo los eventos de la API, con el token
        obtenerGenteDisponible(globalUser.getBearerToken());

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nombre = edit.getText().toString().trim();

                if(nombre != null && !nombre.equals("")){
                    personas.add(nombre);
                    Log.d(TAG, personas.toString());
                    edit.setText("");
                }


            }
        });

    }

    private void obtenerGenteDisponible(String token) {
        Request request = new Request();
        request.getUnasignedPeople(token, new UsersRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final List<Users> personas) {
                Log.d(TAG, personas.toString());
                setupUserAutocomplete(personas);
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

    private void setupUserAutocomplete(List<Users> value) {
        edit = findViewById(R.id.practicanteEditText);
        float elevation = 1f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<Users> presenter = new UserPresenter(this, value);
        AutocompleteCallback<Users> callback = new AutocompleteCallback<Users>() {
            @Override
            public boolean onPopupItemClicked(@NonNull Editable editable, @NonNull Users item) {
                editable.clear();
                editable.append(item.getName());
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        userAutocomplete = Autocomplete.<Users>on(edit)
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
