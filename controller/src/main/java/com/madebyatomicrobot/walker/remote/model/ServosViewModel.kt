package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.Servos
import io.reactivex.disposables.CompositeDisposable
import kotlin.reflect.KMutableProperty

class ServosViewModel(val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ServosViewModel::class.java.simpleName
    }

    var servos: Servos by ViewModelProperty(Servos())
        @Bindable get

    var oppositeServosSlaved: Boolean by ViewModelProperty(true)
        @Bindable get


    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getServos().subscribe(
                        { _servos -> servos = _servos },
                        { error -> Log.e(TAG, "Servos error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun servoLabel(servo: String, value: Int): String {
        return "$servo ($value${kotlin.text.Typography.degree})"
    }

    @Bindable fun isReadOnly(): Boolean {
        return !getControlServos() && !getWatchServos()
    }

    fun getControlServos(): Boolean {
        return servos.controlServos
    }

    fun setControlServos(controlServos: Boolean) {
        servos.controlServos = controlServos
        servos.watchServos = false
        notifyChange()

        connector.setServos(servos)
    }

    fun getWatchServos(): Boolean {
        return servos.watchServos
    }

    fun setWatchServos(watchServos: Boolean) {
        servos.controlServos = false
        servos.watchServos = watchServos
        notifyChange()

        connector.setServos(servos)
    }

    fun setServo(servo: Int, progress: Int) {
        servoFunctions[servo].invoke(progress)
    }

    private val servoFunctions: Array<(Int) -> Unit> = arrayOf(
            { progress -> updateServoFields(progress, servos.servo00::position, servos.servo15::position) },
            { progress -> updateServoFields(progress, servos.servo01::position, servos.servo14::position) },
            { progress -> updateServoFields(progress, servos.servo02::position, servos.servo13::position) },
            { progress -> updateServoFields(progress, servos.servo03::position, servos.servo12::position) },
            { progress -> updateServoFields(progress, servos.servo04::position, servos.servo11::position) },
            { progress -> updateServoFields(progress, servos.servo05::position, servos.servo10::position) },
            { progress -> updateServoFields(progress, servos.servo06::position, servos.servo09::position) },
            { progress -> updateServoFields(progress, servos.servo07::position, servos.servo08::position) },
            { progress -> updateServoFields(progress, servos.servo08::position, servos.servo07::position) },
            { progress -> updateServoFields(progress, servos.servo09::position, servos.servo06::position) },
            { progress -> updateServoFields(progress, servos.servo10::position, servos.servo05::position) },
            { progress -> updateServoFields(progress, servos.servo11::position, servos.servo04::position) },
            { progress -> updateServoFields(progress, servos.servo12::position, servos.servo03::position) },
            { progress -> updateServoFields(progress, servos.servo13::position, servos.servo02::position) },
            { progress -> updateServoFields(progress, servos.servo14::position, servos.servo01::position) },
            { progress -> updateServoFields(progress, servos.servo15::position, servos.servo00::position) })

    private fun updateServoFields(angle: Int, servo: KMutableProperty<Int>, slaveServo: KMutableProperty<Int>) {
        servo.setter.call(angle)
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                slaveServo.setter.call(angle)
            }

            if (servos.controlServos) {
                connector.setServos(servos)
            }
        }
    }
}
