import Foundation
import UIKit

extension UIViewController {
    func hideKeyboardOnTap(_ selector: Selector) {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: selector)
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
}

class LoginViewController: UIViewController
{
    
    @IBOutlet weak var usuarioInput: UITextField!
    @IBOutlet weak var passwordInput: UITextField!
    @IBOutlet weak var activityIndicatorSpinner: UIActivityIndicatorView!
    
    var service : Services?
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.service = Services()
        activityIndicatorSpinner.stopAnimating()
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }

    @IBAction func ingresarClicked(_ sender: Any) {
        activityIndicatorSpinner.startAnimating()
        let parameters: [String: String] = [
            "name" : usuarioInput.text!,
            "password" : passwordInput.text!
        ]
        self.service?.login(parameters: parameters, completion: self.completion)
        //ESTO ES USADO PARA TESTEAR
        /*let defaults = UserDefaults.standard
        let token = defaults.string(forKey: "token")!
        self.service?.getUserByToken(token: token)*/
    }
    
    func completion(result: Bool){
        self.activityIndicatorSpinner.stopAnimating()
        if(result){
            performSegue(withIdentifier: "InicioTutorSegue", sender: nil)
        }
        else{
            let alert = UIAlertController(title: "Creedenciales incorrectas", message: "Usuario o contraseña no válidos", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
}
