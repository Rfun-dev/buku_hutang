package com.listview.bukuhutang.helper

import androidx.recyclerview.widget.DiffUtil
import com.listview.bukuhutang.database.pembeli.Pembeli

class DiffCallback(private val mOldPembeliList : List<Pembeli>, private val mNewPembeliList : List<Pembeli>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldPembeliList.size

    override fun getNewListSize(): Int = mNewPembeliList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldPembeliList[oldItemPosition].id == mNewPembeliList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        mNewPembeliList[newItemPosition].let { new ->
            mOldPembeliList[oldItemPosition].let { old ->
                val nameBoolean = new.nama == old.nama
                val hutangBoolean = new.hutang == old.hutang
                val waktuBoolean = new.waktu == old.waktu
                val jatuhTempoBoolean = new.jatuhTempo == old.jatuhTempo
                val telahDibayarBoolean = new.setor == old.setor

                return nameBoolean && hutangBoolean && waktuBoolean && jatuhTempoBoolean && telahDibayarBoolean
            }
        }
    }
}