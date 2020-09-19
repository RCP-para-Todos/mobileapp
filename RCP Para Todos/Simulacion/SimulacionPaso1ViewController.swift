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
    @IBOutlet weak var buttonIniciar: UIButton!
    @IBOutlet weak var imagenSimuacion: UIImageView!
    
    var escenarioRandom : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.initInterface()
    }
        
    func initInterface(){
        self.buttonIniciar.layer.cornerRadius = 15
    }
    
    func elegirEscenarioRandom(){
        self.escenarioRandom = Int.random(in: 0...1)
        if(self.escenarioRandom == 0){
            self.imagenSimuacion.image = UIImage(named: "SimulacionPaso1")
        }
        else if(self.escenarioRandom == 1){
            //TODO
        }
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "paso2Simulacion" {
            if let destinationVC = segue.destination as? SimulacionPaso2ViewController {
                destinationVC.escenarioRandom = self.escenarioRandom
            }
        }
    }
    
    
}


