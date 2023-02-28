package com.listview.bukuhutang.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.listview.bukuhutang.R
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.databinding.HistoryItemBinding

class HistoryAdapter(val activity : AppCompatActivity): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    lateinit var historyList : List<History>
    class ViewHolder(val binding : HistoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            historyList[position].let {
                tvSetor.text = it.setor.toString()
                tvStatus.text = it.status ?: "Bayar"
                Log.d("TAG", "onBindViewHolder: ${it.status}")
                if(it.status == "Tambah"){
                    tvSetor.setTextColor(activity.getColor(R.color.red_200))
                }else if(it.status == "Bayar"){
                    tvSetor.setTextColor(activity.getColor(R.color.blue_200))
                }
                tvWaktu.text =  it.tanggal
            }
        }
    }

    override fun getItemCount(): Int = historyList.size
}