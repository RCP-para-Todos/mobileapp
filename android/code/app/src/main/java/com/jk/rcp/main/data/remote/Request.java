package com.jk.rcp.main.data.remote;

import android.util.Log;

import com.jk.rcp.main.data.model.course.Course;
import com.jk.rcp.main.data.model.course.CourseRequestCallbacks;
import com.jk.rcp.main.data.model.course.CoursesRequestCallbacks;
import com.jk.rcp.main.data.model.course.NewCourse;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventListRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventPatch;
import com.jk.rcp.main.data.model.event.EventPatchRequestCallbacks;
import com.jk.rcp.main.data.model.event.EventRequestCallbacks;
import com.jk.rcp.main.data.model.event.NewEvent;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.LoginPost;
import com.jk.rcp.main.data.model.user.LoginRequestCallbacks;
import com.jk.rcp.main.data.model.user.Users;
import com.jk.rcp.main.data.model.user.UsersRequestCallbacks;

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

    public void updateObservations(EventPatch event, String token, final EventPatchRequestCallbacks requestCallbacks) {
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

    public void getEvent(String eventId, String token, final EventPatchRequestCallbacks requestCallbacks) {
        mAPIService.getEvent(eventId, token).enqueue(new Callback<EventPatch>() {
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

    public void getUnasignedPeople(String token, final UsersRequestCallbacks requestCallbacks) {
        mAPIService.getUsers("nocourse", token).enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
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
            public void onFailure(Call<List<Users>> call, Throwable t) {
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

    public void crearCurso(String nombre, String fechaActual, String nombreInstructor, List<String> userIds, String token, final CourseRequestCallbacks requestCallbacks) {
        NewCourse newCourse = new NewCourse();
        newCourse.setEventDate(fechaActual);
        newCourse.setStudent(userIds);
        newCourse.setInstructor(nombreInstructor);
        newCourse.setName(nombre);
        mAPIService.addCourse(token, newCourse).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void crearEvento(String usuarioActivo,
                            String curso,
                            Integer duration,
                            String tipo,
                            String event_date,
                            List<Instant> instantes,
                            Double tiempoInactividad,
                            Double porcentajeSobrevida,
                            String calidadInsuflaciones,
                            Double porcentajeInsuflacionOk,
                            Double porcentajeCompresionOk,
                            Double cantidadInsuflacionesOkMalCabeza,
                            Double fuerzaPromedioAplicada,
                            String bearerToken,
                            final EventRequestCallbacks requestCallbacks) {

        NewEvent newEvent = new NewEvent();

        newEvent.setUser(usuarioActivo);
        newEvent.setCourse(curso);
        newEvent.setDuration(duration);
        newEvent.setType(tipo);
        newEvent.setEventDate(event_date);
        newEvent.setInstants(instantes);
        newEvent.setCalidadInsuflaciones(calidadInsuflaciones);
        newEvent.setTiempoInactividad(tiempoInactividad);
        newEvent.setPorcentajeSobrevida(porcentajeSobrevida);
        newEvent.setPorcentajeInsuflacionOk(porcentajeInsuflacionOk);
        newEvent.setPorcentajeSobrevida(porcentajeCompresionOk);
        newEvent.setCantidadInsuflacionesOkMalCabeza(cantidadInsuflacionesOkMalCabeza);
        newEvent.setFuerzaPromedioAplicada(fuerzaPromedioAplicada);

        mAPIService.addEvent(
                bearerToken, newEvent

        ).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                t.printStackTrace();
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }
}
