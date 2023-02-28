package com.listview.bukuhutang.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.listview.bukuhutang.R
import com.listview.bukuhutang.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    private var _binding : FragmentOnBoardingBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.button?.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            onBoardingFinished()
        }
    }

    private fun onBoardingFinished() {
        val sharePref = requireActivity().getSharedPreferences("onBoarding",Context.MODE_PRIVATE)
        val editor = sharePref.edit()
        editor.putBoolean("Finished",true)
        editor.apply()
    }
}