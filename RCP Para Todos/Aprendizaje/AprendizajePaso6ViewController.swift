import Foundation
import UIKit
import AVFoundation
import UICircularProgressRing
import CoreBluetooth // Para el bluetooth.

class AprendizajePaso6ViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate
{

    @IBOutlet weak var imageGif: UIImageView!
    @IBOutlet weak var progressBar: UICircularTimerRing!
    @IBOutlet weak var stackView: UIStackView!
    
    var player: AVAudioPlayer?
    
    public var delegate: BLEDelegate?
       
    public var centralManager : CBCentralManager!
    public var esp32 : CBPeripheral!
    public var characteristics = [String : CBCharacteristic]() // Es una variable tipo diccionario.

    //Variables estaticas que son compartidas con la vista Secundaria (De configuracion)
    public static var esp32Shared : CBPeripheral!
    public static var characteristicsShared = [String : CBCharacteristic]()

    var characteristicASCIIValue = NSString()
    
    var contadorX : Int = 0
    var instantes : [Instante] = []
    //Los datos se reciben cada 0.5 segundos, pero se necesita actuar sobre ellos cada 1 segundo.
    var mediosSegundos : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.centralManager = CBCentralManager(delegate: self, queue: nil)
        self.inicializarBarraSuperior()
        self.loadGif()
        self.playSound()
        self.loadProgressCircleBar()
        self.instantes = [Instante]()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 6"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func loadGif(){
        imageGif.loadGif(name: "CompresionManiobra")
    }
    
    func loadProgressCircleBar(){
        self.progressBar.startTimer(to: 30) { state in
            switch state {
            case .finished:
                print("finished")
                let alert = UIAlertController(title: "Compresiones Finalizadas", message: "Aprendizaje de Compresiones Finalizada", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            case .continued(let time):
                print("continued: \(String(describing: time))")
            case .paused(let time):
                print("paused: \(String(describing: time))")
            }
        }
    }
    
    func playSound() {
        guard let url = Bundle.main.url(forResource: "CompresionManiobraAudio", withExtension: "mp3") else { return }

        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
            try AVAudioSession.sharedInstance().setActive(true)

            /* The following line is required for the player to work on iOS 11. Change the file type accordingly*/
            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)

            /* iOS 10 and earlier require the following line:
            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileTypeMPEGLayer3) */

            guard let player = player else { return }

            player.play()

        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func manejarGraficoAccion(){
        if(mediosSegundos % 2 == 0){
            let instantesIndice : Int = self.instantes.count - 1
            let i1 : Instante = self.instantes[instantesIndice-1]
            let i2 : Instante = self.instantes[instantesIndice]
            self.tratamientoInstantes(i1: i1, i2: i2)
        }
    }
    
    func tratamientoInstantes(i1: Instante, i2: Instante){
        //Imagenes a utilizar.
        let compresionNula = UIImage(systemName: "clear")!
        let compresionInsuficiente = UIImage(systemName: "arrow.down")!
        let compresionCorrecta = UIImage(systemName: "bookmark")!
        let compresionExcesiva = UIImage(systemName: "arrow.up")!
        
        var imageView1 : UIImageView
        
        //Evaluacion de compresion de instante 1.
        if(i1.Compresion == "Nula"){
            imageView1 = UIImageView(image: compresionNula)
        }
        else if(i1.Compresion == "Insuficiente"){
            imageView1 = UIImageView(image: compresionInsuficiente)
        }
        else if(i1.Compresion == "Correcta"){
            imageView1 = UIImageView(image: compresionCorrecta)
        }
        else if(i1.Compresion == "Excesiva"){
            imageView1 = UIImageView(image: compresionExcesiva)
        }
        else{
            imageView1 = UIImageView(image: compresionNula)
        }
        
        //Posicionamientos
        imageView1.frame = CGRect(x: contadorX, y: 0, width: 10, height: 10)
        
        self.stackView.addSubview(imageView1)
    }
    
    //Esta funcion es invocada cuando el dispositivo es conectado, es decir pasa a estado 2.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado Paso6")
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
        AprendizajePaso6ViewController.characteristicsShared = self.characteristics
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
            //print(recibido)
            let cadenaBytetoString = String(bytes: recibido, encoding: .utf8)
            let datosCorrectos = cadenaBytetoString!.components(separatedBy: ";")
            
            print("ReciboBluetoothPaso6")
            let insuflacion : String = Conversor.insuflacionToString(n: Int(datosCorrectos[0])!)
            let compresion : String = Conversor.compresionToString(n: Int(datosCorrectos[1])!)
            let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
            
            let instante : Instante = Instante(insuflacion: insuflacion, compresion: compresion, posicion: posicion)
            
            //Si llegue a 60 segundos.
            /*if(mediosSegundos == 60){
                let alert = UIAlertController(title: "Compresiones Finalizadas", message: "Aprendizaje de Compresiones Finalizada", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            }*/
            
            //Agregado de instante al vector.
            self.instantes.append(instante)
            
            //Manejo de variables.
            
            self.mediosSegundos += 1;
            self.contadorX+=5;
            
            //Manejo de graficos.
            
            if(self.mediosSegundos < 60){
                self.manejarGraficoAccion()
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
            print("Bluetooth activado Paso 6")
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
            
            AprendizajePaso6ViewController.esp32Shared = self.esp32
        }
    }

}
