//
//  SimulacionEstadisticasViewController.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 01/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation
import UIKit
import Charts

class SimulacionEstadisticasViewController: UIViewController, ChartViewDelegate
{
    @IBOutlet weak var labelResultado: UILabel!
    @IBOutlet weak var pieChartView: PieChartView!
    @IBOutlet weak var labelTotalInsuflado: UILabel!
    @IBOutlet weak var buttonIrAlInicio: UIButton!
    @IBOutlet weak var backButton: UINavigationItem!
    
    
    var instantes : [Instante] = []
    var ambulanciaClicked : Bool = false
    var entornoNoSeguroClicked : Bool = false
    var elEntornoEsSeguro : Bool = false
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.pieChartView.delegate = self
        self.initGraph()
        self.totalInsuflado()
        self.logicaResultado()
        self.initInterface()
    }
        
    func initInterface(){
        self.backButton.backBarButtonItem?.isEnabled = false //PROBAR
        self.buttonIrAlInicio.layer.cornerRadius = 15
    }
    
    func inicializarBarraSuperior()
    {
        self.title = "Simulacion RCP Resultado"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    func totalInsuflado(){
        var mediosegundos = 0
        for i in self.instantes{
            if(i.Insuflacion == "Correcta"){
                mediosegundos += 1
            }
        }
        self.labelTotalInsuflado.text = String(mediosegundos/2)
    }
    
    func logicaResultado(){
        //Si el entorno es seguro y se selecciona llamar a la ambulancia se realiza la simulacion.
       if(self.elEntornoEsSeguro && self.ambulanciaClicked && !self.entornoNoSeguroClicked){
        self.labelResultado.text = "Felicitaciones, el entorno estaba seguro y la ambulancia llegó a tiempo luego del RCP"
       }
       //Si el entorno es seguro pero no selecciona para llamar a la ambulancia se realiza la simulacion pero sera invalidada finalmente porque la ambulancia nunca llegara.
       else if(self.elEntornoEsSeguro && !self.ambulanciaClicked && !self.entornoNoSeguroClicked){
           self.labelResultado.text = "Lo sentimos, aunque la maniobra RCP fue practicada la ambulancia no llegó ya que nunca fue llamada"
       }
       //Si el entorno no es seguro pero no se selecciona el entorno no es seguro, se realiza la simulacion pero sera invalidada finalmente porque el entorno no era seguro.
       else if(!self.elEntornoEsSeguro && !self.entornoNoSeguroClicked && !self.ambulanciaClicked){
           self.labelResultado.text = "Lo sentimos, el entorno era no seguro y no se debía interactuar con el simulador"
       }
       //Si el entorno no es seguro pero se selecciona llamar a la ambulancia, se realiza la simulacion pero sera invalidada finalmente porque el entorno no era seguro.
       else if(!self.elEntornoEsSeguro && !self.entornoNoSeguroClicked && self.ambulanciaClicked){
           self.labelResultado.text = "Lo sentimos, el entorno era no seguro y no se debía interactuar con el simulador"
       }
    }
    
    func initGraph(){
        let labels = ["Nulas", "Insuficientes", "Correctas", "Excesivas"]
        var n : Double = 0, i : Double = 0, c : Double = 0, e : Double = 0
        for ins in self.instantes{
            print(ins.Compresion)
            if(ins.Compresion == "Nula"){
                n = n+1
            }
            else if(ins.Compresion == "Insuficiente"){
                i = i+1
            }
            else if(ins.Compresion == "Correcta"){
                c = c+1
            }
            else if(ins.Compresion == "Excesiva"){
                e = e+1
            }
        }
        let values = [n, i, c, e]
        self.setChart(dataPoints: labels, values: values)
    }
    
    func setChart(dataPoints: [String], values: [Double]) {
        var dataEntries: [ChartDataEntry] = []
        for i in 0..<values.count {
            let dataEntry1 = PieChartDataEntry(value: values[i], label: dataPoints[i])
            dataEntries.append(dataEntry1)
        }
        let pieChartDataSet = PieChartDataSet(entries: dataEntries, label: nil)
        let pieChartData = PieChartData(dataSet: pieChartDataSet)
      
        var colors: [UIColor] = []

        for _ in 0..<dataPoints.count {
        let red = Double(arc4random_uniform(256))
        let green = Double(arc4random_uniform(256))
        let blue = Double(arc4random_uniform(256))

        let color = UIColor(red: CGFloat(red/255), green: CGFloat(green/255), blue: CGFloat(blue/255), alpha: 1)
        colors.append(color)
      }
        pieChartDataSet.colors = colors
        self.pieChartView.data = pieChartData
    }

    @IBAction func buttonIrAlInicioClicked(_ sender: Any) {
        self.navigationController!.popViewControllers(viewsToPop: 4)
    }
    
}

