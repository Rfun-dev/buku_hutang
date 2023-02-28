package com.listview.bukuhutang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.listview.bukuhutang.R
import com.listview.bukuhutang.ui.adapter.PembeliAdapter
import com.listview.bukuhutang.database.pembeli.Pembeli
import com.listview.bukuhutang.databinding.FragmentHomePageBinding
import com.listview.bukuhutang.helper.DataHelper
import com.listview.bukuhutang.ui.viemodel.HistoryViewModel
import com.listview.bukuhutang.ui.viemodel.PembeliViewModel
import com.listview.bukuhutang.ui.viemodel.factory.ViewmodelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomePageFragment : Fragment(), PembeliAdapter.OnClickListener {
    var _binding : FragmentHomePageBinding? = null
    private val binding get() =  _binding
    private val name = "home_fragment"
    private var viewModel : PembeliViewModel? = null
    private var viewModelHistory : HistoryViewModel? = null
    private var adapter : PembeliAdapter? = null
    private var listPembeli = listOf<Pembeli>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater,container,false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        viewModelHistory = obtainViewModelHisoty(requireActivity() as AppCompatActivity)
        viewModel?.getAllPembeli()?.observe(viewLifecycleOwner){
            var totalHarga  = 0
            it.forEach { pembeli ->
                totalHarga += pembeli.hutang?.minus(pembeli.setor as Int) as Int
}
            binding?.tvJumlahHutang?.text = resources.getString(R.string.harga,totalHarga)
            listPembeli = it
            adapter?.listPembeli?.addAll(listPembeli)
            binding?.rvPembeli?.adapter = adapter
        }
        binding?.fabAddPembeli?.setOnClickListener {
            val toAddPembeliFragment = HomePageFragmentDirections.actionHomePageFragmentToAddPembeliFragment()
            toAddPembeliFragment.title = this.name
            view.findNavController().navigate(toAddPembeliFragment)
        }
        setUpListButton()
        setUpView()
        setSwipeRecyclerView()
        setRecyclerView()
    }

    private fun setSwipeRecyclerView(){
        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Delete Item")
                    .setMessage("apakah ingin menghapus item")
                    .setPositiveButton("Konfirmasi"){ _, _ ->
                        val position = viewHolder.adapterPosition
                        val pembeli = listPembeli[position]
                        viewModel?.deletePembeli(pembeli)
                        viewModelHistory?.deleteAllHistoryId(pembeli.id as Int)
                        adapter?.listPembeli?.removeAt(position)
                    }.setNegativeButton("Batal"){ _, _ ->
                        val position = viewHolder.adapterPosition
                        adapter?.notifyItemChanged(position)
                    }
                builder.show()
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding?.rvPembeli)
    }

    @SuppressLint("RestrictedApi")
    private fun setUpListButton(){
        val menuBuilder = MenuBuilder(requireActivity())
        val inflater = MenuInflater(requireActivity())
        inflater.inflate(R.menu.list_menu,menuBuilder)
        binding?.btnList?.setOnClickListener {
            val optionMenu = MenuPopupHelper(requireContext(),menuBuilder,it)
            optionMenu.setForceShowIcon(true)

            menuBuilder.setCallback(object : MenuBuilder.Callback{
                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    when(item.itemId){
                        R.id.zToA -> {
                            adapter?.listPembeli?.clear()
                            adapter?.listPembeli?.addAll(
                               listPembeli.sortedByDescending { pembeli ->
                                   pembeli.nama
                               }
                           )
                            binding?.rvPembeli?.adapter = adapter
                        }
                        R.id.aToZ -> {
                            adapter?.listPembeli?.clear()
                            adapter?.listPembeli?.addAll(
                                listPembeli.sortedBy { pembeli ->
                                    pembeli.nama
                                }
                            )
                            binding?.rvPembeli?.adapter = adapter

                        }
                        R.id.date -> {
                            adapter?.listPembeli?.clear()
                            val dateFormat = DateTimeFormatter.ofPattern("d MMM yyyy")
                            adapter?.listPembeli?.addAll(
                                listPembeli.sortedBy { pembeli ->
                                    LocalDate.parse(pembeli.waktu,dateFormat)
                                }
                            )
                            binding?.rvPembeli?.adapter = adapter
                        }
                        R.id.high_price -> {
                            adapter?.listPembeli?.clear()
                            adapter?.listPembeli?.addAll(
                                listPembeli.sortedByDescending { pembeli ->
                                    pembeli.hutang
                                }
                            )
                            binding?.rvPembeli?.adapter = adapter
                        }
                        R.id.low_price -> {
                            adapter?.listPembeli?.clear()
                            adapter?.listPembeli?.addAll(
                                listPembeli.sortedBy { pembeli ->
                                    pembeli.hutang
                                }
                            )
                            binding?.rvPembeli?.adapter = adapter
                        }
                    }
                    return true
                }

                override fun onMenuModeChange(menu: MenuBuilder) {
                }
            })
            optionMenu.show()
        }
    }

    private fun setUpView(){
        binding?.apply {
            tvHariIni.text = DataHelper.getCurrentDate()
        }
    }

    private fun setRecyclerView(){
        binding?.rvPembeli?.layoutManager = LinearLayoutManager(requireActivity())
        adapter = PembeliAdapter(this,requireActivity())
    }

    private fun obtainViewModel(activity : AppCompatActivity) : PembeliViewModel{
        val factory = ViewmodelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[PembeliViewModel::class.java]
    }

    private fun obtainViewModelHisoty(activity : AppCompatActivity) : HistoryViewModel{
        val factory = ViewmodelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[HistoryViewModel::class.java]
    }

    override fun onClick(item: Pembeli) {
        val action = HomePageFragmentDirections.actionHomePageFragmentToDetailHutangFragment(item)
        findNavController().navigate(action)
    }
}