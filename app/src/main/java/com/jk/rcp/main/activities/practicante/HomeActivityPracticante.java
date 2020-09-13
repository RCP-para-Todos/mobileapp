package com.jk.rcp.main.activities.practicante;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

public class HomeActivityPracticante extends AppCompatActivity {
    private static final String TAG = "HomeActivityPracticante";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
