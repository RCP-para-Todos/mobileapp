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
}
