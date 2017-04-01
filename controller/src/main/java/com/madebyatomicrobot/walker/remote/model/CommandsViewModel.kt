package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.Command
import com.madebyatomicrobot.walker.connector.data.Command.Companion.FORWARD
import com.madebyatomicrobot.walker.connector.data.Command.Companion.RESET
import com.madebyatomicrobot.walker.connector.data.Command.Companion.REVERSE
import com.madebyatomicrobot.walker.connector.data.Command.Companion.STOPPED
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import io.reactivex.disposables.CompositeDisposable

class CommandsViewModel(val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = CommandsViewModel::class.java.simpleName
    }

    var command: Command = Command()
        set(value) {
            field = value
            notifyChange()
        }

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getCommand().subscribe(
                        { _command -> command = _command; },
                        { error -> Log.e(TAG, "Command error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    @Bindable fun isReset(): Boolean = command.current == RESET
    @Bindable fun isStopped(): Boolean = command.current == STOPPED
    @Bindable fun isForward(): Boolean = command.current == FORWARD
    @Bindable fun isReverse(): Boolean = command.current == REVERSE

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