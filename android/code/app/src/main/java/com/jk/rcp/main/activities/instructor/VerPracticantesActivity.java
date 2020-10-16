package com.jk.rcp.main.activities.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.common.EstadisticasActivity;
import com.jk.rcp.main.data.adapter.StudentsListAdapter;
import com.jk.rcp.main.data.model.course.Course;

public class VerPracticantesActivity extends AppCompatActivity {
    private static final String TAG = "VerPracticantesActivity";
    private Course course;
    private ListView studentsList;
    private StudentsListAdapter studentsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_practicantes);
        if (getIntent().getExtras() != null) {
            course = (Course) getIntent().getSerializableExtra("Course");

            if (course.getStudents().size() > 0) {
                studentsList = findViewById(R.id.studentsList);
                studentsListAdapter = new StudentsListAdapter(getApplicationContext(), course.getStudents());
                studentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        Intent intent = new Intent(getApplicationContext(), EstadisticasActivity.class);
                        intent.putExtra("Student", course.getStudents().get(position));
                        startActivity(intent);
                    }
                });
                studentsList.setAdapter(studentsListAdapter);
            }
        }

        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ver practicantes");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}

