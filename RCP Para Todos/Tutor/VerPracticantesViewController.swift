//
//  VerPracticantesViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 18/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class VerPracticantesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate{
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicationSpinner: UIActivityIndicatorView!
    //let practicantes = ["Juan", "Jorge", "Jose", "Tomas", "Matias", "Tiago"]
    var practicantes : [String] = []
    var practicantesIds : [String] = []
    var serviceCurso : ServiceCurso?
    var titleView : String?
    var cursoId : String?
    var practicanteSeleccionado : String?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.activityIndicationSpinner.startAnimating()
        self.serviceCurso = ServiceCurso()
        self.serviceCurso!.getCursoById(id: self.cursoId!, completion: self.getCursoById)
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.inicializarBarraSuperior()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = self.titleView
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func getCursoById(arrayIds: [String], arrayNames: [String]){
        self.practicantesIds = arrayIds
        self.practicantes = arrayNames
        self.tableView.reloadData()
        self.activityIndicationSpinner.stopAnimating()
    }
    
    func guardarPracticanteSeleccionado(practicanteSeleccionado: String){
        let defaults = UserDefaults.standard
        defaults.set(practicanteSeleccionado, forKey: "practicanteSeleccionado")
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
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.practicanteSeleccionado = self.practicantes[indexPath.row]
        self.guardarPracticanteSeleccionado(practicanteSeleccionado: self.practicanteSeleccionado ?? "")
        self.performSegue(withIdentifier: "VerActividadesSegue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "VerActividadesSegue" {
            if let destinationVC = segue.destination as? VerActividadesViewController {
                destinationVC.titleView = self.practicanteSeleccionado
            }
        }
    }
}
