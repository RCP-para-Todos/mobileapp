package com.jk.rcp.main.data.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jk.rcp.main.data.model.course.Student;
import com.jk.rcp.main.data.model.instant.Instant;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Created_date")
    @Expose
    private String eventDate;
    @SerializedName("user")
    @Expose
    private Student user;
    @SerializedName("course")
    @Expose
    private String course;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("puntaje")
    @Expose
    private String puntaje;
    @SerializedName("tiempoInactividad")
    @Expose
    private String tiempoInactividad;
    @SerializedName("porcentajeSobrevida")
    @Expose
    private String porcentajeSobrevida;
    @SerializedName("porcentajeInsuflacionOk")
    @Expose
    private String porcentajeInsuflacionOk;
    @SerializedName("porcentajeCompresionOk")
    @Expose
    private String porcentajeCompresionOk;
    @SerializedName("cantidadInsuflacionesOkMalCabeza")
    @Expose
    private String cantidadInsuflacionesOkMalCabeza;
    @SerializedName("fuerzaPromedioAplicada")
    @Expose
    private String fuerzaPromedioAplicada;
    @SerializedName("calidadInsuflaciones")
    @Expose
    private String calidadInsuflaciones;
    @SerializedName("instants")
    @Expose
    private ArrayList<Instant> instants;
    @SerializedName("observations")
    @Expose
    private String observations;
    @SerializedName("brazosFlexionados")
    @Expose
    private String brazosFlexionados;
    @SerializedName("noConsultaEstadoVictima")
    @Expose
    private String noConsultaEstadoVictima;
    @SerializedName("noEstaAtentoAlEscenario")
    @Expose
    private String noEstaAtentoAlEscenario;
    @SerializedName("disponeAyudaNoSolicita")
    @Expose
    private String disponeAyudaNoSolicita;
    @SerializedName("demoraTomaDesiciones")
    @Expose
    private String demoraTomaDesiciones;

    /**
     * @param tiempoInactividad
     * @param noConsultaEstadoVictima
     * @param noEstaAtentoAlEscenario
     * @param instants
     * @param fuerzaPromedioAplicada
     * @param cantidadInsuflacionesOkMalCabeza
     * @param type
     * @param duration
     * @param puntaje
     * @param observations
     * @param porcentajeInsuflacionOk
     * @param course
     * @param brazosFlexionados
     * @param id
     * @param demoraTomaDesiciones
     * @param calidadInsuflaciones
     * @param user
     * @param porcentajeSobrevida
     * @param porcentajeCompresionOk
     * @param disponeAyudaNoSolicita
     * @param eventDate
     */
    public Event(String id, String eventDate, Student user, String course, String type, String duration, String puntaje, String tiempoInactividad, String porcentajeSobrevida, String porcentajeInsuflacionOk, String porcentajeCompresionOk, String cantidadInsuflacionesOkMalCabeza, String fuerzaPromedioAplicada, String calidadInsuflaciones, ArrayList<Instant> instants, String observations, String brazosFlexionados, String noConsultaEstadoVictima, String noEstaAtentoAlEscenario, String disponeAyudaNoSolicita, String demoraTomaDesiciones) {
        super();
        this.id = id;
        this.eventDate = eventDate;
        this.user = user;
        this.course = course;
        this.type = type;
        this.duration = duration;
        this.puntaje = puntaje;
        this.tiempoInactividad = tiempoInactividad;
        this.porcentajeSobrevida = porcentajeSobrevida;
        this.porcentajeInsuflacionOk = porcentajeInsuflacionOk;
        this.porcentajeCompresionOk = porcentajeCompresionOk;
        this.cantidadInsuflacionesOkMalCabeza = cantidadInsuflacionesOkMalCabeza;
        this.fuerzaPromedioAplicada = fuerzaPromedioAplicada;
        this.calidadInsuflaciones = calidadInsuflaciones;
        this.instants = instants;
        this.observations = observations;
        this.brazosFlexionados = brazosFlexionados;
        this.noConsultaEstadoVictima = noConsultaEstadoVictima;
        this.noEstaAtentoAlEscenario = noEstaAtentoAlEscenario;
        this.disponeAyudaNoSolicita = disponeAyudaNoSolicita;
        this.demoraTomaDesiciones = demoraTomaDesiciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Student getUser() {
        return user;
    }

    public void setUser(Student user) {
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    public String getTiempoInactividad() {
        return tiempoInactividad;
    }

    public void setTiempoInactividad(String tiempoInactividad) {
        this.tiempoInactividad = tiempoInactividad;
    }

    public String getPorcentajeSobrevida() {
        return porcentajeSobrevida;
    }

    public void setPorcentajeSobrevida(String porcentajeSobrevida) {
        this.porcentajeSobrevida = porcentajeSobrevida;
    }

    public String getPorcentajeInsuflacionOk() {
        return porcentajeInsuflacionOk;
    }

    public void setPorcentajeInsuflacionOk(String porcentajeInsuflacionOk) {
        this.porcentajeInsuflacionOk = porcentajeInsuflacionOk;
    }

    public String getPorcentajeCompresionOk() {
        return porcentajeCompresionOk;
    }

    public void setPorcentajeCompresionOk(String porcentajeCompresionOk) {
        this.porcentajeCompresionOk = porcentajeCompresionOk;
    }

    public String getCantidadInsuflacionesOkMalCabeza() {
        return cantidadInsuflacionesOkMalCabeza;
    }

    public void setCantidadInsuflacionesOkMalCabeza(String cantidadInsuflacionesOkMalCabeza) {
        this.cantidadInsuflacionesOkMalCabeza = cantidadInsuflacionesOkMalCabeza;
    }

    public String getFuerzaPromedioAplicada() {
        return fuerzaPromedioAplicada;
    }

    public void setFuerzaPromedioAplicada(String fuerzaPromedioAplicada) {
        this.fuerzaPromedioAplicada = fuerzaPromedioAplicada;
    }

    public String getCalidadInsuflaciones() {
        return calidadInsuflaciones;
    }

    public void setCalidadInsuflaciones(String calidadInsuflaciones) {
        this.calidadInsuflaciones = calidadInsuflaciones;
    }

    public ArrayList<Instant> getInstants() {
        return instants;
    }

    public void setInstants(ArrayList<Instant> instants) {
        this.instants = instants;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getBrazosFlexionados() {
        return brazosFlexionados;
    }

    public void setBrazosFlexionados(String brazosFlexionados) {
        this.brazosFlexionados = brazosFlexionados;
    }

    public String getNoConsultaEstadoVictima() {
        return noConsultaEstadoVictima;
    }

    public void setNoConsultaEstadoVictima(String noConsultaEstadoVictima) {
        this.noConsultaEstadoVictima = noConsultaEstadoVictima;
    }

    public String getNoEstaAtentoAlEscenario() {
        return noEstaAtentoAlEscenario;
    }

    public void setNoEstaAtentoAlEscenario(String noEstaAtentoAlEscenario) {
        this.noEstaAtentoAlEscenario = noEstaAtentoAlEscenario;
    }

    public String getDisponeAyudaNoSolicita() {
        return disponeAyudaNoSolicita;
    }

    public void setDisponeAyudaNoSolicita(String disponeAyudaNoSolicita) {
        this.disponeAyudaNoSolicita = disponeAyudaNoSolicita;
    }

    public String getDemoraTomaDesiciones() {
        return demoraTomaDesiciones;
    }

    public void setDemoraTomaDesiciones(String demoraTomaDesiciones) {
        this.demoraTomaDesiciones = demoraTomaDesiciones;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", user=" + user +
                ", course='" + course + '\'' +
                ", type='" + type + '\'' +
                ", duration='" + duration + '\'' +
                ", puntaje='" + puntaje + '\'' +
                ", tiempoInactividad='" + tiempoInactividad + '\'' +
                ", porcentajeSobrevida='" + porcentajeSobrevida + '\'' +
                ", porcentajeInsuflacionOk='" + porcentajeInsuflacionOk + '\'' +
                ", porcentajeCompresionOk='" + porcentajeCompresionOk + '\'' +
                ", cantidadInsuflacionesOkMalCabeza='" + cantidadInsuflacionesOkMalCabeza + '\'' +
                ", fuerzaPromedioAplicada='" + fuerzaPromedioAplicada + '\'' +
                ", calidadInsuflaciones='" + calidadInsuflaciones + '\'' +
                ", instants=" + instants +
                ", observations='" + observations + '\'' +
                ", brazosFlexionados='" + brazosFlexionados + '\'' +
                ", noConsultaEstadoVictima='" + noConsultaEstadoVictima + '\'' +
                ", noEstaAtentoAlEscenario='" + noEstaAtentoAlEscenario + '\'' +
                ", disponeAyudaNoSolicita='" + disponeAyudaNoSolicita + '\'' +
                ", demoraTomaDesiciones='" + demoraTomaDesiciones + '\'' +
                '}';
    }
}