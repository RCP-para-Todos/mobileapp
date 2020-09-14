package com.jk.rcp.main.data.model.user;

import androidx.annotation.NonNull;

import com.jk.rcp.main.data.model.user.LoginPost;

import okhttp3.ResponseBody;

public interface LoginRequestCallbacks {
    void onSuccess(@NonNull LoginPost value);

    void onError(@NonNull Throwable throwable);

    void onErrorBody(@NonNull ResponseBody errorBody);
}
