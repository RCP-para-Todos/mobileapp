import Foundation
import UIKit
import CoreBluetooth // Para el bluetooth.


//Estos valores estan definidos en el ESP32.
let kBLEService_UUID = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E"
let kBLE_Characteristic_uuid_Rx = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
let kBLE_Characteristic_uuid_Tx = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"

let BLEService_UUID = CBUUID(string: kBLEService_UUID)
let BLE_Characteristic_uuid_Tx = CBUUID(string: kBLE_Characteristic_uuid_Tx)//(Property = Write without response)
let BLE_Characteristic_uuid_Rx = CBUUID(string: kBLE_Characteristic_uuid_Rx)// (Property = Read/Notify)
public protocol BLEDelegate
{
    func bleDidUpdateState()
    func bleDidConnectToPeripheral()
    func bleDidDisconenctFromPeripheral()
    func bleDidReceiveData(data: Data?)
}

class InicioViewController: UIViewController
{

    override func viewDidLoad()
    {
        super.viewDidLoad()
        
    }
    @IBAction func logoutClicked(_ sender: Any) {
        let alert = UIAlertController(title: "Sesión", message: "Desea cerrar sesión?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Cerrar sesión", style: UIAlertAction.Style.destructive, handler: { action in
            self.navigationController?.popViewController(animated: true)

        }))
        alert.addAction(UIAlertAction(title: "Cancelar", style: UIAlertAction.Style.default, handler: { action in
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
}
