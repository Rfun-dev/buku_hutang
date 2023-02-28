package com.listview.bukuhutang.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.listview.bukuhutang.database.pembeli.Pembeli
import com.listview.bukuhutang.database.pembeli.PembeliDao
import com.listview.bukuhutang.database.DatabaseApp
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.database.history.HistoryDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppRepository(application: Application){
    private val mPembeliDao : PembeliDao
    private val mHistoryDao : HistoryDao

    private val exeutorsService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = DatabaseApp.getDatabase(application)
        mPembeliDao = db.pembeliDao()
        mHistoryDao = db.historyDao()
    }

    fun getAllPembeli() : LiveData<List<Pembeli>> = mPembeliDao.getAllPembeli()

    fun insertPembeli(pembeli: Pembeli) = exeutorsService.execute {
        mPembeliDao.insertPembeli(pembeli)
    }
    fun getPembeliById(id : Int) = mPembeliDao.getPembeliById(id)

    fun updatePembeli(pembeli: Pembeli) = exeutorsService.execute{
        mPembeliDao.updatePembeli(pembeli)
    }

    fun deletePembeli(pembeli: Pembeli) = exeutorsService.execute{
        mPembeliDao.deletePembeli(pembeli)
    }

    fun getAllHistoryById(idPembeli: Int) : LiveData<List<History>> = mHistoryDao.getAllHistorybyId(idPembeli)

    fun insertHistory(history: History) = exeutorsService.execute {
        mHistoryDao.insertHistory(history)
    }

    fun updateHistory(history: History) = exeutorsService.execute{
        mHistoryDao.updateHistory(history)
    }

    fun deleteAllHistoryById(idPengguna: Int) = exeutorsService.execute{
        mHistoryDao.deleteAllHistoryById(idPengguna)
    }

}