package com.listview.bukuhutang.database.history

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class History(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    @ColumnInfo("id_pembeli")
    val idPembeli : Int? = null,
    val status : String? = null,
    val setor : Int? = null,
    val tanggal : String? = null,
) : Parcelable
