//
//  VerCursosViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 18/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class VerCursosViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate{
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicatorSpinner: UIActivityIndicatorView!
    var cursos : [String] = []
    var cursosIds : [String] = []
    var cursoSeleccionado : String?
    var cursoSeleccionadoId: String?
    var serviceCurso : ServiceCurso?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.activityIndicatorSpinner.startAnimating()
        self.serviceCurso = ServiceCurso()
        self.getCursosPorTutor()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.inicializarBarraSuperior()
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Ver cursos"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func getCursosPorTutor(){
        self.serviceCurso?.getCursosPorTutor(completion: self.llenarArray)
    }
    
    func llenarArray(arrayIds:[String], array:[String]){
        self.cursosIds = arrayIds
        self.cursos = array
        self.tableView.reloadData()
        self.activityIndicatorSpinner.stopAnimating()
    }
    
    func tableView(_ tableView:UITableView, numberOfRowsInSection section:Int) -> Int
    {
        return self.cursos.count
    }
     
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:UITableViewCell=UITableViewCell(style: UITableViewCell.CellStyle.subtitle, reuseIdentifier: "mycell")
        cell.textLabel?.text  = self.cursos[indexPath.row]
            
        cell.imageView!.image = UIImage(systemName:"person.3")
      return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.cursoSeleccionado = self.cursos[indexPath.row]
        self.cursoSeleccionadoId = self.cursosIds[indexPath.row]
        self.performSegue(withIdentifier: "VerPracticantesSegue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "VerPracticantesSegue" {
            if let destinationVC = segue.destination as? VerPracticantesViewController {
                destinationVC.titleView = self.cursoSeleccionado
                destinationVC.cursoId = self.cursoSeleccionadoId
            }
        }
    }
}
