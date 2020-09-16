package com.jk.rcp.main.data.remote;

import android.util.Log;

import com.jk.rcp.main.data.model.course.Course;
import com.jk.rcp.main.data.model.course.CoursesRequestCallbacks;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventRequestCallbacks;
import com.jk.rcp.main.data.model.user.LoginPost;
import com.jk.rcp.main.data.model.user.LoginRequestCallbacks;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Request {
    private final static String TAG = "REQUEST";
    private APIService mAPIService;

    public Request() {
        mAPIService = ApiUtils.getAPIService();
    }

    public void sendLogin(String username, String password, final LoginRequestCallbacks requestCallbacks) {
        mAPIService.login(username, password).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginPost> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void getEvents(String token, final EventRequestCallbacks requestCallbacks) {
        mAPIService.getEvents(token).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void getEventsByPracticant(String student, String token, final EventRequestCallbacks requestCallbacks) {
        mAPIService.getEventsByPracticant(student, token).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void getCourses(String token, final CoursesRequestCallbacks requestCallbacks) {
        mAPIService.getCourses(token).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
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
