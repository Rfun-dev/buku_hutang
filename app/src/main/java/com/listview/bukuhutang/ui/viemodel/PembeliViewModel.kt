package com.listview.bukuhutang.ui.viemodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.listview.bukuhutang.database.pembeli.Pembeli
import com.listview.bukuhutang.repository.AppRepository

class PembeliViewModel(val application: Application) : ViewModel() {
    private val mPembeliRepository : AppRepository = AppRepository(application)

    fun insertPembeli(pembeli: Pembeli) = mPembeliRepository.insertPembeli(pembeli)

    fun updatePembeli(pembeli: Pembeli) = mPembeliRepository.updatePembeli(pembeli)

    fun deletePembeli(pembeli: Pembeli) = mPembeliRepository.deletePembeli(pembeli)

    fun getAllPembeli() : LiveData<List<Pembeli>> = mPembeliRepository.getAllPembeli()

    fun getPembeliById(id : Int) : LiveData<Pembeli> = mPembeliRepository.getPembeliById(id)

}