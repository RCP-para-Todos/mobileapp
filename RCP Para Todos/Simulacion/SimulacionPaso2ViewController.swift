//
//  SimulacionPaso2ViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 01/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit
import UICircularProgressRing

class SimulacionPaso2ViewController: UIViewController
{
    @IBOutlet weak var progressBar: UICircularTimerRing!
    @IBOutlet weak var buttonElEntornoNoEsSeguro: UIButton!
    @IBOutlet weak var buttonLlamarAmbulancia: UIButton!
    
    var ambulanciaClicked : Bool = false
    var entornoNoSeguroClicked : Bool = false
    var elEntornoEsSeguro : Bool = false
    
    override func viewDidLoad()
    {
        self.recibirSimuladorSiElEntornoEsSeguro()
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.loadProgressCircleBar()
        self.initInterface()
    }
        
    func initInterface(){
        self.buttonElEntornoNoEsSeguro.layer.cornerRadius = 15
        self.buttonLlamarAmbulancia.layer.cornerRadius = 15
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Simulacion RCP Paso 2"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func loadProgressCircleBar(){
        self.progressBar.startTimer(to: Constants.SIMULACION_DURACION_SEGUNDOS_PASO2) { state in
            switch state {
            case .finished:
                self.logicaSimulacion()
                print("finished")
            case .continued(let time):
                print("continued: \(String(describing: time))")
            case .paused(let time):
                print("paused: \(String(describing: time))")
            }
        }
    }
    
    func logicaSimulacion(){
        //ENTORNO SEGURO
        
        //Si el entorno es seguro y se selecciona llamar a la ambulancia se realiza la simulacion.
        if(self.elEntornoEsSeguro && self.ambulanciaClicked && !self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3Simulacion", sender: nil)
        }
        //Si el entorno es seguro pero no selecciona para llamar a la ambulancia se realiza la simulacion pero sera invalidada finalmente porque la ambulancia nunca llegara.
        else if(self.elEntornoEsSeguro && !self.ambulanciaClicked && !self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3Simulacion", sender: nil)
        }
            
        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        else if(self.elEntornoEsSeguro && self.ambulanciaClicked && self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3AlternativoSimulacion", sender: nil)
        }
            
        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        else if(self.elEntornoEsSeguro && !self.ambulanciaClicked && self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3AlternativoSimulacion", sender: nil)
        }
        
        //ENTORNO NO SEGURO
            
        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion correctamente.
        else if(!self.elEntornoEsSeguro && self.ambulanciaClicked && self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3AlternativoSimulacion", sender: nil)
        }
        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que no se llamo a la ambulancia.
        else if(!self.elEntornoEsSeguro && !self.ambulanciaClicked && self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3AlternativoSimulacion", sender: nil)
        }
        //Si el entorno no es seguro pero no se selecciona el entorno no es seguro, se realiza la simulacion pero sera invalidada finalmente porque el entorno no era seguro.
        else if(!self.elEntornoEsSeguro && !self.ambulanciaClicked && !self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3Simulacion", sender: nil)
        }
        //Si el entorno no es seguro pero se selecciona llamar a la ambulancia, se realiza la simulacion pero sera invalidada finalmente porque el entorno no era seguro.
        else if(!self.elEntornoEsSeguro && self.ambulanciaClicked && !self.entornoNoSeguroClicked){
            self.performSegue(withIdentifier: "paso3Simulacion", sender: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "paso3Simulacion" {
            if let destinationVC = segue.destination as? SimulacionPaso3ViewController {
                destinationVC.ambulanciaClicked = self.ambulanciaClicked
                destinationVC.entornoNoSeguroClicked = self.entornoNoSeguroClicked
                destinationVC.elEntornoEsSeguro = self.elEntornoEsSeguro
            }
        }
        if segue.identifier == "paso3AlternativoSimulacion" {
            if let destinationVC = segue.destination as? SimulacionPaso3AlternativoViewController {
                destinationVC.ambulanciaClicked = self.ambulanciaClicked
                destinationVC.entornoNoSeguroClicked = self.entornoNoSeguroClicked
                destinationVC.elEntornoEsSeguro = self.elEntornoEsSeguro
            }
        }
    }
    
    func recibirSimuladorSiElEntornoEsSeguro(){
        self.elEntornoEsSeguro = true
    }
    
    @IBAction func buttonEntornoNoSeguroClicked(_ sender: Any) {
        self.entornoNoSeguroClicked = true
        self.buttonLlamarAmbulancia.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
    }
    
    @IBAction func buttonLlamarAmbulancia(_ sender: Any) {
        self.ambulanciaClicked = true
        self.buttonElEntornoNoEsSeguro.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
    }
    
    
}
