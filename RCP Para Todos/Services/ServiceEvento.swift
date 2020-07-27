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
        print(parameters)
        AF.request("\(Constants.GLOBAL_ENDPOINT)events", method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON {
          response in
            print(response)
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
                arrayResponseNames.append(i.1["Created_date"].stringValue)
            }
            print(arrayResponseNames)
            completion(arrayResponseIds, arrayResponseNames)
        }
    }
}
