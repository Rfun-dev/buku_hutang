package com.listview.bukuhutang.database.history

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Transaction
    @Query("SELECT * FROM history WHERE id_pembeli=:idPembeli")
    fun getAllHistorybyId(idPembeli : Int) : LiveData<List<History>>

    @Insert
    fun insertHistory(history: History)

    @Update
    fun updateHistory(history: History)

    @Query("DELETE FROM history WHERE id_pembeli = :idPengguna")
    fun deleteAllHistoryById(idPengguna : Int)
}