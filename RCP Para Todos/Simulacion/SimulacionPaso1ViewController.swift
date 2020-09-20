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
    @IBOutlet weak var labelDescripcion: UILabel!
    
    var escenarioRandom : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.elegirEscenarioRandom()
        self.initInterface()
    }
        
    func initInterface(){
        self.buttonIniciar.layer.cornerRadius = 15
    }
    
    func elegirEscenarioRandom(){
        self.escenarioRandom = Int.random(in: 0...5)
        if(self.escenarioRandom == 0){
            self.labelDescripcion.text = "Durante tus vacaciones luego de una larga caminata, la persona que te acompaña se agarra el pecho y cae desplomada"
            self.imagenSimuacion.image = UIImage(named: "escenario1")
        }
        else if(self.escenarioRandom == 1){
            self.labelDescripcion.text = "Trabajando en una obra en la cual se realiza un zanjeo para el recambio de cables subterráneos, tu compañero luego de clavar la pala cae desplomado"
            self.imagenSimuacion.image = UIImage(named: "escenario2")
        }
        else if(self.escenarioRandom == 2){
            self.labelDescripcion.text = "Estabas en tu casa descansando cuando escuchás un fuerte ruido en la habitación de al lado. Al acercarte para ver que pasó, encontrás a un familiar en el suelo, no responde"
            self.imagenSimuacion.image = UIImage(named: "escenario3")
        }
        else if(self.escenarioRandom == 3){
            self.labelDescripcion.text = "Manejando por la ruta, a lo lejos ves un obstáculo en tu camino. Al acercarte un poco más te encontrás con un camión volcado y un fuerte olor a combustible. Muy cerca del camión ves al conductor y aparentemente no se mueve"
            self.imagenSimuacion.image = UIImage(named: "escenario4")
        }
        else if(self.escenarioRandom == 4){
            self.labelDescripcion.text = "Luego de escuchar un fuerte golpe, te acercás corriendo al lugar y encontrás a una mujer desmayada"
            self.imagenSimuacion.image = UIImage(named: "escenario5")
        }
        else if(self.escenarioRandom == 5){
            self.labelDescripcion.text = "Mientras charlabas con tu padre, de pronto deja de hablar y se desploma"
            self.imagenSimuacion.image = UIImage(named: "escenario6")
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


