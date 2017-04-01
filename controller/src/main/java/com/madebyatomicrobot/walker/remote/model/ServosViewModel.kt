package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import com.madebyatomicrobot.walker.connector.data.ServosStatus
import com.madebyatomicrobot.walker.remote.ServoEditorFragment
import io.reactivex.disposables.CompositeDisposable
import kotlin.reflect.KMutableProperty

class ServosViewModel(val activity: FragmentActivity, val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ServosViewModel::class.java.simpleName
    }

    var servosConfig: ServosConfig by ViewModelProperty(ServosConfig())
        @Bindable get

    var servosStatus: ServosStatus by ViewModelProperty(ServosStatus())
        @Bindable get

    var oppositeServosSlaved: Boolean by ViewModelProperty(true)
        @Bindable get


    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getServosConfig().subscribe(
                        { config -> servosConfig = config },
                        { error -> Log.e(TAG, "Servos config error", error) }))

        disposables.add(
                connector.getServosStatus().subscribe(
                        { status -> servosStatus = status },
                        { error -> Log.e(TAG, "Servos status error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun servoLabel(servo: String, value: Int): String {
        return "$servo ($value${kotlin.text.Typography.degree})"
    }

    @Bindable fun isReadOnly(): Boolean {
        return !isControlServos() && !isWatchServos()
    }

    fun isControlServos(): Boolean {
        return servosConfig.global.controlServos
    }

    fun setControlServos(controlServos: Boolean) {
        servosConfig.global.controlServos = controlServos
        servosConfig.global.watchServos = false
        notifyChange()

        connector.setServosConfig(servosConfig)
    }

    fun isWatchServos(): Boolean {
        return servosConfig.global.watchServos
    }

    fun setWatchServos(watchServos: Boolean) {
        servosConfig.global.controlServos = false
        servosConfig.global.watchServos = watchServos
        notifyChange()

        connector.setServosConfig(servosConfig)
    }

    @Bindable fun isServo00Enabled(): Boolean = !isReadOnly() && servosConfig.servo00.enabled
    @Bindable fun isServo01Enabled(): Boolean = !isReadOnly() && servosConfig.servo01.enabled
    @Bindable fun isServo02Enabled(): Boolean = !isReadOnly() && servosConfig.servo02.enabled
    @Bindable fun isServo03Enabled(): Boolean = !isReadOnly() && servosConfig.servo03.enabled
    @Bindable fun isServo04Enabled(): Boolean = !isReadOnly() && servosConfig.servo04.enabled
    @Bindable fun isServo05Enabled(): Boolean = !isReadOnly() && servosConfig.servo05.enabled
    @Bindable fun isServo06Enabled(): Boolean = !isReadOnly() && servosConfig.servo06.enabled
    @Bindable fun isServo07Enabled(): Boolean = !isReadOnly() && servosConfig.servo07.enabled
    @Bindable fun isServo08Enabled(): Boolean = !isReadOnly() && servosConfig.servo08.enabled
    @Bindable fun isServo09Enabled(): Boolean = !isReadOnly() && servosConfig.servo09.enabled
    @Bindable fun isServo10Enabled(): Boolean = !isReadOnly() && servosConfig.servo10.enabled
    @Bindable fun isServo11Enabled(): Boolean = !isReadOnly() && servosConfig.servo11.enabled
    @Bindable fun isServo12Enabled(): Boolean = !isReadOnly() && servosConfig.servo12.enabled
    @Bindable fun isServo13Enabled(): Boolean = !isReadOnly() && servosConfig.servo13.enabled
    @Bindable fun isServo14Enabled(): Boolean = !isReadOnly() && servosConfig.servo14.enabled
    @Bindable fun isServo15Enabled(): Boolean = !isReadOnly() && servosConfig.servo15.enabled

    fun setServo(servo: Int, progress: Int) {
        servoFunctions[servo].invoke(progress)
    }

    private val servoFunctions: Array<(Int) -> Unit> = arrayOf(
            { progress -> updateServoFields(progress, servosStatus.servo00::position, servosStatus.servo15::position) },
            { progress -> updateServoFields(progress, servosStatus.servo01::position, servosStatus.servo14::position) },
            { progress -> updateServoFields(progress, servosStatus.servo02::position, servosStatus.servo13::position) },
            { progress -> updateServoFields(progress, servosStatus.servo03::position, servosStatus.servo12::position) },
            { progress -> updateServoFields(progress, servosStatus.servo04::position, servosStatus.servo11::position) },
            { progress -> updateServoFields(progress, servosStatus.servo05::position, servosStatus.servo10::position) },
            { progress -> updateServoFields(progress, servosStatus.servo06::position, servosStatus.servo09::position) },
            { progress -> updateServoFields(progress, servosStatus.servo07::position, servosStatus.servo08::position) },
            { progress -> updateServoFields(progress, servosStatus.servo08::position, servosStatus.servo07::position) },
            { progress -> updateServoFields(progress, servosStatus.servo09::position, servosStatus.servo06::position) },
            { progress -> updateServoFields(progress, servosStatus.servo10::position, servosStatus.servo05::position) },
            { progress -> updateServoFields(progress, servosStatus.servo11::position, servosStatus.servo04::position) },
            { progress -> updateServoFields(progress, servosStatus.servo12::position, servosStatus.servo03::position) },
            { progress -> updateServoFields(progress, servosStatus.servo13::position, servosStatus.servo02::position) },
            { progress -> updateServoFields(progress, servosStatus.servo14::position, servosStatus.servo01::position) },
            { progress -> updateServoFields(progress, servosStatus.servo15::position, servosStatus.servo00::position) })

    private fun updateServoFields(angle: Int, servo: KMutableProperty<Int>, slaveServo: KMutableProperty<Int>) {
        servo.setter.call(angle)
        if (servosConfig.global.controlServos) {
            if (oppositeServosSlaved) {
                slaveServo.setter.call(angle)
            }

            if (servosConfig.global.controlServos) {
                connector.setServosStatus(servosStatus)
            }
        }
    }

    fun editServo(servoId: String) {
        val fragment = ServoEditorFragment.newInstance(servoId)
        fragment.show(activity.supportFragmentManager, null)
    }
}
