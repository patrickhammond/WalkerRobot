package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.Servos
import io.reactivex.disposables.CompositeDisposable

class ServoEditorViewModel(val servoId: String, val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ServoEditorViewModel::class.java.simpleName
    }

    var servo: Servos.Servo by ViewModelProperty(Servos.Servo())
        @Bindable get

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getServo(servoId).subscribe(
                        { _servo -> servo = _servo },
                        { error -> Log.e(TAG, "Servo error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun isEnabled(): Boolean {
        return servo.enabled
    }

    fun setEnabled(enabled: Boolean) {
        servo.enabled = enabled
        connector.setServo(servoId, servo)
        notifyChange()
    }

    fun getAdjustment(): Int {
        return servo.adjustment
    }

    fun setAdjustment(adjustment: Int) {
        servo.adjustment = adjustment
        connector.setServo(servoId, servo)
        notifyChange()
    }

    fun getAdjustmentLabel(): String {
        return "Adjustment (${getAdjustment()}${kotlin.text.Typography.degree})"
    }
}
