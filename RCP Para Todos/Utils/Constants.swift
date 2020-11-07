//
//  Constants.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 25/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

struct Constants {
    //App Constants
    static let GLOBAL_ENDPOINT = "https://api.rcp.margaale.ddnss.de/"
    static let COLOR_BOTON_DESACTIVADO = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
    static let COLOR_BOTON_ACTIVADO = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
    static let IMAGE_COMPRESION = UIImage(systemName: "heart.fill")
    static let IMAGE_INSUFLACION = UIImage(systemName: "wind")
    static let APRENDIZAJE_PORCENTAJE_COMPRESIONES_VALIDAS = 0.2
    static let APRENDIZAJE_PORCENTAJE_INSUFLACIONES_VALIDAS = 0.2
    static let APRENDIZAJE_DURACION_SEGUNDOS_COMPRESION_INSUFLACION = 30.0
    static let APRENDIZAJE_DURACION_SEGUNDOS_FINAL = 68.0
    static let SIMULACION_DURACION_SEGUNDOS_PASO2 = 10.0
    static let SIMULACION_DURACION_SEGUNDOS_PASO3 = 60.0
}
