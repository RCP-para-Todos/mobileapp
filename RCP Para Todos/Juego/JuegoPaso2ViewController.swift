//
//  JuegoPaso2ViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 11/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit
import CoreBluetooth

class JuegoPaso2ViewController: UIViewController,  CBCentralManagerDelegate, CBPeripheralDelegate
{
    
    @IBOutlet weak var heart1: UIImageView!
    @IBOutlet weak var heart2: UIImageView!
    @IBOutlet weak var heart3: UIImageView!
    @IBOutlet weak var heart4: UIImageView!
    @IBOutlet weak var wind1: UIImageView!
    @IBOutlet weak var wind2: UIImageView!
    @IBOutlet weak var wind3: UIImageView!
    @IBOutlet weak var wind4: UIImageView!
    @IBOutlet weak var labelPuntaje: UILabel!
    
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
    
    var timer = Timer()
    var count : Int = 0
    var antiCount : Int = 0
    var puntaje : Int = 0
    var inicio : Bool = false
    var insuflacionesMomento : Bool = false
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.serviceEvento = ServiceEvento()
        self.centralManager = CBCentralManager(delegate: self, queue: nil)
        self.inicializarBarraSuperior()
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Juego RCP"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func tratamientoRecepcionBluetooth(datosCorrectos: [String]){
        print("ReciboBluetoothPaso2Juego")
        let insuflacion : String = Conversor.insuflacionToString(n: Int(datosCorrectos[0])!)
        let compresion : String = Conversor.compresionToString(n: Int(datosCorrectos[1])!)
        let posicion : String = Conversor.posicionToString(n: Int(datosCorrectos[2])!)
        
        let instante : Instante = Instante(insuflacion: insuflacion, compresion: compresion, posicion: posicion)
        
        //Agregado de instante al vector.
        self.instantes.append(instante)
        
        //Manejo de variables.
        self.mediosSegundos += 1;
    }
    
    func scheduledTimer(){
        self.timer = Timer.scheduledTimer(timeInterval: 0.125, target: self, selector: #selector(self.logicShow), userInfo: nil, repeats: true)
    }
    
    @objc func logicShow(){
        if(self.antiCount % 1000 == 0 && !self.insuflacionesMomento && self.antiCount != 0){
            self.heart1.isHidden = true
            self.heart2.isHidden = true
            self.heart3.isHidden = true
            self.heart4.isHidden = false
            self.logicaColorearCompresion()
        }
        else if(self.antiCount % 1000 == 875 && !self.insuflacionesMomento){
            self.heart1.isHidden = true
            self.heart2.isHidden = true
            self.heart3.isHidden = false
            self.heart4.isHidden = true
        }
        else if(self.antiCount % 1000 == 750 && !self.insuflacionesMomento){
            self.heart1.isHidden = true
            self.heart2.isHidden = false
            self.heart3.isHidden = true
            self.heart4.isHidden = true
        }
        else if(self.antiCount % 1000 == 625 && !self.insuflacionesMomento){
            self.heart1.isHidden = false
            self.heart2.isHidden = true
            self.heart3.isHidden = true
            self.heart4.isHidden = true
        }
        else if(self.antiCount % 1000 == 500 && !self.insuflacionesMomento){
            self.heart1.isHidden = true
            self.heart2.isHidden = true
            self.heart3.isHidden = true
            self.heart4.isHidden = false
        }
        else if(self.antiCount % 1000 == 375 && !self.insuflacionesMomento){
            self.heart1.isHidden = true
            self.heart2.isHidden = true
            self.heart3.isHidden = false
            self.heart4.isHidden = true
        }
        else if(self.antiCount % 1000 == 250 && !self.insuflacionesMomento){
            self.heart1.isHidden = true
            self.heart2.isHidden = false
            self.heart3.isHidden = true
            self.heart4.isHidden = true
        }
        else if(self.antiCount % 1000 == 125 && !self.insuflacionesMomento){
            self.heart1.isHidden = false
            self.heart2.isHidden = true
            self.heart3.isHidden = true
            self.heart4.isHidden = true
        }
        //Cada condicion representa 30 segundos, cada 30 segundos se paran las compresiones y se realizan las insuflaciones.
        if((self.antiCount > 29000 && self.antiCount < 34000) || (self.antiCount > 64000 && self.antiCount < 69000) || (self.antiCount > 99000 && self.antiCount < 104000) || (self.antiCount > 134000 && self.antiCount < 139000) || (self.antiCount > 169000 && self.antiCount < 174000) || (self.antiCount > 204000 && self.antiCount < 209000) || (self.antiCount > 239000 && self.antiCount < 244000) || (self.antiCount > 274000 && self.antiCount < 279000) || (self.antiCount > 309000 && self.antiCount < 314000) || (self.antiCount > 344000 && self.antiCount < 349000) || (self.antiCount > 379000 && self.antiCount < 384000) || (self.antiCount > 414000 && self.antiCount < 419000) || (self.antiCount > 449000 && self.antiCount < 454000) || (self.antiCount > 484000 && self.antiCount < 489000)){
            self.insuflacionesMomento = true
            self.wind1.isHidden = false
            self.wind2.isHidden = false
            self.wind3.isHidden = false
            self.wind4.isHidden = false
            self.logicaColorearInsuflacion()
        }
        else{
            self.insuflacionesMomento = false
            self.wind1.isHidden = true
            self.wind2.isHidden = true
            self.wind3.isHidden = true
            self.wind4.isHidden = true
        }
        self.antiCount = self.antiCount + 125
        if(self.antiCount >= self.count){
            self.timer.invalidate()
            self.subirEvento()
            self.centralManager = nil
            self.performSegue(withIdentifier: "juego3Segue", sender: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "juego3Segue" {
            if let destinationVC = segue.destination as? JuegoPaso3ViewController {
                destinationVC.puntaje = self.puntaje
            }
        }
    }
    
    func subirEvento(){
        //DATOS DEL EVENTO
        let usuarioActivo = UserDefaults.standard.string(forKey: "usuarioActivo")
        let curso = UserDefaults.standard.string(forKey: "curso")
        let tiempoTotalManiobra = Instante.tiempoTotalManiobra(instantes: self.instantes)
        let tipo = "juego"
        let event_date = Utils.hoy()
        let instantes : [[String : Any]] = Instante.toJson(instantes: self.instantes)
        let puntaje = self.puntaje


        let parameters : [String: Any] = [
           "user" : usuarioActivo!,
           "course" : curso!,
           "duration" : tiempoTotalManiobra,
           "type" : tipo,
           "event_date": event_date,
           "instants": instantes,
           "puntaje": puntaje
           ]
        self.serviceEvento!.newEvento(parameters: parameters, completion: self.newEvento)
    }
    
    func newEvento(completion: Bool){
        
    }
    
    func logicaColorearCompresion(){
        let instante = self.instantes[self.instantes.count-1]
        if(instante.Compresion == "Nula"){
            self.heart4.tintColor = .white
            self.puntaje = self.puntaje - 40
        }
        else if(instante.Compresion == "Insuficiente"){
            self.heart4.tintColor = .lightGray
            self.puntaje = self.puntaje - 40
        }
        else if(instante.Compresion == "Correcta"){
            self.heart4.tintColor = .green
            self.puntaje = self.puntaje + 100
        }
        else if(instante.Compresion == "Excesiva"){
            self.heart4.tintColor = .red
            self.puntaje = self.puntaje - 40
        }
        if(self.puntaje < 0){
            self.puntaje = 0
        }
        self.labelPuntaje.text = String(self.puntaje)
    }
    
    func logicaColorearInsuflacion(){
        let instante = self.instantes[self.instantes.count-1]
        if(instante.Insuflacion == "Nula"){
            self.heart4.tintColor = .white
            self.puntaje = self.puntaje - 60
        }
        else if(instante.Insuflacion == "Insuficiente"){
            self.heart4.tintColor = .lightGray
            self.puntaje = self.puntaje - 60
        }
        else if(instante.Insuflacion == "Correcta"){
            self.heart4.tintColor = .green
            self.puntaje = self.puntaje + 50
        }
        else if(instante.Insuflacion == "Excesiva"){
            self.heart4.tintColor = .red
            self.puntaje = self.puntaje - 60
        }
    }
    
    // MARK: DELEGADOS BLUETOOTH
    
    //Esta funcion es invocada cuando el dispositivo es conectado, es decir pasa a estado 2.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral)
    {
        print("Conectado Paso2 Juego")
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
        JuegoPaso2ViewController.characteristicsShared = self.characteristics
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
        
        if(!self.inicio){
            self.scheduledTimer()
            self.inicio = true
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
            print("Bluetooth activado Paso 2")
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
            
            JuegoPaso2ViewController.esp32Shared = self.esp32
        }
    }
}
