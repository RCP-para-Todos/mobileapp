//
//  ServiceCurso.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 26/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class ServiceCurso
{
    public func newCurso(parameters: [String: Any], completion: @escaping (Bool) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)courses", method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON {
          response in
            completion(true)
        }
    }
    
    public func getCursosPorTutor(completion: @escaping ([String], [String]) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let usuarioActivo = UserDefaults.standard.string(forKey: "usuarioActivo")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)courses?q=\(usuarioActivo!.lowercased())", method: .get, headers: headers).responseJSON {
          response in
            guard let data = response.value else { return }
            var arrayResponseNames : [String] = []
            var arrayResponseIds : [String] = []
            let jsonData = JSON(data)
            for i in jsonData{
                arrayResponseIds.append(i.1["_id"].stringValue)
                arrayResponseNames.append(i.1["name"].stringValue)
            }
            arrayResponseIds = Array(arrayResponseIds).sorted(by: <)
            arrayResponseNames = Array(arrayResponseNames).sorted(by: <)
            completion(arrayResponseIds, arrayResponseNames)
        }
    }
    
    public func getCursoById(id: String, completion: @escaping ([String], [String]) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)courses/\(id.lowercased())", method: .get, headers: headers).responseJSON {
          response in
            guard let data = response.value else { return }
            var arrayResponseNames : [String] = []
            var arrayResponseIds : [String] = []
            let jsonData = JSON(data)["students"]
            for i in jsonData{
                arrayResponseIds.append(i.1["_id"].stringValue)
                arrayResponseNames.append(i.1["name"].stringValue)
            }
            completion(arrayResponseIds, arrayResponseNames)
        }
    }
    
}

