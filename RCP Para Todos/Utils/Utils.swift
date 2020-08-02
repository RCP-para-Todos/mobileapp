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
}
