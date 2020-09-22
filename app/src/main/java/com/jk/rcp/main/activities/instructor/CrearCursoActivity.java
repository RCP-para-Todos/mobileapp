package com.jk.rcp.main.activities.instructor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.adapter.StudentsListAdapter;
import com.jk.rcp.main.data.model.course.Course;
import com.jk.rcp.main.data.model.course.CourseRequestCallbacks;
import com.jk.rcp.main.data.model.course.Student;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.model.user.Users;
import com.jk.rcp.main.data.model.user.UsersRequestCallbacks;
import com.jk.rcp.main.data.remote.Request;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePresenter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;

public class CrearCursoActivity extends AppCompatActivity {
    private static final String TAG = "CrearCursoActivity";
    private static List<Student> personas;
    private Autocomplete userAutocomplete;
    private User globalUser;
    private EditText edit;
    private ListView studentsList;
    private StudentsListAdapter studentsListAdapter;
    private List<Users> usersWithoutCourse;
    private EditText courseName;

    public static List<Student> getPersonas() {
        return personas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_curso);
        globalUser = (User) getApplicationContext();
        personas = new ArrayList<Student>();
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
                if (nombre != null && !nombre.equals("")) {
                    personas.add(new Student(nombre));
                    edit.setText("");
                    studentsListAdapter.notifyDataSetChanged();
                }
            }
        });

        studentsList = findViewById(R.id.studentsList);
        studentsListAdapter = new StudentsListAdapter(getApplicationContext(), personas);
        studentsList.setAdapter(studentsListAdapter);

        courseName = findViewById(R.id.nombreCursoEditText);
        Button btnCrearCurso = findViewById(R.id.btnCrearCurso);
        btnCrearCurso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (personas.size() > 0 && !courseName.getText().toString().equals("")) {
                    List<String> userIds = new ArrayList<String>();
                    for (Student student : personas
                    ) {
                        String id = getId(usersWithoutCourse, student.getName());
                        if (!id.equals("")) {
                            userIds.add(id);
                        }
                    }
                    crearCurso(courseName.getText().toString(), userIds, globalUser.getUsername(), globalUser.getBearerToken());
                }

            }
        });
    }


    public static String getId(List<Users> c, String name) {
        for (Users o : c) {
            if (o != null && o.getName().equals(name)) {
                return o.getName();
            }
        }
        return "";
    }

    private void crearCurso(String nombre, List<String> userIds, String nombreInstructor, String token) {
        Request request = new Request();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String fechaActual = dateFormat.format(date);
        request.crearCurso(nombre, fechaActual, nombreInstructor, userIds, token, new CourseRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final Course course) {
                Toast.makeText(getApplicationContext(), "Curso creado correctamente", Toast.LENGTH_LONG).show();
                finish();
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

    private void obtenerGenteDisponible(String token) {
        Request request = new Request();
        request.getUnasignedPeople(token, new UsersRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final List<Users> personas) {
                setupUserAutocomplete(personas);
                usersWithoutCourse = personas;
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
