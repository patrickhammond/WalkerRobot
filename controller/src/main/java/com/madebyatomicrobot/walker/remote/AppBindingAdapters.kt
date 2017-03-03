package com.madebyatomicrobot.walker.remote

import android.databinding.BindingAdapter
import android.view.View

object AppBindingAdapters {
    @JvmStatic @BindingAdapter("activated")
    fun setActivated(view: View, activated: Boolean) {
        view.isActivated = activated
    }
}

