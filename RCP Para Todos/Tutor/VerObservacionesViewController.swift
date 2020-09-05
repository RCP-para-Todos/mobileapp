//
//  VerObservacionesViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 02/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class VerObservacionesViewController: UIViewController{
    
    @IBOutlet weak var labelPracticante: UILabel!
    @IBOutlet weak var labelFecha: UILabel!
    @IBOutlet weak var primerCheckBox: UIButton!
    @IBOutlet weak var segundoCheckBox: UIButton!
    @IBOutlet weak var tercerCheckBox: UIButton!
    @IBOutlet weak var cuartoCheckBox: UIButton!
    @IBOutlet weak var quintoCheckBox: UIButton!
    @IBOutlet weak var textObservaciones: UITextView!
    @IBOutlet weak var buttonRegistrar: UIButton!
    
    let rol = UserDefaults.standard.string(forKey: "rol")
    var evento: Evento?
    var serviceEvento: ServiceEvento?
    
    //Checkbox
    var primeroEstado : Bool = false
    var segundoEstado : Bool = false
    var terceroEstado : Bool = false
    var cuartoEstado : Bool = false
    var quintoEstado : Bool = false
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        self.serviceEvento = ServiceEvento()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.inicializarBarraSuperior()
        self.initInterface()
    }
    
    func initInterface(){
        self.buttonRegistrar.layer.cornerRadius = 15
        self.labelFecha.text = Utils.translateDateMongo(date: evento!.fecha)
        self.labelPracticante.text = "Ian" //Hardcodeado jaja ya me canse por hoy.
        self.primeroEstado = evento!.brazosFlexionados ?? false
        self.segundoEstado = evento!.noConsultaEstadoVictima ?? false
        self.terceroEstado = evento!.noEstaAtentoAlEscenario ?? false
        self.cuartoEstado
            = evento!.disponeAyudaNoSolicita ?? false
        self.quintoEstado = evento!.demoraTomaDecisiones ?? false
        self.textObservaciones.text = evento!.observaciones
        
        self.logicaCheckBox1()
        self.logicaCheckBox2()
        self.logicaCheckBox3()
        self.logicaCheckBox4()
        self.logicaCheckBox5()
        
        if(self.rol == "instructor"){
            self.primerCheckBox.isEnabled = true
            self.segundoCheckBox.isEnabled = true
            self.tercerCheckBox.isEnabled = true
            self.cuartoCheckBox.isEnabled = true
            self.quintoCheckBox.isEnabled = true
            self.textObservaciones.isEditable = true
            self.buttonRegistrar.setTitle("Registrar", for: .normal)
        }
        else{
            self.primerCheckBox.isEnabled = false
            self.segundoCheckBox.isEnabled = false
            self.tercerCheckBox.isEnabled = false
            self.cuartoCheckBox.isEnabled = false
            self.quintoCheckBox.isEnabled = false
            self.textObservaciones.isEditable = false
            self.buttonRegistrar.setTitle("Volver", for: .normal)
        }
        
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Ver observaciones"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    // MARK: FUNCIONES CHECKBOX
    
    @IBAction func primerCheckBoxClicked(_ sender: Any) {
        self.logicaCheckBox1()
    }
    @IBAction func segundoCheckBoxClicked(_ sender: Any) {
        self.logicaCheckBox2()
    }
    @IBAction func tercerCheckBoxClicked(_ sender: Any) {
        self.logicaCheckBox3()
    }
    @IBAction func cuartoCheckBoxClicked(_ sender: Any) {
        self.logicaCheckBox4()
    }
    @IBAction func quintoCheckBoxClicked(_ sender: Any) {
        self.logicaCheckBox5()
    }
    
    func logicaCheckBox1(){
        if(self.primeroEstado){
            self.primerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.primeroEstado = false
        }
        else{
            self.primerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.primeroEstado = true
        }
    }
    
    func logicaCheckBox2(){
        if(self.segundoEstado){
            self.segundoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.segundoEstado = false
        }
        else{
            self.segundoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.segundoEstado = true
        }
    }
    
    func logicaCheckBox3(){
        if(self.terceroEstado){
            self.tercerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.terceroEstado = false
        }
        else{
            self.tercerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.terceroEstado = true
        }
    }
    
    func logicaCheckBox4(){
        if(self.cuartoEstado){
            self.cuartoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.cuartoEstado = false
        }
        else{
            self.cuartoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.cuartoEstado = true
        }
    }
    
    func logicaCheckBox5(){
        if(self.quintoEstado){
            self.quintoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.quintoEstado = false
        }
        else{
            self.quintoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.quintoEstado = true
        }
    }
    
    @IBAction func buttonRegistrarClicked(_ sender: Any) {
        if(self.rol == "instructor"){
            let parameters : [String: Any] = [
            "brazosFlexionados": self.primeroEstado,
            "noConsultaEstadoVictima": self.segundoEstado,
            "noEstaAtentoAlEscenario": self.terceroEstado,
            "disponeAyudaNoSolicita": self.cuartoEstado,
            "demoraTomaDesiciones": self.quintoEstado,
            "observations":self.textObservaciones.text!
            ]
            self.serviceEvento?.updateEventoWithObservations(id: evento!.id, parameters: parameters, completion: self.newEvento)
        }
        self.navigationController?.popViewController(animated: true)
    }
    
    func newEvento(completion: Bool){
        
    }
    
    // MARK: FUNCIONES DE TECLADO
    
    // Adaptación de la pantalla al mostrar ú ocultar el teclado.
    func setupHideKeyboardOnTap()
    {
        self.view.addGestureRecognizer(self.endEditingRecognizer())
        self.navigationController?.navigationBar.addGestureRecognizer(self.endEditingRecognizer())
    }
    
    // Adaptación de la pantalla al mostrar ú ocultar el teclado.
    private func endEditingRecognizer() -> UIGestureRecognizer
    {
        let tap = UITapGestureRecognizer(target: self.view, action: #selector(self.view.endEditing(_:)))
        tap.cancelsTouchesInView = false
        return tap
    }
    // Adaptación de la pantalla al mostrar ú ocultar el teclado.
    @objc func keyboardWillShow(notification: NSNotification)
    {
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue
        {
            if self.view.frame.origin.y == 0
            {
                self.view.frame.origin.y -= keyboardSize.height + 100
            }
        }
    }
    // Adaptación de la pantalla al mostrar ú ocultar el teclado.
    @objc func keyboardWillHide(notification: NSNotification)
    {
        if self.view.frame.origin.y != 0
        {
            self.view.frame.origin.y = 0
        }
    }
}
