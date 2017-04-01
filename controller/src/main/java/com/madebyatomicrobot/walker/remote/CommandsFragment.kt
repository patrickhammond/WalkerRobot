package com.madebyatomicrobot.walker.remote

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.remote.model.CommandsViewModel
import javax.inject.Inject

class CommandsFragment : Fragment(), CommandsViewModel.CommandsView {
    companion object {
        fun newInstance(): CommandsFragment {
            return CommandsFragment()
        }
    }

    interface CommandsHost {
        fun showHumansMessage()
    }

    private var host: CommandsHost? = null
    @Inject lateinit var connector: RemoteConnector

    lateinit var viewModel: CommandsViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        host = context as CommandsHost

        ControllerApplication.getApp(context).component.inject(this)
    }

    override fun onDetach() {
        host = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = CommandsViewModel(this, connector)

        val binding = DataBindingUtil.inflate<CommandBinding>(inflater!!, R.layout.fragment_commands, container, false)
        binding.viewModel = viewModel
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

    override fun showHumansMessage() {
        host!!.showHumansMessage()
    }
}
