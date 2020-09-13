package com.jk.rcp.main.activities.instructor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

public class HomeActivityInstructor extends AppCompatActivity {
    private static final String TAG = "HomeActivityInstructor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content_instructor);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
