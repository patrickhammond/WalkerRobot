package com.madebyatomicrobot.walker.remote

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.remote.model.ServoEditorViewModel
import javax.inject.Inject

class ServoEditorFragment : BottomSheetDialogFragment() {
    companion object {
        val EXTRA_SERVO_ID = "servo_id"

        fun newInstance(servoId: String): ServoEditorFragment {
            val args = Bundle()
            args.putString(EXTRA_SERVO_ID, servoId)

            val fragment = ServoEditorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var connector: RemoteConnector
    lateinit var servoId: String

    lateinit var viewModel: ServoEditorViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ControllerApplication.getApp(context!!).component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        servoId = arguments.getString(EXTRA_SERVO_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ServoEditorViewModel(servoId, connector)

        val binding = DataBindingUtil.inflate<ServoEditorBinding>(inflater!!, R.layout.fragment_servo_editor, container, false)
        binding.servoEditor = viewModel
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