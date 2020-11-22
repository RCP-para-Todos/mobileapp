//
//  SimulacionPaso3ViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 01/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit
import CoreBluetooth

class SimulacionPaso3ViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate
{
    @IBOutlet weak var labelTimerCountDown: UILabel!
    @IBOutlet weak var imageHeart: UIImageView!
    @IBOutlet weak var imageWind: UIImageView!
    @IBOutlet weak var labelCountCompresiones: UILabel!
    @IBOutlet weak var labelCountInsuflaciones: UILabel!
    
    public var delegate: BLEDelegate?
    
    public var centralManager : CBCentralManager!
    public var esp32 : CBPeripheral!
    public var characteristics = [String : CBCharacteristic]()

    public static var esp32Shared : CBPeripheral!
    public static var characteristicsShared = [String : CBCharacteristic]()

    var characteristicASCIIValue = NSString()
    
    //Manejo de instantes.
    var serviceEvento : ServiceEvento?
    var instantes : [Instante] = []
    var mediosSegundos : Int = 0
    
    //Opciones seleccionadas desde paso2.
    var ambulanciaClicked : Bool = false
    var entornoNoSeguroClicked : Bool = false
    var elEntornoEsSeguro : Bool = false
    
    //Countdown
    var timerCountDown = Timer()
    var minutes : Int = 0
    var seconds : Int = 0
    var totalSeconds : Int = Int(Constants.SIMULACION_DURACION_SEGUNDOS_PASO3)
    
    //Compresiones e Insuflaciones correctas.
    var compresionesCorrectas : Int = 0
    var insuflacionesCorrectas : Int = 0
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.centralManager = CBCentralManager(delegate: self, queue: nil)
        self.serviceEvento = ServiceEvento()
        self.inicializarBarraSuperior()
        self.inicializarTimerCountDown()
        self.instantes = [Instante]()
        self.scheduledCountDown()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.centralManager = nil
        self.timerCountDown.invalidate()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Simulacion RCP Paso 3"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras"
        backButton.target = self
        backButton.action = #selector(volverAtras2Views)
        self.navigationController!.navigationBar.topItem!.rightBarButtonItem = backButton
    }
    
    @objc func volverAtras2Views() {
        self.navigationController!.popViewControllers(viewsToPop: 3)
    }

    
    func tratamientoRecepcionBluetooth(datosCorrectos: [String]){
        let insuflacion : String = Conversor.insuflacionToString(n: Int(datosCorrectos[0])!)
        let compresion : String = Conversor.compresionToString(n: Int(datosCorrectos[1])!)
        let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
        let posicionCabeza: String = Conversor.posicionCabezaToString(n: Int(datosCorrectos[3])!)
        
        //FIX 22/11 si en el escenario no era para interactuar con el maniqui e interactue, entonces paso a la siguiente pantalla.
        let defaults = UserDefaults.standard
        let escenarioNumero = defaults.integer(forKey: "escenarioNumeroFix")
        if(escenarioNumero == 1 || escenarioNumero == 2 || escenarioNumero == 3){
            if(insuflacion != "Nula" || compresion != "Nula"){
                self.centralManager = nil
                self.performSegue(withIdentifier: "pasoEstadisticasSimulacion", sender: nil)
            }
        }
        
        let instante : Instante = Instante(insuflacion: insuflacion, compresion: compresion, posicion: posicion, posicionCabeza: posicionCabeza)
        
        //Agregado de instante al vector.
        self.instantes.append(instante)
        
        self.logicaEntornoNoSeguro(instante: instante)
        
        //Manejo de variables.
        self.mediosSegundos += 1;
        
        //Manejo de graficos.
        self.manejarGrafico(instante: instante)
    }
    
    /*
     Si el entorno es no seguro, y el usuario no lo declara como no seguro se llega a esta pantalla de todas formas.
     Se desprenden 4 posibles flujos:
     1)El entorno no es seguro, no se marca como no seguro, no se llama a la ambulancia y se realiza alguna accion, se sale inmediatamente de esta pantalla.
     2)El entorno no es seguro, no se marca como no seguro, se llama a la ambulancia y se realiza alguna accion,
        se sale inmediatamente de esta pantalla.
     3) El entorno no es seguro, no se marca como no seguro, no se llama a la ambulancia y no se realiza ninguna accion (raro), se espera el tiempo y al final se informa.
     4) El entorno no es seguro, no se marca como no seguro, se llama a la ambulancia y no se realiza ninguna accion (raro), se espera el tiempo y al final se informa.
     */
    func logicaEntornoNoSeguro(instante: Instante){
        if(self.entornoNoSeguroClicked && !self.elEntornoEsSeguro){
            if(instante.Compresion != "Nula" || instante.Insuflacion != "Nula"){
                self.centralManager = nil
                self.performSegue(withIdentifier: "pasoEstadisticasSimulacion", sender: nil)
            }
        }
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
            self.compresionesCorrectas = self.compresionesCorrectas + 1
            self.labelCountCompresiones.text = String(self.compresionesCorrectas) + " Compresiones"
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
            self.insuflacionesCorrectas = self.insuflacionesCorrectas + 1
            self.labelCountInsuflaciones.text = String(self.insuflacionesCorrectas) + " Insuflaciones"
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
    
    func subirEvento(){
        
        //DATOS DEL EVENTO
        let usuarioActivo = UserDefaults.standard.string(forKey: "usuarioActivo")
        let curso = UserDefaults.standard.string(forKey: "curso")
        let duracion = String(Constants.SIMULACION_DURACION_SEGUNDOS_PASO3)
        let tipo = "simulacion"
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
            "duration" : duracion,
            "type" : tipo,
            "event_date": event_date,
            "instants": instantes,
            "tiempoInactividad": tiempoTotalInactividad,
            "porcentajeSobrevida": porcentajeSobrevida,
            "porcentajeInsuflacionOk": porcentajeInsuflacionesCorrectas,
            "porcentajeCompresionOk": porcentajeCompresionesCorrectas,
            "cantidadInsuflacionesOkMalCabeza": cantidadInsuflacionesCorrectasMalaPosicion,
            "fuerzaPromedioAplicada": fuerzaPromedioAplicada,
            "calidadInsuflaciones":calidadInsuflaciones,
            /*"brazosFlexionados": false,
            "noConsultaEstadoVictima": false,
            "noEstaAtentoAlEscenario": false,
            "disponeAyudaNoSolicita": false,
            "demoraTomaDesiciones": false*/
            ]
        self.serviceEvento?.newEvento(parameters: parameters, completion: self.newEvento)
    }
    
    func newEvento(completion: Bool){
        
    }
    
    // MARK: TIMER
    
    func inicializarTimerCountDown(){
        self.minutes = (self.totalSeconds % 3600) / 60
        self.seconds = (self.totalSeconds % 3600) % 60
    }
    
    //Scheduler Countdown
    func scheduledCountDown(){
        self.timerCountDown = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(self.logicCountDown), userInfo: nil, repeats: true)
    }
    
    //Logic Countdown
    @objc func logicCountDown(){
        if self.minutes == 0 && self.seconds == 0 {
            self.minutes = -1
            self.seconds = -1
            self.labelTimerCountDown.text = "00:00:00"
            self.subirEvento()
            self.centralManager = nil
            self.performSegue(withIdentifier: "pasoEstadisticasSimulacion", sender: nil)
        }
        else{
            self.totalSeconds-=1
            self.minutes = (self.totalSeconds % 3600) / 60
            self.seconds = (self.totalSeconds % 3600) % 60
            self.labelTimerCountDown.text = String(format: "%02d:%02d:%02d", 0, minutes, seconds)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "pasoEstadisticasSimulacion" {
            if let destinationVC = segue.destination as? SimulacionEstadisticasViewController {
                destinationVC.ambulanciaClicked = self.ambulanciaClicked
                destinationVC.entornoNoSeguroClicked = self.entornoNoSeguroClicked
                destinationVC.elEntornoEsSeguro = self.elEntornoEsSeguro
                destinationVC.instantes = self.instantes
            }
        }
    }
    
    // MARK: DELEGADOS BLUETOOTH
    
    //Esta funcion es invocada cuando el dispositivo es conectado, es decir pasa a estado 2.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado Paso3 Simulacion")
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
        SimulacionPaso3ViewController.characteristicsShared = self.characteristics
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
            
            SimulacionPaso3ViewController.esp32Shared = self.esp32
        }
    }
    
}

