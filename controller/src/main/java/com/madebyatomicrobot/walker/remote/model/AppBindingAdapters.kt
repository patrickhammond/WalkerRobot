package com.madebyatomicrobot.walker.remote.model

import android.databinding.BindingAdapter
import android.view.View

object AppBindingAdapters {
    @JvmStatic @BindingAdapter("activated")
    fun setActivated(view: View, activated: Boolean) {
        view.isActivated = activated
    }

    @JvmStatic @BindingAdapter("untouchable")
    fun setUntouchable(view: View, untouchable: Boolean) {
        view.setOnTouchListener(
                when (untouchable) {
                    true -> View.OnTouchListener { _, _ -> true }
                    false -> null
                })
    }

    @JvmStatic @BindingAdapter("enabled")
    fun setEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
    }
}

