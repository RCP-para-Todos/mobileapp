//
//  VerCursosViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 18/07/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit

class VerActividadViewController: UIViewController{
    
    @IBOutlet weak var labelTiempoTotalManiobra: UILabel!
    @IBOutlet weak var labelPorcentajeSobrevida: UILabel!
    @IBOutlet weak var labelTiempoInactividad: UILabel!
    @IBOutlet weak var labelPorcentajeInsuflaciones: UILabel!
    @IBOutlet weak var labelPorcentajeCompresiones: UILabel!
    @IBOutlet weak var labelCabezaPosicion: UILabel!
    @IBOutlet weak var labelFuerzaPromedioAplicada: UILabel!
    @IBOutlet weak var labelCalidadInsuflaciones: UILabel!
    @IBOutlet weak var buttonObservacionesTutor: UIButton!
    @IBOutlet weak var imageEyeButton: UIImageView!
    
    var serviceEvento : ServiceEvento?
    var titleView : String?
    var idEvento : String?
    var evento: Evento?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.serviceEvento = ServiceEvento()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.serviceEvento!.getEventoById(id: self.idEvento!, completion: self.getEventoById)
        self.inicializarBarraSuperior()
        self.initInterface()
    }
    
    func initInterface(){
        self.buttonObservacionesTutor.layer.cornerRadius = 15
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
    
    public func getEventoById(evento: Evento){
        self.evento = evento
        self.labelTiempoTotalManiobra.text = String(evento.duracion)
        self.labelTiempoInactividad.text = String(evento.tiempoInactividad!)
        self.labelPorcentajeCompresiones.text = String(evento.porcentajeCompresion!)+"%"
        self.labelFuerzaPromedioAplicada.text = String(evento.fuerzaPromedioAplicada!)
        self.labelPorcentajeSobrevida.text = String(evento.porcentajeSobrevida!)+"%"
        self.labelPorcentajeInsuflaciones.text = String(evento.porcentajeInsuflacion!)+"%"
        self.labelCabezaPosicion.text = String(evento.cabezaPosicion!)
        self.labelCalidadInsuflaciones.text = String(evento.calidadInsuflaciones!)
    }
    
    @IBAction func buttonObservacionesClicked(_ sender: Any) {
        performSegue(withIdentifier: "verObservacionesSegue", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "verObservacionesSegue" {
            if let destinationVC = segue.destination as? VerObservacionesViewController {
                destinationVC.evento = self.evento
            }
        }
    }
    
}
