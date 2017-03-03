package com.madebyatomicrobot.walker.remote

import android.databinding.BaseObservable
import android.databinding.Bindable

class Command : BaseObservable() {
    companion object {
        private val STOPPED = 0
        private val FORWARD = 1
        private val REVERSE = 2
    }

    private var currentCommand = STOPPED

    val stopped: Boolean
        @Bindable get() = currentCommand == STOPPED

    val forward: Boolean
        @Bindable get() = currentCommand == FORWARD

    val reverse: Boolean
        @Bindable get() = currentCommand == REVERSE

    fun stop() {
        currentCommand = STOPPED
        notifyCommandPropertyChanged()
    }

    fun forward() {
        currentCommand = FORWARD
        notifyCommandPropertyChanged()
    }

    fun reverse() {
        currentCommand = REVERSE
        notifyCommandPropertyChanged()
    }

    private fun notifyCommandPropertyChanged() {
        notifyPropertyChanged(BR.stopped)
        notifyPropertyChanged(BR.forward)
        notifyPropertyChanged(BR.reverse)
    }
}
