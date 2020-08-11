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
    @IBOutlet weak var pickerTiempo: UIPickerView!
    
    let arrayDificultad : [String] = ["Facil", "Medio", "Dificil"]
    let arrayTiempo : [String] = ["1M", "2M", "3M","4M", "5M", "6M","7M", "8M", "9M"]
    
    var dificultadSelected : String = "Facil"
    var tiempoSelected : String = "1M"
    
    
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
        self.pickerTiempo.delegate = self
        self.pickerTiempo.dataSource = self
    }
    
    
    @IBAction func buttonJugarClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "juego2Segue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "juego2Segue" {
            if let destinationVC = segue.destination as? JuegoPaso2ViewController {
                var tiempoSelectedTemp = self.tiempoSelected
                tiempoSelectedTemp = String(tiempoSelectedTemp.dropLast())
                let tiempo : Int = Int(tiempoSelectedTemp)!*60000 //MILISEGUNDOS
                destinationVC.count = tiempo
            }
        }
    }
    
    internal func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    internal func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == self.pickerDificultad {
            return self.arrayDificultad.count
        } else if pickerView == self.pickerTiempo{
            return self.arrayTiempo.count
        }
        return 0
    }
    
    internal func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == self.pickerDificultad {
            return self.arrayDificultad[row]
        } else if pickerView == self.pickerTiempo{
            return self.arrayTiempo[row]
        }
        return nil
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == self.pickerDificultad {
            self.dificultadSelected = self.arrayDificultad[row]
        } else if pickerView == self.pickerTiempo{
            self.tiempoSelected = self.arrayTiempo[row]
        }

    }
    
}
