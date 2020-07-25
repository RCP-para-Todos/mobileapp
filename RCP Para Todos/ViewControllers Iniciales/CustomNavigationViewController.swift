import Foundation
import UIKit

class CustomNavigationViewController: UINavigationController
{

    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.barraSuperior()
    }
    
    func barraSuperior()
    {
        let backButton = UIBarButtonItem()
        backButton.isEnabled = false
        backButton.title = nil;
        self.navigationBar.topItem?.backBarButtonItem = backButton
    }

}
