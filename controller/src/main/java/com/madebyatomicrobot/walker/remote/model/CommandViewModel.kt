package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.Command
import com.madebyatomicrobot.walker.connector.data.Command.Companion.FORWARD
import com.madebyatomicrobot.walker.connector.data.Command.Companion.RESET
import com.madebyatomicrobot.walker.connector.data.Command.Companion.REVERSE
import com.madebyatomicrobot.walker.connector.data.Command.Companion.STOPPED
import io.reactivex.disposables.CompositeDisposable

class CommandViewModel(val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = CommandViewModel::class.java.simpleName
    }

    var command: Command by ViewModelProperty(Command())
        @Bindable get

    val reset: Boolean
        @Bindable get() = command.current == RESET

    val stopped: Boolean
        @Bindable get() = command.current == STOPPED

    val forward: Boolean
        @Bindable get() = command.current == FORWARD

    val reverse: Boolean
        @Bindable get() = command.current == REVERSE

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getCommand().subscribe(
                        { _command -> command = _command },
                        { error -> Log.e(TAG, "Command error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun reset() {
        command.current = RESET
        commandChanged()
    }

    fun stop() {
        command.current = STOPPED
        commandChanged()
    }

    fun forward() {
        command.current = FORWARD
        commandChanged()
    }

    fun reverse() {
        command.current = REVERSE
        commandChanged()
    }

    private fun commandChanged() {
        connector.setCommand(command)
        notifyChange()
    }
}
