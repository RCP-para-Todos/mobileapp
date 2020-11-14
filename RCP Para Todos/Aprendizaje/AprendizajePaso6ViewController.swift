import Foundation
import UIKit
import AVFoundation
import UICircularProgressRing
import CoreBluetooth

class AprendizajePaso6ViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate
{

    @IBOutlet weak var imageGif: UIImageView!
    @IBOutlet weak var progressBar: UICircularTimerRing!
    @IBOutlet weak var stackView: UIStackView!
    
    var player: AVAudioPlayer?
    
    public var delegate: BLEDelegate?
       
    public var centralManager : CBCentralManager!
    public var esp32 : CBPeripheral!
    public var characteristics = [String : CBCharacteristic]()

    public static var esp32Shared : CBPeripheral!
    public static var characteristicsShared = [String : CBCharacteristic]()

    var characteristicASCIIValue = NSString()
    
    var contadorX : Int = 0
    var instantes : [Instante] = []
    var mediosSegundos : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        self.inicializarBarraSuperior()
        self.loadGif()
        self.playSound()
        self.instantes = [Instante]()
        //Despues de reproducir el audio, duracion del audio 8seg.
        DispatchQueue.main.asyncAfter(deadline: .now() + 8.0, execute: {
            self.centralManager = CBCentralManager(delegate: self, queue: nil)
            self.loadProgressCircleBar()
        })
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 5"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func loadGif(){
        imageGif.loadGif(name: "CompresionManiobra")
    }
    
    func loadProgressCircleBar(){
        self.progressBar.startTimer(to: Constants.APRENDIZAJE_DURACION_SEGUNDOS_COMPRESION_INSUFLACION) { state in
            switch state {
            case .finished:
                self.logicaEvaluacionCompresiones()
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

            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)
            guard let player = player else { return }

            player.play()

        } catch let error {}
    }
    
    func manejarGraficoAccion(){
        if(mediosSegundos % 10 == 0){
            let instantesIndice : Int = self.instantes.count - 1
            let i1 : Instante = self.instantes[instantesIndice-1]
            let i2 : Instante = self.instantes[instantesIndice]
            self.tratamientoInstantes(i1: i1, i2: i2)
        }
    }
    
    func tratamientoInstantes(i1: Instante, i2: Instante){
        var imageView1 : UIImageView
        imageView1 = UIImageView(image: Constants.IMAGE_COMPRESION)
        
        //Evaluacion de compresion de instante 1.
        if(i1.Compresion == "Nula"){
            imageView1.tintColor = .white
        }
        else if(i1.Compresion == "Insuficiente"){
            imageView1.tintColor = .lightGray
        }
        else if(i1.Compresion == "Correcta"){
            imageView1.tintColor = .green
        }
        else if(i1.Compresion == "Excesiva"){
            imageView1.tintColor = .red
        }
        else{
            imageView1.tintColor = .white
        }
        
        //Posicionamientos
        imageView1.frame = CGRect(x: contadorX, y: 0, width: 56, height: 56)
        
        self.stackView.addSubview(imageView1)
    }
    
    func tratamientoRecepcionBluetooth(datosCorrectos: [String]){
        let insuflacion : String = Conversor.insuflacionToString(n: Int(datosCorrectos[0])!)
        let compresion : String = Conversor.compresionToString(n: Int(datosCorrectos[1])!)
        let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
        let posicionCabeza: String = Conversor.posicionCabezaToString(n: Int(datosCorrectos[3])!)
        
        let instante : Instante = Instante(insuflacion: insuflacion, compresion: compresion, posicion: posicion, posicionCabeza: posicionCabeza)
        
        //Llegado los 30 segundos.
        if(mediosSegundos == 60){
            //self.logicaEvaluacionCompresiones()
        }
        
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
    
    //Para pasar a la practica de insuflaciones la practica de compresiones debe estar aprobada.
    func logicaEvaluacionCompresiones(){
        self.centralManager = nil
        var compresionesCorrectas : Int = 0
        for i in self.instantes{
            if(i.Compresion == "Correcta"){
                compresionesCorrectas+=1
            }
        }
        //print("COMPRESIONES PORCENTAJE: \(Double(compresionesCorrectas) / Double(self.instantes.count))")
        if((Double(compresionesCorrectas) / Double(self.instantes.count)) > Constants.APRENDIZAJE_PORCENTAJE_COMPRESIONES_VALIDAS){
            let alert = UIAlertController(title: "Compresiones Finalizadas", message: "Aprendizaje de compresiones realizado exitosamente", preferredStyle: UIAlertController.Style.alert)
            let action = UIAlertAction(title: "Aceptar", style: UIAlertAction.Style.default) {
                UIAlertAction in
                self.performSegue(withIdentifier: "paso7", sender: nil)
            }
            alert.addAction(action)
            self.present(alert, animated: true, completion: nil)
        }
        else{
            let alert = UIAlertController(title: "Compresiones Finalizadas", message: "Aprendizaje de compresiones fallido, vuelva a intentar", preferredStyle: UIAlertController.Style.alert)
            let action = UIAlertAction(title: "Aceptar", style: UIAlertAction.Style.default) {
                UIAlertAction in
                self.navigationController?.popViewController(animated: true)
            }
            alert.addAction(action)
            self.present(alert, animated: true, completion: nil)
        }
        
    }
    
    // MARK: DELEGADOS BLUETOOTH
    
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado Paso6")
        peripheral.discoverServices(nil)
    }
    
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
        
        enableNotifications(enable: true)
        AprendizajePaso6ViewController.characteristicsShared = self.characteristics
        //SACAR DE ACA
        let mensaje = "TEXTO"
        let data: Data = mensaje.data(using: String.Encoding.utf8)!
        self.esp32.writeValue(data, for: Array(characteristics)[0].value, type: CBCharacteristicWriteType.withResponse)
    }
    
    func read()
    {
        guard let lectura = self.characteristics[kBLE_Characteristic_uuid_Tx] else { return }
        self.esp32?.readValue(for: lectura)
    }
    
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?)
    {
        if error != nil
        {
            print("[ERROR] Error actualizando valor. \(error!)")
            return
        }
        
        if characteristic.uuid.uuidString == kBLE_Characteristic_uuid_Tx
        {
            self.delegate?.bleDidReceiveData(data: characteristic.value)
            let recibido = [UInt8](characteristic.value!)
            let cadenaBytetoString = String(bytes: recibido, encoding: .utf8)
            let datosCorrectos = cadenaBytetoString!.components(separatedBy: ";")
            self.tratamientoRecepcionBluetooth(datosCorrectos: datosCorrectos)
        }
    }
    
    public func enableNotifications(enable: Bool)
    {
        guard let char = self.characteristics[kBLE_Characteristic_uuid_Tx] else { return }
        self.esp32?.setNotifyValue(enable, for: char)
    }
    
    func centralManagerDidUpdateState(_ central: CBCentralManager)
    {
        if central.state == .poweredOn
        {
            print("Bluetooth activado Paso 6")
            self.centralManager.scanForPeripherals(withServices: nil, options: nil)
        }
        else
        {
            print("Bluetooth desactivado")
        }
    }
    
    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber)
    {
        if(peripheral.name == "ESP32")
        {
            print("\nNombre : \(peripheral.name ?? "(No name)")")
            print("Señal(RSSI) : \(RSSI)")
            for ad in advertisementData
            {
                print("Data : \(ad)")
            }
            
            self.esp32 = peripheral
            self.centralManager.stopScan()
            self.esp32.delegate = self
            self.centralManager.connect(esp32, options: nil)
            
            AprendizajePaso6ViewController.esp32Shared = self.esp32
        }
    }

}
