//
//  JuegoPaso3ViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 11/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class JuegoPaso3ViewController: UIViewController
{
    
    @IBOutlet weak var labelPuntaje: UILabel!
    
    var puntaje : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.labelPuntaje.text  = String(self.puntaje)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Juego RCP"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @IBAction func irAlInicioClicked(_ sender: Any) {
        self.navigationController!.popViewControllers(viewsToPop: 3)
    }
}
