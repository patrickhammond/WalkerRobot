package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import android.widget.SeekBar
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import io.reactivex.disposables.CompositeDisposable

class ServoEditorViewModel(val servoId: String, val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ServoEditorViewModel::class.java.simpleName
    }

    var servoConfig: ServosConfig.Servo by ViewModelProperty(ServosConfig.Servo())
        @Bindable get

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getServoConfig(servoId).subscribe(
                        { servoConfig = it },
                        { Log.e(TAG, "Servo config error", it) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun getServoLabel(): String = "Servo $servoId"

    fun isEnabled(): Boolean = servoConfig.enabled

    fun setEnabled(enabled: Boolean) {
        servoConfig.enabled = enabled
        saveServoConfig()
    }

    fun isInverted(): Boolean = servoConfig.inverted

    fun setInverted(inverted: Boolean) {
        servoConfig.inverted = inverted
        saveServoConfig()
    }

    fun getAdjustmentLabel(): String = "Adjustment (${getAdjustment()}${kotlin.text.Typography.degree})"

    fun getAdjustment(): Int = servoConfig.adjustment

    fun adjustmentChanged(v: SeekBar, adjustment: Int, fromUser: Boolean) = setAdjustment(adjustment)

    fun setAdjustment(adjustment: Int) {
        servoConfig.adjustment = adjustment
        saveServoConfig()
    }

    private fun saveServoConfig() {
        connector.setServoConfig(servoId, servoConfig)
        notifyChange()
    }
}
