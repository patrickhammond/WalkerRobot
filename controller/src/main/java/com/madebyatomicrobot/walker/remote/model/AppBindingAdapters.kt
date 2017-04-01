package com.madebyatomicrobot.walker.remote.model

import android.databinding.BindingAdapter
import android.graphics.Paint
import android.view.View
import android.widget.TextView


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

    @JvmStatic @BindingAdapter("strikethrough")
    fun setStrikethrough(view: TextView, strikethrough: Boolean) {
        view.setPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG, strikethrough)
    }

    private fun TextView.setPaintFlag(flag: Int, added: Boolean) {
        if (added) {
            this.paintFlags = this.paintFlags or flag
        } else {
            this.paintFlags = this.paintFlags and flag.inv()
        }
    }
}

