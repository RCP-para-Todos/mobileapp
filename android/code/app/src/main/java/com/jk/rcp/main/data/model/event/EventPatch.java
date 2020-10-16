package com.jk.rcp.main.data.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jk.rcp.main.data.model.instant.Instant;

import java.io.Serializable;
import java.util.List;

public class EventPatch implements Serializable {

    @SerializedName("keywords")
    @Expose
    private List<Object> keywords = null;
    @SerializedName("_id")
    @Expose
    private String id;
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
    @SerializedName("instants")
    @Expose
    private List<Instant> instants = null;
    @SerializedName("Created_date")
    @Expose
    private String createdDate;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("observations")
    @Expose
    private String observations;
    @SerializedName("brazosFlexionados")
    @Expose
    private Boolean brazosFlexionados;
    @SerializedName("noConsultaEstadoVictima")
    @Expose
    private Boolean noConsultaEstadoVictima;
    @SerializedName("noEstaAtentoAlEscenario")
    @Expose
    private Boolean noEstaAtentoAlEscenario;
    @SerializedName("disponeAyudaNoSolicita")
    @Expose
    private Boolean disponeAyudaNoSolicita;
    @SerializedName("demoraTomaDesiciones")
    @Expose
    private Boolean demoraTomaDesiciones;

    /**
     * No args constructor for use in serialization
     */
    public EventPatch() {
    }

    /**
     * @param noConsultaEstadoVictima
     * @param noEstaAtentoAlEscenario
     * @param keywords
     * @param instants
     * @param type
     * @param duration
     * @param createdDate
     * @param v
     * @param observations
     * @param course
     * @param brazosFlexionados
     * @param id
     * @param demoraTomaDesiciones
     * @param user
     * @param disponeAyudaNoSolicita
     */
    public EventPatch(List<Object> keywords, String id, String user, String course, String type, Integer duration, List<Instant> instants, String createdDate, Integer v, String observations, Boolean brazosFlexionados, Boolean noConsultaEstadoVictima, Boolean noEstaAtentoAlEscenario, Boolean disponeAyudaNoSolicita, Boolean demoraTomaDesiciones) {
        super();
        this.keywords = keywords;
        this.id = id;
        this.user = user;
        this.course = course;
        this.type = type;
        this.duration = duration;
        this.instants = instants;
        this.createdDate = createdDate;
        this.v = v;
        this.observations = observations;
        this.brazosFlexionados = brazosFlexionados;
        this.noConsultaEstadoVictima = noConsultaEstadoVictima;
        this.noEstaAtentoAlEscenario = noEstaAtentoAlEscenario;
        this.disponeAyudaNoSolicita = disponeAyudaNoSolicita;
        this.demoraTomaDesiciones = demoraTomaDesiciones;
    }

    public List<Object> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Object> keywords) {
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Instant> getInstants() {
        return instants;
    }

    public void setInstants(List<Instant> instants) {
        this.instants = instants;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean getBrazosFlexionados() {
        return brazosFlexionados;
    }

    public void setBrazosFlexionados(Boolean brazosFlexionados) {
        this.brazosFlexionados = brazosFlexionados;
    }

    public Boolean getNoConsultaEstadoVictima() {
        return noConsultaEstadoVictima;
    }

    public void setNoConsultaEstadoVictima(Boolean noConsultaEstadoVictima) {
        this.noConsultaEstadoVictima = noConsultaEstadoVictima;
    }

    public Boolean getNoEstaAtentoAlEscenario() {
        return noEstaAtentoAlEscenario;
    }

    public void setNoEstaAtentoAlEscenario(Boolean noEstaAtentoAlEscenario) {
        this.noEstaAtentoAlEscenario = noEstaAtentoAlEscenario;
    }

    public Boolean getDisponeAyudaNoSolicita() {
        return disponeAyudaNoSolicita;
    }

    public void setDisponeAyudaNoSolicita(Boolean disponeAyudaNoSolicita) {
        this.disponeAyudaNoSolicita = disponeAyudaNoSolicita;
    }

    public Boolean getDemoraTomaDesiciones() {
        return demoraTomaDesiciones;
    }

    public void setDemoraTomaDesiciones(Boolean demoraTomaDesiciones) {
        this.demoraTomaDesiciones = demoraTomaDesiciones;
    }

}