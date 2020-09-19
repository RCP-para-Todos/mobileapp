package com.jk.rcp.main.data.remote;

import android.util.Log;

import com.jk.rcp.main.data.model.course.Course;
import com.jk.rcp.main.data.model.course.CoursesRequestCallbacks;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventListRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventPatch;
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

    public void updateObservations(Event event, String token, final EventRequestCallbacks requestCallbacks) {
        mAPIService.patchEvent(event.getId(), token, event.getObservations(), event.getBrazosFlexionados(),
                event.getNoConsultaEstadoVictima(), event.getNoEstaAtentoAlEscenario(),
                event.getDisponeAyudaNoSolicita(), event.getDemoraTomaDesiciones()
        ).enqueue(new Callback<EventPatch>() {
            @Override
            public void onResponse(Call<EventPatch> call, Response<EventPatch> response) {
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
            public void onFailure(Call<EventPatch> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void getEvents(String token, final EventListRequestCallbacks requestCallbacks) {
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

    public void getEventsByPracticant(String student, String token, final EventListRequestCallbacks requestCallbacks) {
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

    public void sendRegister(String name, String password, String rol, final LoginRequestCallbacks requestCallbacks) {
        mAPIService.register(name, password, rol).enqueue(new Callback<LoginPost>() {
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

    public void showResponse(String response) {
        Log.d(TAG, response);
    }

}
