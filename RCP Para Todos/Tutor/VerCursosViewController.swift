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
    
    //ESTO VIENE DEL BACK
    let cursos = ["E.T. 37 TM", "UNLM TN", "UBA TT", "Dpto. Ci.", "R.R."]
    var cursoSeleccionado : String?
    
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
        self.title = "Ver cursos"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
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
        performSegue(withIdentifier: "VerPracticantesSegue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "VerPracticantesSegue" {
            if let destinationVC = segue.destination as? VerPracticantesViewController {
                destinationVC.titleView = self.cursoSeleccionado
            }
        }
    }
}
