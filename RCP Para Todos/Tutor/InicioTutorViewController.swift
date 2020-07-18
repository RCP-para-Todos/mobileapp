//
//  InicioTutorViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 18/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class InicioTutorViewController: UIViewController
{

    override func viewDidLoad()
    {
        super.viewDidLoad()
    }
    @IBAction func logoutClicked(_ sender: Any) {
           let alert = UIAlertController(title: "Sesión", message: "Desea cerrar sesión?", preferredStyle: .alert)
           alert.addAction(UIAlertAction(title: "Cerrar sesión", style: UIAlertAction.Style.destructive, handler: { action in
               self.navigationController?.popViewController(animated: true)

           }))
           alert.addAction(UIAlertAction(title: "Cancelar", style: UIAlertAction.Style.default, handler: { action in
           }))
           self.present(alert, animated: true, completion: nil)
       }
}
