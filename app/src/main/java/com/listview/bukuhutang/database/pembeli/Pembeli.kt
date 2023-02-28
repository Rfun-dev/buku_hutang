package com.listview.bukuhutang.database.pembeli

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.listview.bukuhutang.database.history.History
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Pembeli(
    @PrimaryKey
    val id : Int? = null,
    val nama : String? = null,
    val waktu : String? = null,
    var hutang : Int? = null,
    @ColumnInfo("jatuh_tempo")
    val jatuhTempo : String? = null,
    @ColumnInfo("setor")
    var setor : Int? = null
) : Parcelable
