import Foundation
import UIKit
import Charts

class AprendizajeEstadisticasViewController: UIViewController, ChartViewDelegate
{
    @IBOutlet var pieChartView: PieChartView!
    @IBOutlet weak var labelTotalInsuflado: UILabel!
    var instantes : [Instante] = []
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.inicializarBarraSuperior()
        self.pieChartView.delegate = self
        self.initGraph()
        self.totalInsuflado()
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
    
    func inicializarBarraSuperior()
    {
        self.title = "Aprendiendo RCP Estadisticas"
        let backButton = UIBarButtonItem()
        backButton.isEnabled = true
        backButton.title = "Atras";
        self.navigationController!.navigationBar.topItem!.backBarButtonItem = backButton
    }
    
    @IBAction func irAlInicioClicked(_ sender: Any) {
        self.navigationController!.popViewControllers(viewsToPop: 8)
    }
    

}
