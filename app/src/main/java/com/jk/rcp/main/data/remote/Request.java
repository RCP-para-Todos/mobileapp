package com.jk.rcp.main.data.remote;

import android.util.Log;

import com.jk.rcp.main.data.model.user.UserPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Request {
    private final static Integer GROUP = 614;
    private final static Integer COMMISION = 2900;
    private final static String ENV = "DEV";
    private final static String TAG = "REQUEST";
    private APIService mAPIService;

    public Request() {
        mAPIService = ApiUtils.getAPIService();
    }

    public void sendLogin(String username, String password, String rol, final RequestCallbacks requestCallbacks) {
        mAPIService.login(username, password).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void sendRegister(String name, String surname, String rol) {
//        mAPIService.register(ENV, name, surname, Integer.valueOf(dni), email, password, COMMISION, GROUP).enqueue(new Callback<UserPost>() {
//            @Override
//            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
//                if (response.isSuccessful()) {
//                    EventManager.registerEvent(Constants.USER_REGISTERED);
//                    showResponse(response.body().toString());
//                } else {
//                    EventManager.registerEvent(Constants.USER_COULDNT_REGISTER);
//                    Log.i(TAG, "Ocurrió un error.");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserPost> call, Throwable t) {
//                Log.e(TAG, "Error al enviar el request.");
//                EventManager.registerEvent(Constants.USER_COULDNT_REGISTER);
//            }
//        });
    }

    public void registerEvent(String token, String typeEvents, String state, String description) {
//        mAPIService.registerEvent(token, ENV, typeEvents, state, description).enqueue(new Callback<EventPost>() {
//            @Override
//            public void onResponse(Call<EventPost> call, Response<EventPost> response) {
//                if (response.isSuccessful()) {
//                    showResponse(response.body().toString());
//                } else {
//
//                    Log.i(TAG, "Ocurrió un error.");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EventPost> call, Throwable t) {
//                Log.e(TAG, "Error al enviar el request.");
//            }
//        });
    }

    public void showResponse(String response) {
        Log.d(TAG, response);
    }

}
