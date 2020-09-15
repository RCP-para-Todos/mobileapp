package com.jk.rcp.main.data.model.instant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Instant implements Serializable {

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

    // Funciones de calculo
    public static int tiempoTotalManiobra(List<Instant> instantes) {
        int tiempoTotal = 0;
        if (instantes.size() > 0) {
            tiempoTotal = instantes.size();
        }

        return tiempoTotal / 2;
    }

    /* El tiempo de inactividad es solo contar las nulas sumando al tiempo de inactividad.
     O solo seria tiempo de actividad (lo contrario) las correctas ?
     */
    public static Double tiempoTotalInactividad(List<Instant> instantes) {
        Double tiempoTotalInactividad = 0.0;

        for (Instant instante : instantes
        ) {
            if (instante.getCompresion() != null && instante.getCompresion().equals("Nula") && instante.getInsuflacion().equals("Nula")) {
                tiempoTotalInactividad = tiempoTotalInactividad + 0.5;
            }
        }

        return tiempoTotalInactividad;
    }

    /* Empiezo con 100% de porcentaje de sobrevida.
     Cada segundo me resta un 10%.
     */
    public static Double porcentajeTotalSobreVida(List<Instant> instantes) {
        Double porcentajeTotalSobreVida = 100.0;
        Double tiempoTotalInactividad = Instant.tiempoTotalInactividad(instantes);
        return porcentajeTotalSobreVida - tiempoTotalInactividad * 10;
    }

    public static Double porcentajeInsuflacionesCorrectas(List<Instant> instantes) {
        Double insuflacionesTotalesSinNulas = 0.0;
        Double insuflacionesCorrectas = 0.0;

        for (Instant instante : instantes) {
            if (instante.getInsuflacion() != null && (instante.getInsuflacion().equals("Insuficiente") || instante.getInsuflacion().equals("Excesiva"))) {
                insuflacionesTotalesSinNulas = insuflacionesTotalesSinNulas + 1;
            }
            if (instante.getInsuflacion() != null && instante.getInsuflacion().equals("Correcta")) {
                insuflacionesTotalesSinNulas = insuflacionesTotalesSinNulas + 1;
                insuflacionesCorrectas = insuflacionesCorrectas + 1;
            }
        }
        if (insuflacionesCorrectas == 0.0 && insuflacionesTotalesSinNulas == 0.0) {
            return 0.0;
        }
        return (insuflacionesCorrectas / insuflacionesTotalesSinNulas) * 100;
    }

    public static Double porcentajeCompresionesCorrectas(List<Instant> instantes) {
        Double compresionesCorrectas = 0.0;

        for (Instant instante : instantes) {
            if (instante.getCompresion() != null && instante.getCompresion().equals("Correcta")) {
                compresionesCorrectas = compresionesCorrectas + 1;
            }
        }

        return (compresionesCorrectas / Double.valueOf(instantes.size())) * 100;
    }

    // TODO
    public static Double cantidadInsuflacionesCorrectasPosicionCabeza(List<Instant> instantes) {
        return 1.0;
    }

    // TODO
    public static Double fuerzaPromedioAplicada(List<Instant> instantes) {
        return 10.0;
    }

    public static String calidadInsuflaciones(List<Instant> instantes) {
        Double porcentajeInsuflacionesCorrectas = Instant.porcentajeInsuflacionesCorrectas(instantes);
        if (porcentajeInsuflacionesCorrectas > 0.5) {
            return "Optima";
        } else {
            return "Mala";
        }
    }

    @Override
    public String toString() {
        return "Instant{" +
                "nro='" + nro + '\'' +
                ", insuflacion='" + insuflacion + '\'' +
                ", compresion='" + compresion + '\'' +
                ", posicion='" + posicion + '\'' +
                '}';
    }
}