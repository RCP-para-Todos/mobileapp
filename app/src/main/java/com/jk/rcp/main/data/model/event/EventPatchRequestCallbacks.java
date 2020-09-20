package com.jk.rcp.main.data.model.event;

import androidx.annotation.NonNull;

import okhttp3.ResponseBody;

public interface EventPatchRequestCallbacks {
    void onSuccess(@NonNull EventPatch value);

    void onError(@NonNull Throwable throwable);

    void onErrorBody(@NonNull ResponseBody errorBody);
}
