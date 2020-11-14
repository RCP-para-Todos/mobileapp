import UIKit
import CoreBluetooth // Para el bluetooth.

class JuegoViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate
{
    public var delegate: BLEDelegate?
    
    public var centralManager : CBCentralManager!
    public var esp32 : CBPeripheral!
    public var characteristics = [String : CBCharacteristic]() // Es una variable tipo diccionario.
    
    //Variables estaticas que son compartidas con la vista Secundaria (De configuracion)
    public static var esp32Shared : CBPeripheral!
    public static var characteristicsShared = [String : CBCharacteristic]()
    
    var characteristicASCIIValue = NSString()
    
    @IBOutlet weak var estadoConexionLabel: UILabel!
    @IBOutlet weak var insuflacionLabel: UILabel!
    @IBOutlet weak var compresionLabel: UILabel!
    @IBOutlet weak var clockLabel: UILabel!
    @IBOutlet weak var posicionLabel: UILabel!
    @IBOutlet weak var tiempoLabel: UILabel!
    @IBOutlet weak var accionStackView: UIStackView!
    @IBOutlet weak var tiempoStackView: UIStackView!
    
    var contadorY : Int = 0
    var instantes : [Instante] = []
    //Los datos se reciben cada 0.5 segundos, pero se necesita actuar sobre ellos cada 1 segundo.
    var mediosSegundos : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        //Se inicia el manager que controla el bluetooth.
        self.centralManager = CBCentralManager(delegate: self, queue: nil)
        self.instantes = [Instante]()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "juegoEstadisticas"){
            var compresionesNulas : Int = 0
            var compresionesInsuficientes : Int = 0
            var compresionesCorrectas : Int = 0
            var compresionesExcesivas : Int = 0
            for instante in self.instantes {
                if(instante.Compresion == "Nula"){
                    compresionesNulas+=1;
                }
                if(instante.Compresion == "Insuficiente"){
                    compresionesInsuficientes+=1;
                }
                if(instante.Compresion == "Correcta"){
                    compresionesCorrectas+=1;
                }
                if(instante.Compresion == "Excesiva"){
                    compresionesExcesivas+=1;
                }
            }
            let estadisticasViewController = segue.destination as! EstadisticasViewController
            estadisticasViewController.compresionesNulasString = String(compresionesNulas)
            estadisticasViewController.compresionesInsuficientesString = String(compresionesInsuficientes)
            estadisticasViewController.compresionesCorrectasString = String(compresionesCorrectas)
            estadisticasViewController.compresionesExcesivasString = String(compresionesExcesivas)
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
        let insuflacionNula = UIImage(systemName: "stop")!
        let insuflacionCorrecta = UIImage(systemName: "stop.fill")!
        
        //Inicializacion de StackViewHorizontal
        let stackViewHorizontal = UIStackView()
        stackViewHorizontal.axis = .horizontal
        stackViewHorizontal.alignment = .fill // .Leading .FirstBaseline .Center .Trailing .LastBaseline
        stackViewHorizontal.distribution = .fill // .FillEqually .FillProportionally .EqualSpacing .EqualCentering
        
        var imageView1 : UIImageView
        var imageView2 : UIImageView
        var imageView3 : UIImageView = UIImageView(image: insuflacionNula)
        
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
        
        //Evaluacion de compresion de instante 2.
        if(i2.Compresion == "Nula"){
            imageView2 = UIImageView(image: compresionNula)
        }
        else if(i2.Compresion == "Insuficiente"){
            imageView2 = UIImageView(image: compresionInsuficiente)
        }
        else if(i2.Compresion == "Correcta"){
            imageView2 = UIImageView(image: compresionCorrecta)
        }
        else if(i2.Compresion == "Excesiva"){
            imageView2 = UIImageView(image: compresionExcesiva)
        }
        else{
            imageView2 = UIImageView(image: compresionNula)
        }
        
        //Evaluacion de insuflacion.
        if(i1.Insuflacion == "Correcta" || i2.Insuflacion == "Correcta"){
            imageView3 = UIImageView(image: insuflacionCorrecta)
        }
        
        //Posicionamientos
        imageView1.frame = CGRect(x: 0, y: contadorY, width: 10, height: 10)
        imageView2.frame = CGRect(x: 20, y: contadorY, width: 10, height: 10)
        imageView3.frame = CGRect(x: 40, y: contadorY, width: 10, height: 10)
        
        stackViewHorizontal.addSubview(imageView1)
        stackViewHorizontal.addSubview(imageView2)
        stackViewHorizontal.addSubview(imageView3)
        self.accionStackView.addSubview(stackViewHorizontal)
    }
    
    func manejarGraficoTiempo(){
        let segundos : Int = mediosSegundos / 2
        let segundosString : String = String(segundos)
        if(mediosSegundos % 2 == 0){
            self.tiempoLabel.text = segundosString
        }
        if(mediosSegundos % 10 == 0){
            
            let segundosView : UILabel = UILabel()
            segundosView.font.withSize(10)
            segundosView.frame = CGRect(x: 50, y: contadorY, width: 30, height: 20)
            segundosView.text = segundosString
            self.tiempoStackView.addSubview(segundosView)
        }
    }
    
    //Esta funcion es invocada cuando el dispositivo es conectado, es decir pasa a estado 2.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado")
        peripheral.discoverServices(nil)
        estadoConexionLabel.text = "Maniqui conectado";
        estadoConexionLabel.textColor = UIColor.green;
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
        JuegoViewController.characteristicsShared = self.characteristics
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
            
            let insuflacion : String = Conversor.insuflacionToString(n: Int(datosCorrectos[0])!)
            let compresion : String = Conversor.compresionToString(n: Int(datosCorrectos[1])!)
            let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
            let posicionCabeza: String = Conversor.posicionCabezaToString(n: Int(datosCorrectos[3])!)
            
            self.insuflacionLabel.text = insuflacion
            self.compresionLabel.text = compresion
            self.posicionLabel.text = posicion
            
            let instante : Instante = Instante(insuflacion: insuflacion, compresion: compresion, posicion: posicion, posicionCabeza: posicionCabeza)
            
            //Si llegue a 60 segundos.
            if(mediosSegundos == 120){
                performSegue(withIdentifier: "juegoEstadisticas", sender: self)
            }
            
            
            //Agregado de instante al vector.
            self.instantes.append(instante)
            
            //Manejo de variables.
            
            self.mediosSegundos += 1;
            self.contadorY+=5;
            
            //Manejo de graficos.
            
            self.manejarGraficoAccion()
            self.manejarGraficoTiempo()
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
            
            JuegoViewController.esp32Shared = self.esp32
        }
    }
}

