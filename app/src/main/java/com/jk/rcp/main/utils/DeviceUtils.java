package com.jk.rcp.main.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DeviceUtils {
    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        EventManager.registerEvent(Constants.DETECT_DEVICE_ONLINE);
        return (netInfo != null && netInfo.isConnected());
    }
}
