package com.jk.rcp.main.activities.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.rcp.R;
import com.jk.rcp.main.data.adapter.CoursesListAdapter;
import com.jk.rcp.main.data.model.course.Course;
import com.jk.rcp.main.data.model.course.CoursesRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventPost;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.remote.Request;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class MisCursosActivity extends AppCompatActivity {
    private static final String TAG = "MisCursosActivity";
    private User globalUser;
    private ListView coursesList;
    private CoursesListAdapter coursesListAdapter;

    //En esto usar fragmentos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cursos);
        globalUser = (User) getApplicationContext();

        // Obtengo los eventos de la API, con el token
        getCourses(globalUser.getBearerToken());

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ver Cursos");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getCourses(String token) {
        Request request = new Request();
        request.getCourses(token, new CoursesRequestCallbacks() {
            @Override
            public void onSuccess(@NonNull final List<Course> courses) {
                coursesList = findViewById(R.id.coursesList);
                coursesListAdapter = new CoursesListAdapter(getApplicationContext(), courses);
                coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(getApplicationContext(), VerPracticantesActivity.class);
                        intent.putExtra("Course", courses.get(position));
                        startActivity(intent);
                    }
                });
                coursesList.setAdapter(coursesListAdapter);
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
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}

