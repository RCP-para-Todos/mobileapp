import Foundation
import UIKit

class EstadisticasViewController: UIViewController
{
    @IBOutlet weak var compresionesNulasLabel: UILabel!
    @IBOutlet weak var compresionesInsuficientesLabel: UILabel!
    @IBOutlet weak var compresionesCorrectasLabel: UILabel!
    @IBOutlet weak var compresionesExcesivasLabel: UILabel!
    var compresionesNulasString : String = "0"
    var compresionesInsuficientesString : String = "0"
    var compresionesCorrectasString : String = "0"
    var compresionesExcesivasString : String = "0"
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.compresionesNulasLabel.text = compresionesNulasString
        self.compresionesInsuficientesLabel.text = compresionesInsuficientesString
        self.compresionesCorrectasLabel.text = compresionesCorrectasString
        self.compresionesExcesivasLabel.text = compresionesExcesivasString
    }

}
