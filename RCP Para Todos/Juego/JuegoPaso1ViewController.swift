//
//  JuegoPaso1ViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 11/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class JuegoPaso1ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    @IBOutlet weak var pickerDificultad: UIPickerView!
    
    let arrayDificultad : [String] = ["Facil (1M)", "Medio (3M)", "Dificil (5M)"]
    
    var dificultadSelected : String = "Facil (1M)"
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.initDelegates()
        self.inicializarBarraSuperior()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Juego RCP"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func initDelegates(){
        self.pickerDificultad.delegate = self
        self.pickerDificultad.dataSource = self
    }
    
    
    @IBAction func buttonJugarClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "juego2Segue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "juego2Segue" {
            if let destinationVC = segue.destination as? JuegoPaso2ViewController {
                var tiempoSelected : Int
                if(self.dificultadSelected == "Facil (1M)"){
                    tiempoSelected = 1 * 60000
                }
                else if(self.dificultadSelected == "Medio (3M)"){
                    tiempoSelected = 3 * 60000
                }
                else{
                    tiempoSelected = 5 * 60000
                }
                destinationVC.count = tiempoSelected
            }
        }
    }
    
    internal func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    internal func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == self.pickerDificultad {
            return self.arrayDificultad.count
        }
        return 0
    }
    
    internal func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == self.pickerDificultad {
            return self.arrayDificultad[row]
        }
        return nil
    }
    
    internal func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == self.pickerDificultad {
            self.dificultadSelected = self.arrayDificultad[row]
        }
    }
    
}
