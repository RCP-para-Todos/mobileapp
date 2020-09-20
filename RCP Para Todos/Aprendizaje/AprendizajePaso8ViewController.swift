import Foundation
import UIKit
import AVFoundation
import UICircularProgressRing
import CoreBluetooth

class AprendizajePaso8ViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate
{

    @IBOutlet weak var imageHeart: UIImageView!
    @IBOutlet weak var imageWind: UIImageView!
    @IBOutlet weak var progressBar: UICircularTimerRing!
    
    var player: AVAudioPlayer?
    
    public var delegate: BLEDelegate?
       
    public var centralManager : CBCentralManager!
    public var esp32 : CBPeripheral!
    public var characteristics = [String : CBCharacteristic]()

    public static var esp32Shared : CBPeripheral!
    public static var characteristicsShared = [String : CBCharacteristic]()

    var characteristicASCIIValue = NSString()
    
    var serviceEvento : ServiceEvento?
    var instantes : [Instante] = []
    var mediosSegundos : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.serviceEvento = ServiceEvento()
        self.inicializarBarraSuperior()
        self.playSound()
        self.instantes = [Instante]()
        //Despues de reproducir el audio, duracion del audio 8seg.
        DispatchQueue.main.asyncAfter(deadline: .now() + 5.0, execute: {
            self.centralManager = CBCentralManager(delegate: self, queue: nil)
            self.loadProgressCircleBar()
        })
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Paso 7"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func loadProgressCircleBar(){
        self.progressBar.startTimer(to: Constants.APRENDIZAJE_DURACION_SEGUNDOS_FINAL) { state in
            switch state {
            case .finished:
                print("finished")
                self.logicaEvaluarAprendizaje()
            case .continued(let time):
                print("continued: \(String(describing: time))")
            case .paused(let time):
                print("paused: \(String(describing: time))")
            }
        }
    }
    
    func playSound() {
        guard let url = Bundle.main.url(forResource: "AprendizajeAudio", withExtension: "mp3") else { return }

        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
            try AVAudioSession.sharedInstance().setActive(true)

            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)
            guard let player = player else { return }

            player.play()

        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func tratamientoRecepcionBluetooth(datosCorrectos: [String]){
        print("ReciboBluetoothPaso8")
        let insuflacion : String = Conversor.insuflacionToString(n: Int(datosCorrectos[0])!)
        let compresion : String = Conversor.compresionToString(n: Int(datosCorrectos[1])!)
        let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
        let posicionCabeza: String = Conversor.posicionCabezaToString(n: Int(datosCorrectos[3])!)
        
        let instante : Instante = Instante(insuflacion: insuflacion, compresion: compresion, posicion: posicion, posicionCabeza: posicionCabeza)
        
        //Agregado de instante al vector.
        self.instantes.append(instante)
        
        //Manejo de variables.
        self.mediosSegundos += 1;
        
        //Manejo de graficos.
        self.manejarGrafico(instante: instante)
    }
    
    func manejarGrafico(instante: Instante){
        self.imageHeart.isHidden = false
        self.imageWind.isHidden = false
        self.imageHeart.image = Constants.IMAGE_COMPRESION
        self.imageWind.image = Constants.IMAGE_INSUFLACION
        if(instante.Compresion == "Nula"){
            self.imageHeart.tintColor = .white
        }
        else if(instante.Compresion == "Insuficiente"){
            self.imageHeart.tintColor = .lightGray
        }
        else if(instante.Compresion == "Correcta"){
            self.imageHeart.tintColor = .green
        }
        else if(instante.Compresion == "Excesiva"){
            self.imageHeart.tintColor = .red
        }
        else{
            self.imageHeart.tintColor = .white
        }
        if(instante.Insuflacion == "Nula"){
            self.imageWind.tintColor = .white
        }
        else if(instante.Insuflacion == "Insuficiente"){
            self.imageWind.tintColor = .lightGray
        }
        else if(instante.Insuflacion == "Correcta"){
            self.imageWind.tintColor = .green
        }
        else if(instante.Insuflacion == "Excesiva"){
            self.imageWind.tintColor = .red
        }
        else{
            self.imageWind.tintColor = .white
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1, execute: {
            self.imageHeart.isHidden = true
            self.imageWind.isHidden = true
        })
    }
    
    func logicaEvaluarAprendizaje(){
        self.centralManager = nil
        self.subirEvento()
        self.performSegue(withIdentifier: "pasoEstadisticas", sender: nil)
    }
    
    func subirEvento(){
        
        //DATOS DEL EVENTO
        let usuarioActivo = UserDefaults.standard.string(forKey: "usuarioActivo")
        let curso = UserDefaults.standard.string(forKey: "curso")
        let tiempoTotalManiobra = Instante.tiempoTotalManiobra(instantes: self.instantes)
        let tipo = "aprendizaje"
        let event_date = Utils.hoy()
        let instantes : [[String : Any]] = Instante.toJson(instantes: self.instantes)
        let tiempoTotalInactividad = Instante.tiempoTotalInactividad(instantes: self.instantes)
        let porcentajeSobrevida = Instante.porcentajeTotalSobreVida(instantes: self.instantes)
        let porcentajeInsuflacionesCorrectas = Instante.porcentajeInsuflacionesCorrectas(instantes: self.instantes)
        let porcentajeCompresionesCorrectas = Instante.porcentajeCompresionesCorrectas(instantes: self.instantes)
        let cantidadInsuflacionesCorrectasMalaPosicion = Instante.cantidadInsuflacionesIncorrectasPosicionCabeza(instantes: self.instantes)
        let fuerzaPromedioAplicada = Instante.fuerzaPromedioAplicada(instantes: self.instantes)
        let calidadInsuflaciones = Instante.calidadInsuflaciones(instantes: self.instantes)
        
        let parameters : [String: Any] = [
            "user" : usuarioActivo!,
            "course" : curso!,
            "duration" : tiempoTotalManiobra,
            "type" : tipo,
            "event_date": event_date,
            "instants": instantes,
            "tiempoInactividad": tiempoTotalInactividad,
            "porcentajeSobrevida": porcentajeSobrevida,
            "porcentajeInsuflacionOk": porcentajeInsuflacionesCorrectas,
            "porcentajeCompresionOk": porcentajeCompresionesCorrectas,
            "cantidadInsuflacionesOkMalCabeza": cantidadInsuflacionesCorrectasMalaPosicion,
            "fuerzaPromedioAplicada": fuerzaPromedioAplicada,
            "calidadInsuflaciones":calidadInsuflaciones
            ]
        self.serviceEvento?.newEvento(parameters: parameters, completion: self.newEvento)
    }
    
    
    
    func newEvento(completion: Bool){
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "pasoEstadisticas" {
            if let destinationVC = segue.destination as? AprendizajeEstadisticasViewController {
                destinationVC.instantes = self.instantes
            }
        }
    }
    
    // MARK: DELEGADOS BLUETOOTH
    
    //Esta funcion es invocada cuando el dispositivo es conectado, es decir pasa a estado 2.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado Paso7")
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
        AprendizajePaso8ViewController.characteristicsShared = self.characteristics
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
            self.tratamientoRecepcionBluetooth(datosCorrectos: datosCorrectos)
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
            print("Bluetooth activado Paso 8")
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
            
            AprendizajePaso8ViewController.esp32Shared = self.esp32
        }
    }

}
