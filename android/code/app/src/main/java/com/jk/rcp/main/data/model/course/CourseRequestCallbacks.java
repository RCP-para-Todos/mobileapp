package com.jk.rcp.main.data.model.course;

import androidx.annotation.NonNull;

import java.util.List;

import okhttp3.ResponseBody;

public interface CourseRequestCallbacks {
    void onSuccess(@NonNull Course value);

    void onError(@NonNull Throwable throwable);

    void onErrorBody(@NonNull ResponseBody errorBody);
}
