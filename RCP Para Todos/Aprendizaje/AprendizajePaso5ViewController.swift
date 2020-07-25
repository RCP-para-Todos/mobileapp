import Foundation
import UIKit

class AprendizajePaso5ViewController: UIViewController
{

    @IBOutlet weak var continuarButton: UIButton!
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.continuarButton.backgroundColor = Constants.COLOR_BOTON_ACTIVADO
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 5"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    @IBAction func continuarClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "paso6", sender: nil)
    }
    
}
