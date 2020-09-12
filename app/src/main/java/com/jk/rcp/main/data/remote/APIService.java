package com.jk.rcp.main.data.remote;

import com.jk.rcp.main.data.model.instant.Instant;
import com.jk.rcp.main.data.model.user.UserPost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface APIService {
    // Inicio metodos de Auth
    @POST("auth/login")
    @FormUrlEncoded
    Call<UserPost> login(@Field("name") String username,
                         @Field("password") String password
    );

    @POST("auth/register")
    @FormUrlEncoded
    Call<UserPost> register(@Field("username") String username,
                            @Field("password") String password,
                            @Field("rol") String rol);

    @POST("auth/token")
    @FormUrlEncoded
    Call<UserPost> token(@Field("refreshToken") String refreshToken);

    @POST("auth/me")
    @FormUrlEncoded
    Call<UserPost> me();

    @POST("auth/logout")
    @FormUrlEncoded
    Call<UserPost> logout();

    // Fin metodos de Auth

    // Inicio metodos de Courses
    @POST("courses")
    @FormUrlEncoded
    Call<UserPost> addCourse(@Field("name") String name,
                             @Field("event_date") String eventDate,
                             @Field("instructor") String instructor,
                             @Field("student") ArrayList<String> students
    );

    @GET("courses")
    @FormUrlEncoded
    Call<UserPost> getCourses();

    @GET
    @FormUrlEncoded
    Call<UserPost> getCourse(@Url String url);

    @PUT
    @FormUrlEncoded
    Call<UserPost> updateCourse(@Url String url,
                                @Field("name") String name,
                                @Field("event_date") String eventDate,
                                @Field("instructor") String instructor,
                                @Field("student") ArrayList<String> students);

    @DELETE
    @FormUrlEncoded
    Call<UserPost> deleteCourse(@Url String url);
    // Fin metodos de Courses

    // Inicio metodos de Events
    @GET("events")
    @FormUrlEncoded
    Call<UserPost> getEvents();

    @POST("events")
    @FormUrlEncoded
    Call<UserPost> addEvent(@Field("_id") Integer id,
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

    @GET
    @FormUrlEncoded
    Call<UserPost> getEvent(@Url String url);

    @PATCH
    @FormUrlEncoded
    Call<UserPost> patchEvent(@Url String url);
    // Fin metodos de Courses

    // Inicio metodos de Roles
    @GET("roles")
    @FormUrlEncoded
    Call<UserPost> getRoles();
    // Fin metodos de Roles

    // Inicio metodos de Users
    @GET("users")
    @FormUrlEncoded
    Call<UserPost> getUsers();

    @GET
    @FormUrlEncoded
    Call<UserPost> getUser(@Url String url);
    // Fin metodos de Users
}