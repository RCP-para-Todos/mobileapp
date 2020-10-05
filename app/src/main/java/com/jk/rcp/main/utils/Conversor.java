package com.jk.rcp.main.utils;

public class Conversor {
    public static String insuflacionToString(Integer n) {
        switch (n) {
            case 0:
                return "Nula";
            case 1:
                return "Insuficiente";
            case 2:
                return "Correcta";
            case 3:
                return "Excesiva";
            default:
                return "";
        }
    }

    public static String compresionToString(Integer n) {
        switch (n) {
            case 0:
                return "Nula";
            case 1:
                return "Insuficiente";
            case 2:
                return "Correcta";
            case 3:
                return "Excesiva";
            default:
                return "";
        }
    }

    public static String posicionToString(Integer n) {
        switch (n) {
            case 1:
                return "Acostado";
            case 2:
                return "Reclinado";
            default:
                return "";
        }
    }
}
