//
//  VerActividadesViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 18/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class VerActividadesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate{
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicatorSpinner: UIActivityIndicatorView!
    
    var eventos : [String] = []
    var eventosIds : [String] = []
    //let eventos = ["Anteayer", "Ayer", "Hace 30m", "Hace 15m", "Recien"]
    var serviceEvento : ServiceEvento?
    var titleView : String?
    var practicanteId : String?
    var actividadSeleccionada : String?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.activityIndicatorSpinner.startAnimating()
        self.serviceEvento = ServiceEvento()
        self.serviceEvento!.getEventosPorPracticante(practicante: self.titleView!, completion: self.getEventosPorPracticante)
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
    
    func getEventosPorPracticante(arrayIds: [String], arrayNames: [String]){
        self.eventosIds = arrayIds
        self.eventos = arrayNames
        self.tableView.reloadData()
        self.activityIndicatorSpinner.stopAnimating()
    }
    
    func tableView(_ tableView:UITableView, numberOfRowsInSection section:Int) -> Int
    {
        return self.eventos.count
    }
     
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:UITableViewCell=UITableViewCell(style: UITableViewCell.CellStyle.subtitle, reuseIdentifier: "mycell")
        cell.textLabel?.text  = self.eventos[indexPath.row]
            
        cell.imageView!.image = UIImage(systemName:"heart.fill")
      return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.actividadSeleccionada = self.eventos[indexPath.row]
        performSegue(withIdentifier: "VerActividadSegue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "VerActividadSegue" {
            if let destinationVC = segue.destination as? VerActividadViewController {
                //destinationVC.titleView = self.practicanteSeleccionado
            }
        }
    }
}
