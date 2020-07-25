//
//  Services.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 28/06/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import Alamofire

class ServiceUser
{
    public func login(parameters: [String: String], completion: @escaping (Bool) -> Void){
        completion(true)
        /*AF.request("https://api.rcp.margaale.ddnss.de/auth/login", method: .post, parameters: parameters, encoding: JSONEncoding.default)
        .responseJSON { result in
            if let value = result.value as? [String: Any] {
                if(value["auth"]! as! Int == 0){
                    completion(false)
                }
                else{
                    let defaults = UserDefaults.standard
                    defaults.set(value["token"]!, forKey: "token")
                    defaults.set(value["refreshToken"]!, forKey: "refreshToken")
                    completion(true)
                }
                return
            }
            else{
                completion(false)
            }
        }*/
    }
    
    public func register(parameters: [String: String], completion: @escaping (Bool) -> Void){
        AF.request("https://api.rcp.margaale.ddnss.de/auth/register", method: .post, parameters: parameters, encoding: JSONEncoding.default)
        .responseJSON { result in
            if let value = result.value as? [String: Any] {
                if(value["auth"]! as! Int == 0){
                    completion(false)
                }
                else{
                    completion(true)
                }
                return
            }
        }
    }
    
    public func getUserByToken(token: String/*, completion: @escaping (Bool) -> Void*/){
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token)",
            "Accept": "application/json"
        ]

        AF.request("https://api.rcp.margaale.ddnss.de/users", method: .get, headers: headers).responseJSON { response in
            debugPrint(response)
        }
    }
}
