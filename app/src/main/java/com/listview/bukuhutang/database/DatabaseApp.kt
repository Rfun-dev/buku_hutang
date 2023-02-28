package com.listview.bukuhutang.database

import android.content.Context
import androidx.room.*
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.database.history.HistoryDao
import com.listview.bukuhutang.database.pembeli.PembeliDao
import com.listview.bukuhutang.database.pembeli.Pembeli

@Database(entities = [Pembeli::class, History::class], version = 1, exportSchema = false)
abstract class DatabaseApp : RoomDatabase(){
    abstract fun pembeliDao() : PembeliDao
    abstract fun historyDao() : HistoryDao

    companion object{
        @Volatile
        private var INSTANCE : DatabaseApp? = null

        @JvmStatic
        fun getDatabase(context : Context) : DatabaseApp {
            if(INSTANCE == null){
                synchronized(DatabaseApp::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                                DatabaseApp::class.java,
                                "pembeli_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }

            }
                return INSTANCE as DatabaseApp
        }
    }
}