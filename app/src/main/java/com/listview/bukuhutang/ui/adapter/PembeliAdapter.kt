package com.listview.bukuhutang.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.listview.bukuhutang.R
import com.listview.bukuhutang.database.pembeli.Pembeli
import com.listview.bukuhutang.databinding.PembeliItemBinding

class PembeliAdapter(
    private val listener : OnClickListener,
    private val activity : FragmentActivity
) : RecyclerView.Adapter<PembeliAdapter.ViewHolder>(){
    var listPembeli = mutableListOf<Pembeli>()
    class ViewHolder(val binding : PembeliItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClickListener{
        fun onClick(item : Pembeli)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PembeliItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       with(holder){
           listPembeli[position].let{ item ->
               binding.tvNama.text = item.nama
               binding.tvWaktu.text = item.waktu
               if(item.hutang as Int <= item.setor as Int){
                   binding.tvHutang.text = activity.resources.getString(R.string.lunas)
                   binding.tvHutang.setTextColor(activity.getColor(R.color.blue_200))
               }else{
                   binding.tvHutang.text = activity.resources.getString(R.string.harga,item.hutang?.minus(item.setor as Int))
               }
               itemView.setOnClickListener {
                   Navigation.findNavController(itemView).navigate(R.id.action_homePageFragment_to_detailHutangFragment)
               }
               binding.itemPembeli.setOnClickListener {
                   listener.onClick(item)
               }
           }
       }
    }

    override fun getItemCount(): Int = listPembeli.size
}