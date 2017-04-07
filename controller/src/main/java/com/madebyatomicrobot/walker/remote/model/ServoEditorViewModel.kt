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

        private val DEFAULT_ANGLE_RANGE = 180

        private val ADJUSTMENT_RANGE = 40
        private val HALF_ADJUSTMENT_RANGE = 40 / 2
    }

    var servoConfig: ServosConfig.Servo? = null

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getServoConfig(servoId).subscribe(
                        this::handleServoConfig,
                        { Log.e(TAG, "Servo config error", it) }))
    }

    fun onPause() {
        disposables.clear()
    }

    private fun handleServoConfig(servoConfig: ServosConfig.Servo) {
        this.servoConfig = servoConfig
        notifyChange()
    }

    fun getServoLabel(): String = servoId

    var enabled: Boolean
        @Bindable get() {
            return servoConfig?.enabled ?: false
        }
        set(value) {
            servoConfig?.enabled = value
            saveServoConfig()
        }

    var inverted: Boolean
        @Bindable get() {
            return servoConfig?.inverted ?: false
        }
        set(value) {
            servoConfig?.inverted = value
            saveServoConfig()
        }

    @Bindable fun getDefaultAngleLabel(): String = "Default Angle (${getDefaultAngle()}${kotlin.text.Typography.degree})"

    fun getDefaultAngleMax() = DEFAULT_ANGLE_RANGE

    @Bindable fun getDefaultAngleProgress(): Int = getDefaultAngle().toInt()

    private fun getDefaultAngle(): Float = servoConfig?.defaultAngle ?: 0.0F

    fun defaultAngleChanged(v: SeekBar, defaultAngle: Int, fromUser: Boolean) {
        servoConfig?.defaultAngle = defaultAngle.toFloat()
        saveServoConfig()
    }

    @Bindable fun getAdjustmentLabel(): String = "Adjustment (${getAdjustment()}${kotlin.text.Typography.degree})"

    fun getAdjustmentMax() = ADJUSTMENT_RANGE

    @Bindable fun getAdjustmentProgress(): Int = (getAdjustment() * 2).toInt() + HALF_ADJUSTMENT_RANGE

    private fun getAdjustment(): Float = servoConfig?.adjustment ?: 0.0F

    fun adjustmentChanged(v: SeekBar, adjustment: Int, fromUser: Boolean) {
        val adjustedAdjustment = (adjustment - HALF_ADJUSTMENT_RANGE) / 2.0F
        servoConfig?.adjustment = adjustedAdjustment
        saveServoConfig()
    }

    private fun saveServoConfig() {
        servoConfig?.let {
            connector.setServoConfig(servoId, it)
            notifyChange()
        }
    }
}
