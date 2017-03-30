package com.madebyatomicrobot.walker.remote

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.madebyatomicrobot.walker.remote.model.ServosViewModel

class ServosFragment : Fragment() {
    companion object {
        fun newInstance(): ServosFragment {
            return ServosFragment()
        }
    }

    lateinit var servosViewModel: ServosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        servosViewModel = ServosViewModel(FirebaseDatabase.getInstance().reference)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ServosBinding>(inflater!!, R.layout.fragment_servos, container, false)
        binding.servos = servosViewModel
        return binding.root
    }
}
