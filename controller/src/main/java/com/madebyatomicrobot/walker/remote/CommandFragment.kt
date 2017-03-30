package com.madebyatomicrobot.walker.remote

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.madebyatomicrobot.walker.remote.model.CommandViewModel

class CommandFragment : Fragment() {
    companion object {
        fun newInstance(): CommandFragment {
            return CommandFragment()
        }
    }

    lateinit var commandViewModel: CommandViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commandViewModel = CommandViewModel(FirebaseDatabase.getInstance().reference)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<CommandBinding>(inflater!!, R.layout.fragment_command, container, false)
        binding.command = commandViewModel
        return binding.root
    }
}
