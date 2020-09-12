package com.jk.rcp.main.data.model.instant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instant {

    @SerializedName("nro")
    @Expose
    private String nro;
    @SerializedName("Insuflacion")
    @Expose
    private String insuflacion;
    @SerializedName("Compresion")
    @Expose
    private String compresion;
    @SerializedName("Posicion")
    @Expose
    private String posicion;

    /**
     *
     * @param posicion
     * @param insuflacion
     * @param nro
     * @param compresion
     */
    public Instant(String nro, String insuflacion, String compresion, String posicion) {
        super();
        this.nro = nro;
        this.insuflacion = insuflacion;
        this.compresion = compresion;
        this.posicion = posicion;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public String getInsuflacion() {
        return insuflacion;
    }

    public void setInsuflacion(String insuflacion) {
        this.insuflacion = insuflacion;
    }

    public String getCompresion() {
        return compresion;
    }

    public void setCompresion(String compresion) {
        this.compresion = compresion;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

}