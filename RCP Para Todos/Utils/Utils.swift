//
//  Utils.swift
//  RCP Para Todos
//
//  Created by Juan Tocino on 02/08/2020.
//  Copyright © 2020 Reflejo. All rights reserved.
//

import Foundation

public class Utils{
    public static func hoy() -> String{
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        let result = formatter.string(from: date)
        return result
    }
    
    public static func translateDateMongo(date: String)->String{
        let rawDate = date
        let separedDate = rawDate.components(separatedBy: "-")
        let separedDate2 = separedDate[2].components(separatedBy: "T")
        let separedDate3 = separedDate2[1].components(separatedBy: ".")
        let finalDate = separedDate2[0]+"-"+separedDate[1]+"-"+separedDate[0]+" "+separedDate3[0]
        return finalDate
    }
    
    public static func translateDateToEasyRead(date: String)->String{
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
        dateFormatter.dateFormat = "dd-MM-yyyy HH:mm:ss"
        let dateF = dateFormatter.date(from:date)!
        let difference = Date() - dateF
        if difference.day! < 0{
            if(difference.hour! < 0){
                if(difference.minute! < 0){
                    return "Hace unos instantes"
                }
                else{
                    return "Hace \(difference.minute!) minutos"
                }
            }
            else{
                return "Hace \(difference.hour!) horas"
            }
        }
        else if difference.day! == 1{
            return "Ayer"
        }
        else if difference.day! > 1 {
            return "Hace \(difference.day!) días"
        }
        return date
    }
}

extension Date {

    static func -(recent: Date, previous: Date) -> (month: Int?, day: Int?, hour: Int?, minute: Int?, second: Int?) {
        let day = Calendar.current.dateComponents([.day], from: previous, to: recent).day
        let month = Calendar.current.dateComponents([.month], from: previous, to: recent).month
        let hour = Calendar.current.dateComponents([.hour], from: previous, to: recent).hour
        let minute = Calendar.current.dateComponents([.minute], from: previous, to: recent).minute
        let second = Calendar.current.dateComponents([.second], from: previous, to: recent).second

        return (month: month, day: day, hour: hour, minute: minute, second: second)
    }

}
