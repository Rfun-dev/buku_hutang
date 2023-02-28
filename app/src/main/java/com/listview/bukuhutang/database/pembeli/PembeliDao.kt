package com.listview.bukuhutang.database.pembeli

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface PembeliDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPembeli(pembeli: Pembeli)

    @Update
    fun updatePembeli(pembeli: Pembeli)

    @Delete
    fun deletePembeli(pembeli: Pembeli)

    @Transaction
    @Query("SELECT * FROM pembeli ORDER BY id ASC")
    fun getAllPembeli() : LiveData<List<Pembeli>>

    @Transaction
    @Query("SELECT * FROM pembeli WHERE id=:id")
    fun getPembeliById(id : Int) : LiveData<Pembeli>
}