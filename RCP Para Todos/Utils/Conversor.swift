//
//  Conversor.swift
//  RCP Para Todos
//
//  Created by Reflejo on 07/06/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation

class Conversor{
    class func insuflacionToString(n: Int) -> String{
        switch(n){
        case 0: return "Nula"
        case 1: return "Insuficiente"
        case 2: return "Correcta"
        case 3: return "Excesiva"
        default:
            return ""
        }
    }
    
    class func compresionToString(n: Int) -> String{
        switch(n){
        case 0: return "Nula"
        case 1: return "Insuficiente"
        case 2: return "Correcta"
        case 3: return "Excesiva"
        default:
            return ""
        }
    }
    
    class func posicionToString(n: Int) -> String{
        switch(n){
        case 1: return "Acostado"
        case 2: return "Reclinado"
        default:
            return ""
        }
    }
}
