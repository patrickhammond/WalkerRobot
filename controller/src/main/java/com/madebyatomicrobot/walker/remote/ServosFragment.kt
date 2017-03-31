package com.madebyatomicrobot.walker.remote

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.remote.model.ServosViewModel
import javax.inject.Inject

class ServosFragment : Fragment() {
    companion object {
        fun newInstance(): ServosFragment {
            return ServosFragment()
        }
    }

    @Inject lateinit var connector: RemoteConnector

    lateinit var viewModel: ServosViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ControllerApplication.getApp(context!!).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ServosViewModel(connector)

        val binding = DataBindingUtil.inflate<ServosBinding>(inflater!!, R.layout.fragment_servos, container, false)
        binding.servos = viewModel
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
