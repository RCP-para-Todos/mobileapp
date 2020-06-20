import Foundation
import UIKit

class AprendizajeEstadisticasViewController: UIViewController
{

    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Estadisticas"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }

}
