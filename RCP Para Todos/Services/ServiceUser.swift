//
//  Services.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 28/06/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class ServiceUser
{
    public func login(parameters: [String: String], completion: @escaping (Bool) -> Void){
        //Test sin backend
        /*let defaults = UserDefaults.standard
        defaults.set("practicante", forKey: "rol")
        defaults.set("test", forKey: "curso")
        completion(true)*/
        AF.request("\(Constants.GLOBAL_ENDPOINT)auth/login", method: .post, parameters: parameters, encoding: JSONEncoding.default)
        .responseJSON { result in
            if let value = result.value as? [String: Any] {
                if(value["auth"]! as! Int == 0){
                    completion(false)
                }
                else{
                    let defaults = UserDefaults.standard
                    let courses = JSON(value["courses"]!)
                    defaults.set(parameters["name"], forKey: "usuarioActivo")
                    defaults.set(value["token"]!, forKey: "token")
                    defaults.set(value["refreshToken"]!, forKey: "refreshToken")
                    defaults.set(value["rol"]!, forKey: "rol")
                    //ELIMINAR ESTA ASQUEROSIDAD
                    print(courses)
                    for i in courses{
                        let cu = (i.1["_id"].stringValue)
                        defaults.set(cu, forKey: "curso")
                    }
                    completion(true)
                }
                return
            }
            else{
                completion(false)
            }
        }
    }
    
    public func register(parameters: [String: String], completion: @escaping (Bool) -> Void){
        AF.request("\(Constants.GLOBAL_ENDPOINT)auth/register", method: .post, parameters: parameters, encoding: JSONEncoding.default)
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

        AF.request("\(Constants.GLOBAL_ENDPOINT)users", method: .get, headers: headers).responseJSON { response in
            debugPrint(response)
        }
    }
    
    public func guardarCreedencialesProximoinicio(usuario: String, contrasena: String){
        let defaults = UserDefaults.standard
        defaults.set(usuario, forKey: "usuarioRecordar")
        defaults.set(contrasena, forKey: "contrasenaRecordar")
    }
    
    public func obtenerCredencialesProximoInicio() -> (String?, String?){
        return (UserDefaults.standard.string(forKey: "usuarioRecordar"), UserDefaults.standard.string(forKey: "contrasenaRecordar"))
    }
}
