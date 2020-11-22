//
//  ServiceEvento.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 26/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class ServiceEvento{
    public func newEvento(parameters: [String: Any], completion: @escaping (Bool) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)events", method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON {
          response in
            completion(true)
        }
    }
    
    public func updateEventoWithObservations(id: String, parameters: [String: Any], completion: @escaping (Bool) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)events/\(id)", method: .patch, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON {
          response in
            completion(true)
        }
    }
    
    public func getEventosPorPracticante(practicante: String, completion: @escaping ([String], [String]) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)events?q=\(practicante.lowercased())", method: .get, headers: headers).responseJSON {
          response in
            guard let data = response.value else { return }
            var arrayResponseNames : [String] = []
            var arrayResponseIds : [String] = []
            let jsonData = JSON(data)
            for i in jsonData{
                arrayResponseIds.append(i.1["_id"].stringValue)
                let finalDate = Utils.translateDateMongo(date: i.1["Created_date"].stringValue)
                arrayResponseNames.append(finalDate)
            }
            arrayResponseIds = Array(arrayResponseIds).sorted(by: >)
            arrayResponseNames = Array(arrayResponseNames).sorted(by: >)
            for i in arrayResponseNames.indices{
                arrayResponseNames[i] = Utils.translateDateToEasyRead(date: arrayResponseNames[i])
            }
            completion(arrayResponseIds, arrayResponseNames)
        }
    }
    
    public func getEventoById(id: String, completion: @escaping (Evento) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)events/\(id)", method: .get, headers: headers).responseJSON {
          response in
            guard let data = response.value else { return }
            var evento : Evento
            let jsonData = JSON(data)
            evento = Evento(id: jsonData["_id"].stringValue, fecha: jsonData["Created_date"].stringValue, usuario: jsonData["user"].stringValue, curso: jsonData["course"].stringValue, tipo: jsonData["type"].stringValue, duracion: jsonData["duration"].stringValue)
                evento.tiempoInactividad = jsonData["tiempoInactividad"].doubleValue
                evento.porcentajeSobrevida = jsonData["porcentajeSobrevida"].doubleValue
                evento.porcentajeInsuflacion = jsonData["porcentajeInsuflacionOk"].doubleValue
                evento.porcentajeCompresion = jsonData["porcentajeCompresionOk"].doubleValue
                evento.cabezaPosicion = jsonData["cantidadInsuflacionesOkMalCabeza"].doubleValue
                evento.fuerzaPromedioAplicada = jsonData["fuerzaPromedioAplicada"].doubleValue
                evento.calidadInsuflaciones = jsonData["calidadInsuflaciones"].stringValue
                evento.puntaje = jsonData["puntaje"].doubleValue
                evento.brazosFlexionados = jsonData["brazosFlexionados"].boolValue
                evento.noConsultaEstadoVictima = jsonData["noConsultaEstadoVictima"].boolValue
                evento.noEstaAtentoAlEscenario = jsonData["noEstaAtentoAlEscenario"].boolValue
                evento.disponeAyudaNoSolicita = jsonData["disponeAyudaNoSolicita"].boolValue
                evento.demoraTomaDecisiones = jsonData["demoraTomaDesiciones"].boolValue
                evento.observaciones = jsonData["observations"].stringValue
                completion(evento)
        }
    }
}
