package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.Servos
import com.madebyatomicrobot.walker.remote.ServoEditorFragment
import io.reactivex.disposables.CompositeDisposable
import kotlin.reflect.KMutableProperty

class ServosViewModel(val activity: FragmentActivity, val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ServosViewModel::class.java.simpleName
        val IGNORED_BOOLEAN = false
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

    @Bindable fun isServo00Enabled(): Boolean = !isReadOnly() && servos.servo00.enabled
    @Bindable fun isServo01Enabled(): Boolean = !isReadOnly() && servos.servo01.enabled
    @Bindable fun isServo02Enabled(): Boolean = !isReadOnly() && servos.servo02.enabled
    @Bindable fun isServo03Enabled(): Boolean = !isReadOnly() && servos.servo03.enabled
    @Bindable fun isServo04Enabled(): Boolean = !isReadOnly() && servos.servo04.enabled
    @Bindable fun isServo05Enabled(): Boolean = !isReadOnly() && servos.servo05.enabled
    @Bindable fun isServo06Enabled(): Boolean = !isReadOnly() && servos.servo06.enabled
    @Bindable fun isServo07Enabled(): Boolean = !isReadOnly() && servos.servo07.enabled
    @Bindable fun isServo08Enabled(): Boolean = !isReadOnly() && servos.servo08.enabled
    @Bindable fun isServo09Enabled(): Boolean = !isReadOnly() && servos.servo09.enabled
    @Bindable fun isServo10Enabled(): Boolean = !isReadOnly() && servos.servo10.enabled
    @Bindable fun isServo11Enabled(): Boolean = !isReadOnly() && servos.servo11.enabled
    @Bindable fun isServo12Enabled(): Boolean = !isReadOnly() && servos.servo12.enabled
    @Bindable fun isServo13Enabled(): Boolean = !isReadOnly() && servos.servo13.enabled
    @Bindable fun isServo14Enabled(): Boolean = !isReadOnly() && servos.servo14.enabled
    @Bindable fun isServo15Enabled(): Boolean = !isReadOnly() && servos.servo15.enabled

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

    fun editServo(servoId: String) {
        val fragment = ServoEditorFragment.newInstance(servoId)
        fragment.show(activity.supportFragmentManager, null)
    }
}
