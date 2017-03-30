package com.madebyatomicrobot.walker.remote

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.madebyatomicrobot.walker.remote.model.ConfigViewModel

class ConfigFragment : Fragment() {
    companion object {
        fun newInstance(): ConfigFragment {
            return ConfigFragment()
        }
    }

    lateinit var configViewModel: ConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configViewModel = ConfigViewModel(FirebaseDatabase.getInstance().reference)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ConfigBinding>(inflater!!, R.layout.fragment_config, container, false)
        binding.config = configViewModel
        return binding.root
    }
}
