//
//  ServicePracticante.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 26/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class ServicePracticante
{
    public func getPracticantesSinCurso(completion: @escaping ([String]) -> Void){
        let token = UserDefaults.standard.string(forKey: "token")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token!)",
              "Accept": "application/json"
          ]
        AF.request("\(Constants.GLOBAL_ENDPOINT)users?q=nocourse", method: .get, headers: headers).responseJSON {
          response in
            guard let data = response.value else { return }
            var arrayResponse : [String] = []
            let jsonData = JSON(data)
            for i in jsonData{
                arrayResponse.append(i.1["name"].stringValue)
            }
            completion(arrayResponse)
        }
    }
}
