package com.listview.bukuhutang.helper

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object DataHelper {
    fun getCurrentDate() : String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun getFormatCurrency(value : Double) : String{
        val locale = Locale("IND","ID")
        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        val formatRupiah =  numberFormat.format(value)
        val split = formatRupiah.split(",")
        val length = split[0].length
        return "${split[0].substring(0,2)}, ${split[0].substring(2,length)}"
    }


}