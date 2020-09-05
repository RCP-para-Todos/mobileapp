import Foundation
import UIKit

class AprendizajePaso1ViewController: UIViewController
{
    @IBOutlet weak var primerCheckBox: UIButton!
    @IBOutlet weak var segundoCheckBox: UIButton!
    @IBOutlet weak var tercerCheckBox: UIButton!
    @IBOutlet weak var cuartoCheckBox: UIButton!
    @IBOutlet weak var entornoSeguroButton: UIButton!
    
    var primeroEstado : Bool = false
    var segundoEstado : Bool = false
    var terceroEstado : Bool = false
    var cuartoEstado : Bool = false
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
        self.initInterface()
    }
        
    func initInterface(){
        self.entornoSeguroButton.layer.cornerRadius = 15
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 1"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    @IBAction func primerCheckBoxClicked(_ sender: Any) {
        if(self.primeroEstado){
            self.primerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.primeroEstado = false
        }
        else{
            self.primerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.primeroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.entornoSeguroButton.isEnabled = true
        }
        else{
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.entornoSeguroButton.isEnabled = false
        }
    }
    @IBAction func segundoCheckBoxClicked(_ sender: Any) {
        if(self.segundoEstado){
            self.segundoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.segundoEstado = false
        }
        else{
            self.segundoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.segundoEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.entornoSeguroButton.isEnabled = true
        }
        else{
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.entornoSeguroButton.isEnabled = false
        }
    }
    @IBAction func tercerCheckBoxClicked(_ sender: Any) {
        if(self.terceroEstado){
            self.tercerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.terceroEstado = false
        }
        else{
            self.tercerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.terceroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.entornoSeguroButton.isEnabled = true
        }
        else{
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.entornoSeguroButton.isEnabled = false
        }
    }
    @IBAction func cuartoCheckBoxClicked(_ sender: Any) {
        if(self.cuartoEstado){
            self.cuartoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.cuartoEstado = false
        }
        else{
            self.cuartoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.cuartoEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.entornoSeguroButton.isEnabled = true
        }
        else{
            self.entornoSeguroButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.entornoSeguroButton.isEnabled = false
        }
    }
    @IBAction func entornoSeguroClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "paso2", sender: nil)
    }
    
    
}
