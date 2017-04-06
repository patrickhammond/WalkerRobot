package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import android.view.View
import com.madebyatomicrobot.walker.connector.data.Command
import com.madebyatomicrobot.walker.connector.data.Command.Companion.FORWARD
import com.madebyatomicrobot.walker.connector.data.Command.Companion.RESET
import com.madebyatomicrobot.walker.connector.data.Command.Companion.REVERSE
import com.madebyatomicrobot.walker.connector.data.Command.Companion.STOPPED
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import io.reactivex.disposables.CompositeDisposable

class CommandsViewModel(val view: CommandsView, val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = CommandsViewModel::class.java.simpleName
    }

    interface CommandsView {
        fun showHumansMessage()
    }

    var command: Command = Command()
        set(value) {
            field = value
            notifyChange()
        }

    var robotUpdateTime: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyChange()
        }

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getCommand().subscribe(
                        { command = it },
                        { Log.e(TAG, "Command error", it) }))

        disposables.add(
                connector.getRobotUpdateTime().subscribe(
                        { robotUpdateTime = it },
                        { Log.e(TAG, "Robot update time error", it) }))
    }

    fun onPause() {
        disposables.clear()
    }

    @Bindable fun isReset(): Boolean = command.current == RESET
    @Bindable fun isStopped(): Boolean = command.current == STOPPED
    @Bindable fun isForward(): Boolean = command.current == FORWARD
    @Bindable fun isReverse(): Boolean = command.current == REVERSE

    fun reset(v: View) {
        command.current = RESET
        commandChanged()
    }

    fun stop(v: View) {
        command.current = STOPPED
        commandChanged()
    }

    fun forward(v: View) {
        command.current = FORWARD
        commandChanged()
    }

    fun reverse(v: View) {
        command.current = REVERSE
        commandChanged()
    }

    private fun commandChanged() {
        connector.setCommand(command)
        notifyChange()
    }

    fun humans(v: View) {
        view.showHumansMessage()
    }
}
