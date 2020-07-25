import Foundation
import UIKit

class AprendizajePaso4ViewController: UIViewController
{

    @IBOutlet weak var primerCheckBox: UIButton!
    @IBOutlet weak var segundoCheckBox: UIButton!
    @IBOutlet weak var tercerCheckBox: UIButton!
    @IBOutlet weak var continuarButton: UIButton!
    
    var primeroEstado : Bool = false
    var segundoEstado : Bool = false
    var terceroEstado : Bool = false
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.continuarButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 4"
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
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
            self.continuarButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.continuarButton.isEnabled = false
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
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
            self.continuarButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.continuarButton.isEnabled = false
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
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
            self.continuarButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = Constants.COLOR_BOTON_DESACTIVADO
            self.continuarButton.isEnabled = false
        }
    }
    
    @IBAction func esternonClicked(_ sender: Any) {
        /*let imageView = UIImageView(frame: CGRect(x: 70, y: 5, width: 120, height: 120))
        let alertMessage = UIAlertController(title: "", message: "\n\n\n\n\n\n", preferredStyle: .alert)

        let image = UIImage(named: "esternon")
        let action = UIAlertAction(title: "OK", style: .default, handler: nil)
        imageView.image = image
        alertMessage .addAction(action)

        self.present(alertMessage, animated: true, completion: nil)
         alertMessage.view.addSubview(imageView)*/
    }
    
    
    @IBAction func continuarClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "paso6_4", sender: nil)
    }


}
