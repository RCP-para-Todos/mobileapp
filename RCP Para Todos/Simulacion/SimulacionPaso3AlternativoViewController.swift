//
//  SimulacionPaso3AlternativoViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 01/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class SimulacionPaso3AlternativoViewController: UIViewController
{
    @IBOutlet weak var buttonIrAlInicio: UIButton!
    
    var ambulanciaClicked : Bool = false
    var entornoNoSeguroClicked : Bool = false
    var elEntornoEsSeguro : Bool = false
    
    
    @IBOutlet weak var labelResultado: UILabel!
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.logicaResultado()
        self.initInterface()
    }
        
    func initInterface(){
        self.buttonIrAlInicio.layer.cornerRadius = 15
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Simulacion RCP Resultado"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func logicaResultado(){
        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
       if(self.elEntornoEsSeguro && !self.ambulanciaClicked && self.entornoNoSeguroClicked){
        self.labelResultado.text = "Lo sentimos, el entorno era seguro, correspondía realizar la maniobra RCP y llamar a la ambulancia"
       }
        //Si el entorno es seguro pero se selecciona el entorno no es seguro, se finalizara sin simulacion con error ya que el entorno era seguro.
        if(self.elEntornoEsSeguro && self.ambulanciaClicked && self.entornoNoSeguroClicked){
         self.labelResultado.text = "Lo sentimos, el entorno era seguro, correspondía realizar la maniobra RCP y esperar a la ambulancia"
        }
       //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion correctamente.
       else if(!self.elEntornoEsSeguro && self.ambulanciaClicked && self.entornoNoSeguroClicked){
           self.labelResultado.text = "Felicitaciones, el entorno no era seguro y no correspondía realizar la maniobra RCP"
       }
        //Si el entorno no es seguro y se selecciona el entorno no es seguro, se finalizara sin simulacion correctamente.
        else if(!self.elEntornoEsSeguro && !self.ambulanciaClicked && self.entornoNoSeguroClicked){
            self.labelResultado.text = "Lo sentimos, el entorno no era seguro y no correspondía realizar la maniobra RCP pero se debía llamar a la ambulancia"
        }
    }
    
    @IBAction func buttonIrAlInicioClicked(_ sender: Any) {
        self.navigationController!.popViewControllers(viewsToPop: 3)
    }
    
}
	
