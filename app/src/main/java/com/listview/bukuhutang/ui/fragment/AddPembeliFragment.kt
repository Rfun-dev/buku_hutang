package com.listview.bukuhutang.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.listview.bukuhutang.R
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.database.pembeli.Pembeli
import com.listview.bukuhutang.databinding.FragmentAddPembeliBinding
import com.listview.bukuhutang.helper.DataHelper
import com.listview.bukuhutang.ui.viemodel.HistoryViewModel
import com.listview.bukuhutang.ui.viemodel.PembeliViewModel
import com.listview.bukuhutang.ui.viemodel.factory.ViewmodelFactory

class AddPembeliFragment : Fragment() {
    private var _binding: FragmentAddPembeliBinding? = null
    private val binding get() = _binding
    private var totalSetor = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPembeliBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleBundle = AddPembeliFragmentArgs.fromBundle(arguments as Bundle).title
        val pembeli = AddPembeliFragmentArgs.fromBundle(arguments as Bundle).pembeli
        checkNavArgs(titleBundle, pembeli)

        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkNavArgs(titleBundle: String, pembeli: Pembeli?) {
        putDate()
        binding?.apply {
            if (titleBundle == "home_fragment") {
                tvTitle.text = resources.getString(R.string.tambah_pembeli)
                btnSubmit.text = resources.getString(R.string.btn_tambah)
                tvStatusPembayaran.text = resources.getString(R.string.hutang)
                edNama.setText(pembeli?.nama)
                edTanggal.setText(pembeli?.waktu)

            } else {
                tvTitle.text = resources.getString(R.string.update_pembeli)
                btnSubmit.text = resources.getString(R.string.btn_update)
                tvStatusPembayaran.text = resources.getString(R.string.bayar)
                edNama.setText(pembeli?.nama)
                edTanggal.setText(pembeli?.waktu)

            }
            buttonSubmitCLick(titleBundle, pembeli)
        }

    }

    private fun putDate() {
        val constraintBuilder = CalendarConstraints.Builder()
        constraintBuilder.setValidator(DateValidatorPointForward.now())

        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Waktu Bayar")
            .setCalendarConstraints(constraintBuilder.build())
        val picker = builder.build()
        binding?.imbDate?.setOnClickListener {
            picker.show(activity?.supportFragmentManager as FragmentManager, picker.toString())
        }
        picker.addOnPositiveButtonClickListener {
            binding?.edTanggal?.setText(picker.headerText)
        }
    }

    private fun buttonSubmitCLick(state: String, item: Pembeli?) {
        val pembelilViewModel = obtainPembeliViewModel(requireActivity() as AppCompatActivity)
        val historyViewModel = obtainHistoryViewModel(requireActivity() as AppCompatActivity)
        if(item != null){
            historyViewModel.getAllHistoryById(item.id as Int).observe(viewLifecycleOwner) { histories ->
                histories.forEach { historyItem ->
                    if(historyItem.status == "Bayar") totalSetor += historyItem.setor as Int
                }
            }
        }
        binding?.btnSubmit?.setOnClickListener{
            val nama = binding?.edNama?.text
            val tanggal = binding?.edTanggal?.text
            val hutang = binding?.edHutang?.text
            when {
                nama?.isEmpty() as Boolean -> binding?.edNama?.error =
                    resources.getString(R.string.kosong)
                hutang?.isEmpty() as Boolean -> binding?.edHutang?.error =
                    resources.getString(R.string.kosong)
                tanggal?.isEmpty() as Boolean -> binding?.edTanggal?.error =
                    resources.getString(R.string.kosong)
                else -> {
                    when (state) {
                        "home_fragment" -> {
                            val pembeli = Pembeli(
                                item?.id,
                                nama.toString(),
                                tanggal.toString(),
                                hutang.toString().toInt(),
                                null,
                                0,
                            )
                            historyViewModel.deleteAllHistoryId(pembeli.id as Int)
                            pembelilViewModel.insertPembeli(pembeli)
                            pembelilViewModel.updatePembeli(pembeli)
                            findNavController().popBackStack()
                        }
                        "tambah_fragment" -> {
                            val pembeli = Pembeli(
                                item?.id,
                                nama.toString(),
                                tanggal.toString(),
                                item?.hutang?.plus(hutang.toString().toInt()),
                                null,
                                totalSetor,
                            )
                            val history = History(
                                null,
                                pembeli.id,
                                resources.getString(R.string.btn_tambah),
                                hutang.toString().toInt(),
                                DataHelper.getCurrentDate(),
                            )
                            pembelilViewModel.updatePembeli(pembeli)
                            historyViewModel.insertHistory(history)
                            findNavController().popBackStack()
                        }
                        else -> {
                            val pembeli = Pembeli(
                                item?.id,
                                nama.toString(),
                                tanggal.toString(),
                                item?.hutang,
                                null,
                                totalSetor.plus(hutang.toString().toInt()),
                            )
                            val history = History(
                                null,
                                pembeli.id,
                                resources.getString(R.string.bayar),
                                hutang.toString().toInt(),
                                DataHelper.getCurrentDate(),
                            )
                            pembelilViewModel.updatePembeli(pembeli)
                            historyViewModel.insertHistory(history)
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun obtainPembeliViewModel(activity: AppCompatActivity): PembeliViewModel {
        val factory = ViewmodelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PembeliViewModel::class.java]
    }

    private fun obtainHistoryViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewmodelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}