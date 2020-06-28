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
    
    var service : Services?
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.service = Services()
        activityIndicatorSpinner.stopAnimating()
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    @IBAction func registrarClicked(_ sender: Any) {
        activityIndicatorSpinner.startAnimating()
        let parameters: [String: String] = [
            "name" : usuarioInput.text!,
            "password" : passwordInput.text!,
            "perfil" : (perfilInput.selectedSegmentIndex == 0) ? "Practicante" : "Usuario"
        ]
        self.service?.register(parameters: parameters, completion: self.completion)
    }
    
    func completion(result: Bool){
        self.activityIndicatorSpinner.stopAnimating()
        if(result){
            navigationController?.popViewController(animated: true)
        }
        else{
            print("ERROR AL REGISTRAR")
        }
    }
    
}

