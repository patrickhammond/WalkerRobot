package com.madebyatomicrobot.walker.remote

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madebyatomicrobot.walker.remote.model.Servos

class ServosFragment : Fragment() {
    companion object {
        fun newInstance(): ServosFragment {
            return ServosFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val servos = Servos()
        val binding = DataBindingUtil.inflate<ServosBinding>(inflater!!, R.layout.fragment_servos, container, false)
        binding.servos = servos
        return binding.root
    }
}
