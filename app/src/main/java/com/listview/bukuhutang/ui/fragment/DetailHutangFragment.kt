package com.listview.bukuhutang.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.listview.bukuhutang.R
import com.listview.bukuhutang.database.pembeli.Pembeli
import com.listview.bukuhutang.databinding.FragmentDetailBinding
import com.listview.bukuhutang.helper.DataHelper
import com.listview.bukuhutang.ui.viemodel.PembeliViewModel
import com.listview.bukuhutang.ui.viemodel.factory.ViewmodelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class DetailHutangFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding
    private val detailTag = "detail_fragment"
    private val tambahTag = "tambah_fragment"
    private val homeTag = "home_fragment"
    private lateinit var pembeli : Pembeli
    private lateinit var viewModel : PembeliViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        pembeli = DetailHutangFragmentArgs.fromBundle(arguments as Bundle).pembeli
        viewModel.getPembeliById(pembeli.id as Int).observe(viewLifecycleOwner){
            val lunas = it.hutang?.minus(it.setor as Int) as Int
            if(lunas <= 0){
                binding?.btnDetailTambah?.visibility = View.INVISIBLE
            }
            if(lunas <= 0){
                binding?.btnDetailBayar?.text =  resources.getString(R.string.btn_tambah)
            }
        }
        setupView()

        binding?.btnDetailBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.btnDetailBayar?.setOnClickListener {
            val control = DetailHutangFragmentDirections.actionDetailHutangFragmentToBayarFragment()
            viewModel.getPembeliById(pembeli.id as Int).observe(viewLifecycleOwner){
                val lunas = it.hutang?.minus(it.setor as Int) as Int
                control.pembeli = it
                if(lunas >= 0){
                    control.title = detailTag
                }else{
                    control.title = homeTag
                }
                findNavController().navigate(control)
            }


        }

        binding?.btnDetailLihatDetail?.setOnClickListener {
            val action = DetailHutangFragmentDirections.actionDetailHutangFragmentToDetailBayarFragment()
            action.id = pembeli.id as Int
            findNavController().navigate(action)
        }

        binding?.btnDetailTambah?.setOnClickListener {
            val action = DetailHutangFragmentDirections.actionDetailHutangFragmentToBayarFragment()
            viewModel.getPembeliById(pembeli.id as Int).observe(viewLifecycleOwner){
                action.pembeli = it
                action.title = tambahTag
                findNavController().navigate(action)
            }
        }
    }

    private fun setupView(){
        viewModel.getPembeliById(pembeli.id as Int).observe(viewLifecycleOwner){
            val format = SimpleDateFormat("dd MMMM yyyy",Locale.getDefault())
            val dateSelected = format.parse(it.waktu.toString())
            val dateCurrent = format.parse(DataHelper.getCurrentDate())
            val mDifference = abs(dateCurrent?.time?.minus(dateSelected?.time as Long) as Long)
            val lunas = it.hutang?.minus(it.setor as Int)
            if(mDifference != 0L){
                val mDifferenceDate = mDifference / (24 * 60 * 60 * 1000)
                val mDayDifference = mDifferenceDate.toString()
                var setorPerhari = lunas?.div(mDifferenceDate)
                setorPerhari = ((setorPerhari?.plus(99))?.div(100))?.times(100)
                var setorPerbulan = if (mDifferenceDate <= 30 ) lunas?.toLong() else it.hutang?.div(mDifferenceDate.div(30))
                setorPerbulan = ((setorPerbulan?.plus(99))?.div(100))?.times(100)

                binding?.tvDetailHutangPerhari?.text = resources.getString(R.string.harga,setorPerhari)
                binding?.tvDetailHutangPerbulan?.text = resources.getString(R.string.harga,setorPerbulan)
                binding?.tvDetailJatuhtempo?.text = resources.getString(R.string.day,mDayDifference)
            }else{
                binding?.tvDetailHutangPerhari?.text = resources.getString(R.string.harga,pembeli.hutang)
                binding?.tvDetailHutangPerbulan?.text = resources.getString(R.string.harga,pembeli.hutang)
                binding?.tvDetailJatuhtempo?.text = resources.getString(R.string.jatuh_tempo)
            }
            binding?.tvDetailName?.text = it.nama
            binding?.tvDetailHutang?.text = resources.getString(R.string.harga,lunas)
            binding?.tvDetailTotalHutang?.text = resources.getString(R.string.harga,it.hutang)
            binding?.tvDetailBayar?.text = resources.getString(R.string.harga,it.setor)
        }
    }

    private fun obtainViewModel(activity : AppCompatActivity) : PembeliViewModel{
        val factory = ViewmodelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[PembeliViewModel::class.java]
    }
}