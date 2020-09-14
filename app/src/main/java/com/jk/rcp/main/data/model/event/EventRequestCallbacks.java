package com.jk.rcp.main.data.model.event;

import androidx.annotation.NonNull;

import java.util.List;

import okhttp3.ResponseBody;

public interface EventRequestCallbacks {
    void onSuccess(@NonNull List<Event> value);

    void onError(@NonNull Throwable throwable);

    void onErrorBody(@NonNull ResponseBody errorBody);
}
