//
//  Evento.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 02/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation

class Evento {
    var id: String
    var fecha: String
    var usuario: String
    var curso: String
    var tipo: String
    var duracion: String
    var puntaje: Double?
    var tiempoInactividad: Double?
    var porcentajeSobrevida: Double?
    var porcentajeInsuflacion: Double?
    var porcentajeCompresion: Double?
    var cabezaPosicion: Double?
    var fuerzaPromedioAplicada: Double?
    var calidadInsuflaciones: String?
    //TUTOR
    var brazosFlexionados: Bool?
    var noConsultaEstadoVictima: Bool?
    var noEstaAtentoAlEscenario: Bool?
    var disponeAyudaNoSolicita: Bool?
    var demoraTomaDecisiones: Bool?
    var observaciones: String?

    init(id: String, fecha: String, usuario: String, curso: String, tipo: String, duracion: String) {
        self.id = id
        self.fecha = fecha
        self.usuario = usuario
        self.curso = curso
        self.tipo = tipo
        self.duracion = duracion
    }
}
