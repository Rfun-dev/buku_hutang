package com.listview.bukuhutang.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.listview.bukuhutang.R
import com.listview.bukuhutang.database.history.History
import com.listview.bukuhutang.databinding.FragmentDetailHutangBinding
import com.listview.bukuhutang.databinding.FragmentLoginBinding
import com.listview.bukuhutang.ui.adapter.HistoryAdapter
import com.listview.bukuhutang.ui.viemodel.HistoryViewModel
import com.listview.bukuhutang.ui.viemodel.factory.ViewmodelFactory

class DetailBayarFragment : Fragment() {
    private var _binding : FragmentDetailHutangBinding? = null
    private val binding get() = _binding
    private lateinit var historyViewModel : HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailHutangBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = DetailBayarFragmentArgs.fromBundle(arguments as Bundle).id
        historyViewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        setUpRecylerView(id)
        binding?.btnDetailHutangBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpRecylerView(id : Int){
        binding?.apply{
            val adapter = HistoryAdapter(activity as AppCompatActivity)
            historyViewModel.getAllHistoryById(id).observe(viewLifecycleOwner){
                adapter.historyList = it
                rvDetailHutang.adapter = adapter
            }
            rvDetailHutang.layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun obtainViewModel(activity : AppCompatActivity) : HistoryViewModel{
        val factory = ViewmodelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[HistoryViewModel::class.java]
    }
}