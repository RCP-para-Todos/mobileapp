import Foundation
import UIKit
import CoreBluetooth // Para el bluetooth.

class AprendizajePaso2ViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate
{
    
    @IBOutlet weak var primerCheckBox: UIButton!
    @IBOutlet weak var segundoCheckBox: UIButton!
    @IBOutlet weak var tercerCheckBox: UIButton!
    @IBOutlet weak var alternativoButton: UIButton!
    @IBOutlet weak var continuarButton: UIButton!
    
    public var delegate: BLEDelegate?
       
    public var centralManager : CBCentralManager!
    public var esp32 : CBPeripheral!
    public var characteristics = [String : CBCharacteristic]() // Es una variable tipo diccionario.

    //Variables estaticas que son compartidas con la vista Secundaria (De configuracion)
    public static var esp32Shared : CBPeripheral!
    public static var characteristicsShared = [String : CBCharacteristic]()

    var characteristicASCIIValue = NSString()
    
    var primeroEstado : Bool = false
    var segundoEstado : Bool = false
    var terceroEstado : Bool = false


    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
        self.centralManager = CBCentralManager(delegate: self, queue: nil)
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 2"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @IBAction func primerCheckBoxClicked(_ sender: Any) {
        if(self.primeroEstado){
            primerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.primeroEstado = false
        }
        else{
            primerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.primeroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
            self.continuarButton.isEnabled = false
        }
    }

    @IBAction func tercerCheckBoxClicked(_ sender: Any) {
        if(self.terceroEstado){
            tercerCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
            self.terceroEstado = false
        }
        else{
            tercerCheckBox.setImage(UIImage(named: "checked"), for: .normal)
            self.terceroEstado = true
        }
        if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
            self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
            self.continuarButton.isEnabled = true
        }
        else{
            self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
            self.continuarButton.isEnabled = false
        }
    }
    @IBAction func continuarClicked(_ sender: Any) {
        performSegue(withIdentifier: "paso3", sender: nil)
        self.centralManager = nil
    }
    
    //Esta funcion es invocada cuando el dispositivo es conectado, es decir pasa a estado 2.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado")
        peripheral.discoverServices(nil)
    }
    
    //Esta funcion es llamada cuando se intenta hacer un "discoverServices" del dispositivo.
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        
        if error != nil
        {
            print("[ERROR] Error descubriendo servicios. \(error!)")
            return
        }
        
        print("[DEBUG] Servicios encontrados para el dispositivo: \(peripheral.identifier.uuidString)")
        
        for service in peripheral.services!
        {
            let theCharacteristics = [BLE_Characteristic_uuid_Rx, BLE_Characteristic_uuid_Tx]
            peripheral.discoverCharacteristics(theCharacteristics, for: service)
        }
    }
    
    //Funcion que es invocada cuando se "discoverCharacteristics" del dispositivo.
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        
        if error != nil
        {
            print("[ERROR] Error descubriendo caracteristicas. \(error!)")
            return
        }
        
        print("[DEBUG] Caracteristicas descubiertas para el dispositivo: \(peripheral.identifier.uuidString)")
        
        for characteristic in service.characteristics!
        {
            self.characteristics[characteristic.uuid.uuidString] = characteristic
        }
        
        //Cuando descubro las caracteristicas del dispositivo a la vez activo las notificaciones. Es decir lo que me manda el ESP32.
        enableNotifications(enable: true)
        AprendizajePaso2ViewController.characteristicsShared = self.characteristics
        //SACAR DE ACA
        let mensaje = "TEXTO"
        let data: Data = mensaje.data(using: String.Encoding.utf8)!
        self.esp32.writeValue(data, for: Array(characteristics)[0].value, type: CBCharacteristicWriteType.withResponse)
    }
    
    //Funcion que determina como se van a realizar la lectura de datos provenientes del ESP32.
    func read()
    {
        guard let lectura = self.characteristics[kBLE_Characteristic_uuid_Tx] else { return }
        self.esp32?.readValue(for: lectura)
    }
    
    //Esta funcion es invocada cada vez que el ESP32 envia "algo"
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?)
    {
        if error != nil
        {
            print("[ERROR] Error actualizando valor. \(error!)")
            return
        }
        
        //Si recibo algo proveniente del ESP32, porque podria estar conectado a mas cosas, y recibir de distintos.
        if characteristic.uuid.uuidString == kBLE_Characteristic_uuid_Tx
        {
            self.delegate?.bleDidReceiveData(data: characteristic.value)
            let recibido = [UInt8](characteristic.value!)
            let cadenaBytetoString = String(bytes: recibido, encoding: .utf8)
            let datosCorrectos = cadenaBytetoString!.components(separatedBy: ";")
            let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
            if(posicion == "Reclinado"){
                segundoCheckBox.setImage(UIImage(named: "checked"), for: .normal)
                self.segundoEstado = true
            }
            else{
                segundoCheckBox.setImage(UIImage(named: "unchecked"), for: .normal)
                self.segundoEstado = false
            }
            if(self.primeroEstado && self.segundoEstado && self.terceroEstado){
                self.continuarButton.backgroundColor = UIColor(red: 0.00, green: 0.70, blue: 0.01, alpha: 1.00)
                self.continuarButton.isEnabled = true
            }
            else{
                self.continuarButton.backgroundColor = UIColor(red: 0.39, green: 0.39, blue: 0.39, alpha: 1.00)
                self.continuarButton.isEnabled = false
            }
        }
    }
    
    //Esta funcion activa las "notificaciones" es decir la recepcion de los datos provenientes del ESP32.
    public func enableNotifications(enable: Bool)
    {
        guard let char = self.characteristics[kBLE_Characteristic_uuid_Tx] else { return }
        self.esp32?.setNotifyValue(enable, for: char)
    }
    
    //Esta funcion que es invocada cuando se produce algun cambio de estado en el CBCentralManager
    func centralManagerDidUpdateState(_ central: CBCentralManager)
    {
        if central.state == .poweredOn
        {
            print("Bluetooth activado")
            //Escaneo los dispositivos.
            self.centralManager.scanForPeripherals(withServices: nil, options: nil)
        }
        else
        {
            print("Bluetooth desactivado")
        }
    }
    
    //Esta funcion es invocada cuando se escanean los dispositivos.
    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber)
    {
        //Si encontre el ESP32.
        if(peripheral.name == "ESP32")
        {
            //Print de debug.
            print("\nNombre : \(peripheral.name ?? "(No name)")")
            print("Señal(RSSI) : \(RSSI)")
            for ad in advertisementData
            {
                print("Data : \(ad)")
            }
            //Mantengo una referencia FUERTE al dispositivo.
            self.esp32 = peripheral
            //Detengo la busqueda.
            self.centralManager.stopScan()
            //Seteo el delegado.
            self.esp32.delegate = self
            //Empiezo la conexion, debe estar tambien el centralManager.connect de arriba.
            self.centralManager.connect(esp32, options: nil)
            
            AprendizajePaso2ViewController.esp32Shared = self.esp32
        }
    }

}
