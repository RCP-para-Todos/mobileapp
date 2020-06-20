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
        self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
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
            primerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.primeroEstado = false
        }
        else{
            primerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.primeroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
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
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
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
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
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
        performSegue(withIdentifier: "paso5", sender: nil)
    }


}
