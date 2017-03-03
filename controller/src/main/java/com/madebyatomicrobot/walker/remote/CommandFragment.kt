package com.madebyatomicrobot.walker.remote

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CommandFragment : Fragment() {
    companion object {
        fun newInstance(): CommandFragment {
            return CommandFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val command = Command()
        val binding = DataBindingUtil.inflate<CommandBinding>(inflater!!, R.layout.fragment_command, container, false)
        binding.command = command
        return binding.root
    }
}
