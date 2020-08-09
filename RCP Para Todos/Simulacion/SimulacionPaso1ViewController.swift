//
//  SimulacionPaso1ViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 28/06/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class SimulacionPaso1ViewController: UIViewController
{
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Simulacion RCP Paso 1"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @IBAction func botonIniciarClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "paso2Simulacion", sender: nil)
    }
    
    
}


