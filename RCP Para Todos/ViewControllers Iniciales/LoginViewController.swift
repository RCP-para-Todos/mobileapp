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
        //ESTO ES USADO PARA TESTEAR
        /*let defaults = UserDefaults.standard
        let token = defaults.string(forKey: "token")!
        self.service?.getUserByToken(token: token)*/
    }
    
    func completion(result: Bool){
        self.activityIndicatorSpinner.stopAnimating()
        if(result){
            performSegue(withIdentifier: "InicioSegue", sender: nil)
            //performSegue(withIdentifier: "InicioTutorSegue", sender: nil)
        }
        else{
            let alert = UIAlertController(title: "Creedenciales incorrectas", message: "Usuario o contraseña no válidos", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
}
