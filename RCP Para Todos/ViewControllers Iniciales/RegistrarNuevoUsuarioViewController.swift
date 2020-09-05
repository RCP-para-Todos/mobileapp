//
//  RegistrarNuevoUsuarioViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 28/06/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class RegistrarNuevoUsuario: UIViewController
{
    @IBOutlet weak var usuarioInput: UITextField!
    @IBOutlet weak var passwordInput: UITextField!
    @IBOutlet weak var activityIndicatorSpinner: UIActivityIndicatorView!
    @IBOutlet weak var perfilInput: UISegmentedControl!
    @IBOutlet weak var buttonRegistrarNuevaCuenta: UIButton!
    
    var service : ServiceUser?
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.service = ServiceUser()
        self.activityIndicatorSpinner.stopAnimating()
        self.initInterface()
    }
    
    func initInterface(){
        self.buttonRegistrarNuevaCuenta.layer.cornerRadius = 15
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    @IBAction func registrarClicked(_ sender: Any) {
        self.activityIndicatorSpinner.startAnimating()
        let parameters: [String: String] = [
            "name" : usuarioInput.text!,
            "password" : passwordInput.text!,
            "rol" : (perfilInput.selectedSegmentIndex == 0) ? "usuario" : "instructor"
        ]
        self.service?.register(parameters: parameters, completion: self.completion)
    }
    
    func completion(result: Bool){
        self.activityIndicatorSpinner.stopAnimating()
        if(result){
            self.navigationController?.popViewController(animated: true)
        }
        else{
            print("ERROR AL REGISTRAR")
        }
    }
    
}

