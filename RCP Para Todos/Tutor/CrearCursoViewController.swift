//
//  CrearCursoViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 18/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class CrearCursoViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate{
    
    @IBOutlet weak var textNombreCurso: UITextField!
    @IBOutlet weak var textNombrePracticante: UITextField!
    @IBOutlet weak var buttonAgregarPracticante: UIButton!
    @IBOutlet weak var tableView: UITableView!
    
    var practicantes: [String] = []
    var servicePracticante : ServicePracticante?
    var serviceCurso : ServiceCurso?
    //ESTO VIENE DEL BACK
    var suggestionsArray : [String] = []
    //var suggestionsArray = ["Juan", "Jorge", "Jose", "Tomas", "Matias", "Tiago"]
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.servicePracticante = ServicePracticante()
        self.serviceCurso = ServiceCurso()
        self.getPracticantesSinCurso()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.inicializarBarraSuperior()
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Crear curso"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @IBAction func buttonAgregarPracticanteClicked(_ sender: Any) {
        let practicanteNuevo = self.textNombrePracticante.text!
        if(self.suggestionsArray.contains(practicanteNuevo) && !self.practicantes.contains(practicanteNuevo)){
            self.practicantes.append(self.textNombrePracticante.text!)
            self.tableView.reloadData()
            self.textNombrePracticante.text = ""
        }
        else if(self.suggestionsArray.contains(practicanteNuevo) && self.practicantes.contains(practicanteNuevo)){
            let alert = UIAlertController(title: "Practicante ya agregado", message: "No puede agregar dos veces el mismo practicante", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
        else{
            let alert = UIAlertController(title: "Usuario inválido", message: "Usuario no encontrado", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    @IBAction func buttonGuardarCursoClicked(_ sender: Any) {
        let nombreCurso = self.textNombreCurso.text
        let usuarioActivo = UserDefaults.standard.string(forKey: "usuarioActivo")
        let event_date = Utils.hoy()
        let practicantes = self.practicantes
        let parameters : [String: Any] = [
            "name" : nombreCurso!,
            "event_date" : event_date,
            "instructor" : usuarioActivo!,
            "students" : practicantes
            ]
        self.serviceCurso?.newCurso(parameters: parameters, completion: self.cursoCreado)
        self.navigationController?.popViewController(animated: true)
    }
    
    func getPracticantesSinCurso(){
        self.servicePracticante?.getPracticantesSinCurso(completion: self.llenarArray)
    }
    
    func llenarArray(array:[String]){
        self.suggestionsArray = array
        self.textNombrePracticante.delegate = self
    }
    
    func cursoCreado(response: Bool){
        
    }
    
    // MARK: DELEGADOS
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return !self.autoCompleteText( in : textField, using: string, suggestionsArray: suggestionsArray)
    }
    
    func autoCompleteText( in textField: UITextField, using string: String, suggestionsArray: [String]) -> Bool {
        if !string.isEmpty,
            let selectedTextRange = textField.selectedTextRange,
            selectedTextRange.end == textField.endOfDocument,
            let prefixRange = textField.textRange(from: textField.beginningOfDocument, to: selectedTextRange.start),
            let text = textField.text( in : prefixRange) {
            let prefix = text + string
            let matches = suggestionsArray.filter {
                $0.hasPrefix(prefix)
            }
            if (matches.count > 0) {
                textField.text = matches[0]
                if let start = textField.position(from: textField.beginningOfDocument, offset: prefix.count) {
                    textField.selectedTextRange = textField.textRange(from: start, to: textField.endOfDocument)
                    return true
                }
            }
        }
        return false
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder()
            return true
    }
    
    func tableView(_ tableView:UITableView, numberOfRowsInSection section:Int) -> Int
    {
        return self.practicantes.count
    }
     
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:UITableViewCell=UITableViewCell(style: UITableViewCell.CellStyle.subtitle, reuseIdentifier: "mycell")
        cell.textLabel?.text  = self.practicantes[indexPath.row]
            
        cell.imageView!.image = UIImage(systemName:"person")
      return cell
    }
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        let delete = UIContextualAction(style: .destructive, title: "Eliminar") { (action, sourceView, completionHandler) in
            self.practicantes.remove(at: indexPath.row)
            self.tableView.reloadData()
        }
        let swipeActionConfig = UISwipeActionsConfiguration(actions: [/*rename,*/ delete])
        swipeActionConfig.performsFirstActionWithFullSwipe = false
        return swipeActionConfig
    }
}
