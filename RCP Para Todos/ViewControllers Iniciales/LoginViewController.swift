import Foundation
import UIKit

class LoginViewController: UIViewController
{
    
    @IBOutlet weak var usuarioInput: UITextField!
    @IBOutlet weak var passwordInput: UITextField!
    @IBOutlet weak var activityIndicatorSpinner: UIActivityIndicatorView!
    
    var service : ServiceUser?
    override func viewDidLoad()
    {
        self.resetDefaults()
        super.viewDidLoad()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.service = ServiceUser()
        self.activityIndicatorSpinner.stopAnimating()
    }
    
    @objc func dismissKeyboard() {
        self.view.endEditing(true)
    }

    @IBAction func ingresarClicked(_ sender: Any) {
        self.activityIndicatorSpinner.startAnimating()
        let parameters: [String: String] = [
            "name" : usuarioInput.text!,
            "password" : passwordInput.text!
        ]
        self.service?.login(parameters: parameters, completion: self.completion)
    }
    
    func completion(result: Bool){
        self.activityIndicatorSpinner.stopAnimating()
        if(result){
            let rol = UserDefaults.standard.string(forKey: "rol")
            
            if(rol == "instructor"){
                self.performSegue(withIdentifier: "InicioTutorSegue", sender: nil)
            }
            else{
                if let curso = UserDefaults.standard.string(forKey: "curso"){
                    self.performSegue(withIdentifier: "InicioSegue", sender: nil)
                }
                else{
                    let alert = UIAlertController(title: "Usuario sin curso asignado", message: "El usuario practicante existe pero no posee un curso asignado", preferredStyle: UIAlertController.Style.alert)
                    alert.addAction(UIAlertAction(title: "Aceptar", style: UIAlertAction.Style.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                   
                }
            }
        }
        else{
            let alert = UIAlertController(title: "Creedenciales incorrectas", message: "Usuario o contraseña no válidos", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Aceptar", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    func resetDefaults() {
        let defaults = UserDefaults.standard
        let dictionary = defaults.dictionaryRepresentation()
        dictionary.keys.forEach { key in
            defaults.removeObject(forKey: key)
        }
    }
    
}
