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

    init(insuflacion: String, compresion: String, posicion: String) {
        self.Insuflacion = insuflacion
        self.Compresion = compresion
        self.Posicion = posicion
    }
}
