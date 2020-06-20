import Foundation
import UIKit

class AprendizajePaso6ViewController: UIViewController
{

    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 6"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }

}
