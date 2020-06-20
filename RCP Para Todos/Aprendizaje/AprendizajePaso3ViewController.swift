import Foundation
import UIKit

class AprendizajePaso3ViewController: UIViewController
{

    @IBOutlet weak var primerCheckBox: UIButton!
    @IBOutlet weak var segundoCheckBox: UIButton!
    @IBOutlet weak var tercerCheckBox: UIButton!
    @IBOutlet weak var cuartoCheckBox: UIButton!
    @IBOutlet weak var continuarButton: UIButton!
    
    var primeroEstado : Bool = false
    var segundoEstado : Bool = false
    var terceroEstado : Bool = false
    var cuartoEstado : Bool = false
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 3"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @IBAction func primerCheckBoxClicked(_ sender: Any) {
        if(self.primeroEstado){
            primerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.primeroEstado = false
        }
        else{
            primerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.primeroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
            self.continuarButton.isEnabled = false
        }
    }
    @IBAction func segundoCheckBoxClicked(_ sender: Any) {
        if(self.segundoEstado){
            segundoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.segundoEstado = false
        }
        else{
            segundoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.segundoEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
            self.continuarButton.isEnabled = false
        }
    }
    @IBAction func tercerCheckBoxClicked(_ sender: Any) {
        if(self.terceroEstado){
            tercerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.terceroEstado = false
        }
        else{
            tercerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.terceroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
            self.continuarButton.isEnabled = false
        }
    }
    @IBAction func cuartoCheckBoxClicked(_ sender: Any) {
        if(self.cuartoEstado){
            cuartoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.cuartoEstado = false
        }
        else{
            cuartoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.cuartoEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado && self.cuartoEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
            self.continuarButton.isEnabled = false
        }
    }
    @IBAction func continuarClicked(_ sender: Any) {
        performSegue(withIdentifier: "paso4", sender: nil)
    }

}
