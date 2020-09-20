package com.jk.rcp.main.data.model.user;

import androidx.annotation.NonNull;

import java.util.List;

import okhttp3.ResponseBody;

public interface UsersRequestCallbacks {
    void onSuccess(@NonNull List<Users> value);

    void onError(@NonNull Throwable throwable);

    void onErrorBody(@NonNull ResponseBody errorBody);
}
