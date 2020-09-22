package com.jk.rcp.main.data.remote;

import com.jk.rcp.main.data.model.course.Course;
import com.jk.rcp.main.data.model.course.NewCourse;
import com.jk.rcp.main.data.model.event.Event;
import com.jk.rcp.main.data.model.event.EventPatch;
import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.LoginPost;
import com.jk.rcp.main.data.model.user.User;
import com.jk.rcp.main.data.model.user.UserPost;
import com.jk.rcp.main.data.model.user.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    // Inicio metodos de Auth
    @POST("auth/login")
    @FormUrlEncoded
    Call<LoginPost> login(@Field("name") String username,
                          @Field("password") String password
    );

    @POST("auth/register")
    @FormUrlEncoded
    Call<LoginPost> register(@Field("name") String username,
                             @Field("password") String password,
                             @Field("rol") String rol);

    @POST("auth/token")
    @FormUrlEncoded
    Call<UserPost> token(@Field("refreshToken") String refreshToken,
                         @Header("Authorization") String auth);

    @POST("auth/me")
    @FormUrlEncoded
    Call<UserPost> me(@Header("Authorization") String auth);

    @POST("auth/logout")
    @FormUrlEncoded
    Call<UserPost> logout(@Header("Authorization") String auth);

    // Fin metodos de Auth

    // Inicio metodos de Courses
    @POST("courses")
    Call<Course> addCourse(@Header("Authorization") String auth,
                           @Body NewCourse newCourse
    );

    @GET("courses")
    Call<List<Course>> getCourses(@Header("Authorization") String auth);

    @GET("courses/{courseId}")
    Call<UserPost> getCourse(@Path("courseId") String id,
                             @Header("Authorization") String auth);

    @PUT("courses/{courseId}")
    @FormUrlEncoded
    Call<UserPost> updateCourse(@Path("courseId") String id,
                                @Header("Authorization") String auth,
                                @Field("name") String name,
                                @Field("event_date") String eventDate,
                                @Field("instructor") String instructor,
                                @Field("student") ArrayList<String> students);

    @DELETE("courses/{courseId}")
    @FormUrlEncoded
    Call<UserPost> deleteCourse(@Path("courseId") String id,
                                @Header("Authorization") String auth);
    // Fin metodos de Courses

    // Inicio metodos de Events
    @GET("events")
    Call<List<Event>> getEvents(@Header("Authorization") String auth);

    @GET("events")
    Call<List<Event>> getEventsByPracticant(@Query("practicantName") String practicant, @Header("Authorization") String auth);

    @POST("events")
    @FormUrlEncoded
    Call<UserPost> addEvent(@Header("Authorization") String auth,
                            @Field("_id") Integer id,
                            @Field("event_date") String eventDate,
                            @Field("user") String user,
                            @Field("course") String course,
                            @Field("type") String type,
                            @Field("duration") String duration,
                            @Field("puntaje") Double puntaje,
                            @Field("tiempoInactividad") Double tiempoInactividad,
                            @Field("porcentajeSobrevida") Double porcentajeSobrevida,
                            @Field("porcentajeInsuflacionOk") Double porcentajeInsuflacionOk,
                            @Field("porcentajeCompresionOk") Double porcentajeCompresionOk,
                            @Field("cantidadInsuflacionesOkMalCabeza") Double cantidadInsuflacionesOkMalCabeza,
                            @Field("fuerzaPromedioAplicada") Double fuerzaPromedioAplicada,
                            @Field("instants") ArrayList<Instant> instants,
                            @Field("observations") String observations,
                            @Field("brazosFlexionados") Boolean brazosFlexionados,
                            @Field("noConsultaEstadoVictima") Boolean noConsultaEstadoVictima,
                            @Field("noEstaAtentoAlEscenario") Boolean noEstaAtentoAlEscenario,
                            @Field("disponeAyudaNoSolicita") Boolean disponeAyudaNoSolicita,
                            @Field("demoraTomaDesiciones") Boolean demoraTomaDesiciones
    );

    @GET("events/{eventId}")
    Call<EventPatch> getEvent(@Path("eventId") String id,
                              @Header("Authorization") String auth);

    @PATCH("events/{eventId}")
    @FormUrlEncoded
    Call<EventPatch> patchEvent(@Path("eventId") String id,
                                @Header("Authorization") String auth,
                                @Field("observations") String observations,
                                @Field("brazosFlexionados") Boolean brazosFlexionados,
                                @Field("noConsultaEstadoVictima") Boolean noConsultaEstadoVictima,
                                @Field("noEstaAtentoAlEscenario") Boolean noEstaAtentoAlEscenario,
                                @Field("disponeAyudaNoSolicita") Boolean disponeAyudaNoSolicita,
                                @Field("demoraTomaDesiciones") Boolean demoraTomaDesiciones);
    // Fin metodos de Courses

    // Inicio metodos de Roles
    @GET("roles")
    Call<UserPost> getRoles(@Header("Authorization") String auth);
    // Fin metodos de Roles

    // Inicio metodos de Users
    @GET("users")
    Call<List<User>> getAllUsers(@Header("Authorization") String auth);

    @GET("users")
    Call<List<Users>> getUsers(@Query("q") String noCourse, @Header("Authorization") String auth);

    @GET("users/{userId}")
    Call<UserPost> getUser(@Path("userId") String id,
                           @Header("Authorization") String auth);
    // Fin metodos de Users
}