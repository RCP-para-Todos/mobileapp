//
//  Instante.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 07/06/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation

class Instante {
    var Insuflacion: String
    var Compresion: String
    var Posicion: String
    var PosicionCabeza: String

    init(insuflacion: String, compresion: String, posicion: String, posicionCabeza: String) {
        self.Insuflacion = insuflacion
        self.Compresion = compresion
        self.Posicion = posicion
        self.PosicionCabeza = posicionCabeza
    }
    
    public static func toJson(instantes: [Instante]) -> [[String : Any]]{
        var retorno : [[String : Any]] = []
        var contador : Int = 0
        for i in instantes{
            let temp = ["nro": contador, "insuflacion": i.Insuflacion, "compresion": i.Compresion, "posicion":i.Posicion] as [String : Any]
            retorno.append(temp)
            contador = contador + 1
        }
        return retorno
    }
    
    public static func tiempoTotalManiobra(instantes: [Instante]) -> Int{
        return instantes.count / 2
    }
    
    /* El tiempo de inactividad es solo contar las nulas sumando al tiempo de inactividad.
     O solo seria tiempo de actividad (lo contrario) las correctas ?
     */
    public static func tiempoTotalInactividad(instantes: [Instante]) -> Double{
        var tiempoTotalInactividad : Double = 0
        for i in instantes{
            if(i.Compresion == "Nula" && i.Insuflacion == "Nula"){
                tiempoTotalInactividad = tiempoTotalInactividad + 0.5
            }
        }
        return tiempoTotalInactividad
    }
    /* Empiezo con 100% de porcentaje de sobrevida.
     Cada segundo me resta un 10%.
     */
    public static func porcentajeTotalSobreVida(instantes: [Instante]) -> Double{
        let porcentajeTotalSobreVida : Double = 100
        let tiempoTotalInactividad = Instante.tiempoTotalInactividad(instantes: instantes)
        return porcentajeTotalSobreVida - (tiempoTotalInactividad / 60) * 10
    }
    
    public static func porcentajeInsuflacionesCorrectas(instantes: [Instante]) -> Double{
        var insuflacionesTotalesSinNulas : Double = 0
        var insuflacionesCorrectas : Double = 0
        for i in instantes{
            if(i.Insuflacion == "Insuficiente" || i.Insuflacion == "Excesiva"){
                insuflacionesTotalesSinNulas = insuflacionesTotalesSinNulas + 1
            }
            if(i.Insuflacion == "Correcta"){
                insuflacionesTotalesSinNulas = insuflacionesTotalesSinNulas + 1
                insuflacionesCorrectas = insuflacionesCorrectas + 1
            }
        }
        if(insuflacionesTotalesSinNulas == 0 || insuflacionesCorrectas == 0){
            return 0
        }
        return (insuflacionesCorrectas / insuflacionesTotalesSinNulas) * 100
    }
    
    public static func porcentajeCompresionesCorrectas(instantes: [Instante]) -> Double{
        var compresionesCorrectas : Double = 0
        for i in instantes{
            if(i.Compresion == "Correcta"){
                compresionesCorrectas = compresionesCorrectas + 1
            }
        }
        return (compresionesCorrectas / Double(instantes.count)) * 100
    }
    
    public static func cantidadInsuflacionesIncorrectasPosicionCabeza(instantes: [Instante]) -> Double{
        var cantidadInsuflacionesTotales : Double = 0
        var cantidadInsuflacionesIncorrectas : Double = 0
        for i in instantes{
            if(i.Insuflacion == "Insuficiente" || i.Insuflacion == "Correcta" || i.Insuflacion == "Excesiva"){
                cantidadInsuflacionesTotales = cantidadInsuflacionesTotales + 1
                if(i.PosicionCabeza == "Incorrecta"){
                    cantidadInsuflacionesIncorrectas = cantidadInsuflacionesIncorrectas + 1
                }
            }
        }
        if(cantidadInsuflacionesTotales == 0 || cantidadInsuflacionesIncorrectas == 0){
            return 0
        }
        return (cantidadInsuflacionesIncorrectas / cantidadInsuflacionesTotales) * 100
    }
    
    // TODO -> No se va a hacer.
    public static func fuerzaPromedioAplicada(instantes: [Instante]) -> Double{
        return 10.0;
    }
    
    public static func calidadInsuflaciones(instantes: [Instante]) -> String{
        let porcentajeInsuflacionesCorrectas = Instante.porcentajeInsuflacionesCorrectas(instantes: instantes)
        if(porcentajeInsuflacionesCorrectas > 0.5){
            return "Optima"
        }
        else{
            return "Mala"
        }
    }
}
