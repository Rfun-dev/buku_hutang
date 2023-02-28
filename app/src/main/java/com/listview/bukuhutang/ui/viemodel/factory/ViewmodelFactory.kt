package com.listview.bukuhutang.ui.viemodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.ui.viemodel.HistoryViewModel
import com.listview.bukuhutang.ui.viemodel.PembeliViewModel

class ViewmodelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var INSTANCE : ViewmodelFactory? = null

        @JvmStatic
        fun getInstance(mApplication: Application) : ViewmodelFactory{
            if(INSTANCE == null){
                synchronized(ViewmodelFactory::class.java){
                    INSTANCE = ViewmodelFactory(mApplication)
                }
            }
            return INSTANCE as ViewmodelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PembeliViewModel::class.java)){
            return PembeliViewModel(mApplication) as T
        }
        else if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class : ${modelClass::class.java}")
    }
}