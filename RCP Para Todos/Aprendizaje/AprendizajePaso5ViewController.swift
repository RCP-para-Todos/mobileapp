import Foundation
import UIKit

class AprendizajePaso5ViewController: UIViewController
{

    @IBOutlet weak var continuarButton: UIButton!
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 5"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }

}
