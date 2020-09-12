package com.jk.rcp.main.activities;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jk.rcp.R;


public class HomeContentFragment extends Fragment {
    private static final String TAG = "HomeContent";
    private TextView helpText;

    public static HomeContentFragment newInstance() {
        HomeContentFragment frag = new HomeContentFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        // Pongo por default ese contenido
        View layout = inflater.inflate(R.layout.activity_home_content, container, false);


        return layout;
    }
}

