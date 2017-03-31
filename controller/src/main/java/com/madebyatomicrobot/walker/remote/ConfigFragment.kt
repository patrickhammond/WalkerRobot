package com.madebyatomicrobot.walker.remote

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.remote.model.ConfigViewModel
import javax.inject.Inject

class ConfigFragment : Fragment() {
    companion object {
        fun newInstance(): ConfigFragment {
            return ConfigFragment()
        }
    }

    @Inject lateinit var connector: RemoteConnector

    lateinit var viewModel: ConfigViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ControllerApplication.getApp(context!!).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ConfigViewModel(connector)

        val binding = DataBindingUtil.inflate<ConfigBinding>(inflater!!, R.layout.fragment_config, container, false)
        binding.config = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }
}
