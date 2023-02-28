package com.listview.bukuhutang.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.listview.bukuhutang.R
import com.listview.bukuhutang.databinding.FragmentSplahScreenBinding

class SplahScreen : Fragment() {
    private var _binding : FragmentSplahScreenBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplahScreenBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            if(onBoardingFinished()){
                findNavController().navigate(R.id.action_splahScreen_to_loginFragment)
            }else{
                findNavController().navigate(R.id.action_splahScreen_to_onBoardingFragment)
            }
        },3000)
    }

    private fun onBoardingFinished() : Boolean{
        val sharePref = requireActivity().getSharedPreferences("onBoarding",Context.MODE_PRIVATE)
        return sharePref.getBoolean("Finished",false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
