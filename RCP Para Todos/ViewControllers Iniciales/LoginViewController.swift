import Foundation
import UIKit

class LoginViewController: UIViewController
{
    
    @IBOutlet weak var buttonIngresar: UIButton!
    @IBOutlet weak var buttonCrearUsuario: UIButton!
    @IBOutlet weak var buttonCheckRecordarContrasena: UIButton!
    @IBOutlet weak var usuarioInput: UITextField!
    @IBOutlet weak var passwordInput: UITextField!
    @IBOutlet weak var activityIndicatorSpinner: UIActivityIndicatorView!
    
    var service : ServiceUser?
    var stateCheckBox : Bool = true
    override func viewDidLoad()
    {
        self.service = ServiceUser()
        self.autoLogin()
        self.resetDefaults()
        super.viewDidLoad()
        self.initInterface()
        self.hideKeyboardOnTap(#selector(self.dismissKeyboard))
        self.activityIndicatorSpinner.stopAnimating()
    }
    
    func initInterface(){
        self.buttonIngresar.layer.cornerRadius = 15
        self.buttonCrearUsuario.layer.cornerRadius = 15
        self.buttonCheckRecordarContrasena.layer.borderWidth = 1
        self.buttonCheckRecordarContrasena.layer.borderColor = UIColor.lightGray.cgColor
    }
    
    func autoLogin(){
        let usuario = service!.obtenerCredencialesProximoInicio().0
        let contrasena = service!.obtenerCredencialesProximoInicio().1
        if(usuario != nil && contrasena != nil){
            let parameters: [String: String] = [
                "name" : usuario!,
                "password" : contrasena!
            ]
            self.activityIndicatorSpinner.startAnimating()
            self.service?.login(parameters: parameters, completion: self.completion)
        }
    }
    
    @objc func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
    @IBAction func buttonCheckRecordarContrasenaClicked(_ sender: Any) {
        if(!self.stateCheckBox){
            self.buttonCheckRecordarContrasena.setImage(UIImage(systemName: "checkmark"), for: .normal)
            self.stateCheckBox = true
        }
        else{
            self.buttonCheckRecordarContrasena.setImage(nil, for: .normal)
            self.stateCheckBox = false
        }
    }
    

    @IBAction func ingresarClicked(_ sender: Any) {
        self.activityIndicatorSpinner.startAnimating()
        let parameters: [String: String] = [
            "name" : usuarioInput.text!,
            "password" : passwordInput.text!
        ]
        if(self.stateCheckBox){
            service?.guardarCreedencialesProximoinicio(usuario: usuarioInput.text!, contrasena: passwordInput.text!)
        }
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
