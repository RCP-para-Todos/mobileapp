package com.jk.rcp.main.data.model.event;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jk.rcp.main.data.model.instant.Instant;

import java.util.List;

public class NewEvent {

    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("course")
    @Expose
    private String course;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("tiempoInactividad")
    @Expose
    private Double tiempoInactividad;
    @SerializedName("porcentajeSobrevida")
    @Expose
    private Double porcentajeSobrevida;
    @SerializedName("porcentajeInsuflacionOk")
    @Expose
    private Double porcentajeInsuflacionOk;
    @SerializedName("porcentajeCompresionOk")
    @Expose
    private Double porcentajeCompresionOk;
    @SerializedName("cantidadInsuflacionesOkMalCabeza")
    @Expose
    private Double cantidadInsuflacionesOkMalCabeza;
    @SerializedName("fuerzaPromedioAplicada")
    @Expose
    private Double fuerzaPromedioAplicada;
    @SerializedName("calidadInsuflaciones")
    @Expose
    private String calidadInsuflaciones;
    @SerializedName("instants")
    @Expose
    private List<Instant> instants = null;

    /**
     * No args constructor for use in serialization
     */
    public NewEvent() {
    }

    /**
     * @param eventDate
     * @param user
     * @param course
     * @param type
     * @param duration
     * @param tiempoInactividad
     * @param porcentajeSobrevida
     * @param porcentajeInsuflacionOk
     * @param porcentajeCompresionOk
     * @param cantidadInsuflacionesOkMalCabeza
     * @param fuerzaPromedioAplicada
     * @param calidadInsuflaciones
     * @param instants
     */
    public NewEvent(String eventDate, String user, String course, String type, Integer duration, Double tiempoInactividad, Double porcentajeSobrevida, Double porcentajeInsuflacionOk, Double porcentajeCompresionOk, Double cantidadInsuflacionesOkMalCabeza, Double fuerzaPromedioAplicada, String calidadInsuflaciones, List<Instant> instants) {
        super();
        this.eventDate = eventDate;
        this.user = user;
        this.course = course;
        this.type = type;
        this.duration = duration;
        this.tiempoInactividad = tiempoInactividad;
        this.porcentajeSobrevida = porcentajeSobrevida;
        this.porcentajeInsuflacionOk = porcentajeInsuflacionOk;
        this.porcentajeCompresionOk = porcentajeCompresionOk;
        this.cantidadInsuflacionesOkMalCabeza = cantidadInsuflacionesOkMalCabeza;
        this.fuerzaPromedioAplicada = fuerzaPromedioAplicada;
        this.calidadInsuflaciones = calidadInsuflaciones;
        this.instants = instants;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getTiempoInactividad() {
        return tiempoInactividad;
    }

    public void setTiempoInactividad(Double tiempoInactividad) {
        this.tiempoInactividad = tiempoInactividad;
    }

    public String getCalidadInsuflaciones() {
        return calidadInsuflaciones;
    }

    public void setCalidadInsuflaciones(String calidadInsuflaciones) {
        this.calidadInsuflaciones = calidadInsuflaciones;
    }

    public List<Instant> getInstants() {
        return instants;
    }

    public void setInstants(List<Instant> instants) {
        this.instants = instants;
    }

    public void setPorcentajeSobrevida(Double porcentajeSobrevida) {
        this.porcentajeSobrevida = porcentajeSobrevida;
    }

    public void setPorcentajeInsuflacionOk(Double porcentajeInsuflacionOk) {
        this.porcentajeInsuflacionOk = porcentajeInsuflacionOk;
    }

    public void setPorcentajeCompresionOk(Double porcentajeCompresionOk) {
        this.porcentajeCompresionOk = porcentajeCompresionOk;
    }

    public void setCantidadInsuflacionesOkMalCabeza(Double cantidadInsuflacionesOkMalCabeza) {
        this.cantidadInsuflacionesOkMalCabeza = cantidadInsuflacionesOkMalCabeza;
    }

    public void setFuerzaPromedioAplicada(Double fuerzaPromedioAplicada) {
        this.fuerzaPromedioAplicada = fuerzaPromedioAplicada;
    }

    public Double getPorcentajeSobrevida() {
        return porcentajeSobrevida;
    }

    public Double getPorcentajeInsuflacionOk() {
        return porcentajeInsuflacionOk;
    }

    public Double getPorcentajeCompresionOk() {
        return porcentajeCompresionOk;
    }

    public Double getCantidadInsuflacionesOkMalCabeza() {
        return cantidadInsuflacionesOkMalCabeza;
    }

    public Double getFuerzaPromedioAplicada() {
        return fuerzaPromedioAplicada;
    }
}