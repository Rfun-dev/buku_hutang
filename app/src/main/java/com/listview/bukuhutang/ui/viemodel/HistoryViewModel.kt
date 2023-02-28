package com.listview.bukuhutang.ui.viemodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.repository.AppRepository

class HistoryViewModel(val application: Application) : ViewModel() {
    private val mRepository = AppRepository(application)

    fun getAllHistoryById(idPengguna : Int) = mRepository.getAllHistoryById(idPengguna)

    fun insertHistory(history: History) = mRepository.insertHistory(history)

    fun updateHistory(history: History) = mRepository.updateHistory(history)

    fun deleteAllHistoryId(idPengguna: Int) = mRepository.deleteAllHistoryById(idPengguna)
}