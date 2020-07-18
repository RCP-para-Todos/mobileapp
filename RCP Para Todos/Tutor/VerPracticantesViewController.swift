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
    
    //ESTO VIENE DEL BACK
    let practicantes = ["Juan", "Jorge", "Jose", "Tomas", "Matias", "Tiago"]
    var titleView : String?
    var practicanteSeleccionado : String?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.inicializarBarraSuperior()
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = self.titleView
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
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
        performSegue(withIdentifier: "VerActividadesSegue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "VerActividadesSegue" {
            if let destinationVC = segue.destination as? VerPracticantesViewController {
                destinationVC.titleView = self.practicanteSeleccionado
            }
        }
    }
}
