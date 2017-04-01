package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
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

    @Bindable fun isReadOnly(): Boolean = !isControlServos() && !isWatchServos()

    fun isControlServos(): Boolean {
        return servosConfig.global.controlServos
    }

    fun setControlServos(controlServos: Boolean) {
        servosConfig.global.controlServos = controlServos
        servosConfig.global.watchServos = false
        saveServoConfig()
    }

    fun isWatchServos(): Boolean {
        return servosConfig.global.watchServos
    }

    fun setWatchServos(watchServos: Boolean) {
        servosConfig.global.controlServos = false
        servosConfig.global.watchServos = watchServos
        saveServoConfig()
    }

    private fun saveServoConfig() {
        notifyChange()
        connector.setServosConfig(servosConfig)
    }

    @Bindable fun getServo00Label(): String = getServoLabel("00", servosStatus.servo00)
    @Bindable fun getServo01Label(): String = getServoLabel("01", servosStatus.servo01)
    @Bindable fun getServo02Label(): String = getServoLabel("02", servosStatus.servo02)
    @Bindable fun getServo03Label(): String = getServoLabel("03", servosStatus.servo03)
    @Bindable fun getServo04Label(): String = getServoLabel("04", servosStatus.servo04)
    @Bindable fun getServo05Label(): String = getServoLabel("05", servosStatus.servo05)
    @Bindable fun getServo06Label(): String = getServoLabel("06", servosStatus.servo06)
    @Bindable fun getServo07Label(): String = getServoLabel("07", servosStatus.servo07)
    @Bindable fun getServo08Label(): String = getServoLabel("08", servosStatus.servo08)
    @Bindable fun getServo09Label(): String = getServoLabel("09", servosStatus.servo09)
    @Bindable fun getServo10Label(): String = getServoLabel("10", servosStatus.servo10)
    @Bindable fun getServo11Label(): String = getServoLabel("11", servosStatus.servo11)
    @Bindable fun getServo12Label(): String = getServoLabel("12", servosStatus.servo12)
    @Bindable fun getServo13Label(): String = getServoLabel("13", servosStatus.servo13)
    @Bindable fun getServo14Label(): String = getServoLabel("14", servosStatus.servo14)
    @Bindable fun getServo15Label(): String = getServoLabel("15", servosStatus.servo15)

    private fun getServoLabel(id: String, servo: ServosStatus.Servo): String = "$id (${servo.position}${kotlin.text.Typography.degree})"

    @Bindable fun isServo00Enabled(): Boolean = isServoEnabled(servosConfig.servo00)
    @Bindable fun isServo01Enabled(): Boolean = isServoEnabled(servosConfig.servo01)
    @Bindable fun isServo02Enabled(): Boolean = isServoEnabled(servosConfig.servo02)
    @Bindable fun isServo03Enabled(): Boolean = isServoEnabled(servosConfig.servo03)
    @Bindable fun isServo04Enabled(): Boolean = isServoEnabled(servosConfig.servo04)
    @Bindable fun isServo05Enabled(): Boolean = isServoEnabled(servosConfig.servo05)
    @Bindable fun isServo06Enabled(): Boolean = isServoEnabled(servosConfig.servo06)
    @Bindable fun isServo07Enabled(): Boolean = isServoEnabled(servosConfig.servo07)
    @Bindable fun isServo08Enabled(): Boolean = isServoEnabled(servosConfig.servo08)
    @Bindable fun isServo09Enabled(): Boolean = isServoEnabled(servosConfig.servo09)
    @Bindable fun isServo10Enabled(): Boolean = isServoEnabled(servosConfig.servo10)
    @Bindable fun isServo11Enabled(): Boolean = isServoEnabled(servosConfig.servo11)
    @Bindable fun isServo12Enabled(): Boolean = isServoEnabled(servosConfig.servo12)
    @Bindable fun isServo13Enabled(): Boolean = isServoEnabled(servosConfig.servo13)
    @Bindable fun isServo14Enabled(): Boolean = isServoEnabled(servosConfig.servo14)
    @Bindable fun isServo15Enabled(): Boolean = isServoEnabled(servosConfig.servo15)

    private fun isServoEnabled(servo: ServosConfig.Servo): Boolean = !isReadOnly() && servo.enabled

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

    fun editServo00(view: View) = editServo("00")
    fun editServo01(view: View) = editServo("01")
    fun editServo02(view: View) = editServo("02")
    fun editServo03(view: View) = editServo("03")
    fun editServo04(view: View) = editServo("04")
    fun editServo05(view: View) = editServo("05")
    fun editServo06(view: View) = editServo("06")
    fun editServo07(view: View) = editServo("07")
    fun editServo08(view: View) = editServo("08")
    fun editServo09(view: View) = editServo("09")
    fun editServo10(view: View) = editServo("10")
    fun editServo11(view: View) = editServo("11")
    fun editServo12(view: View) = editServo("12")
    fun editServo13(view: View) = editServo("13")
    fun editServo14(view: View) = editServo("14")
    fun editServo15(view: View) = editServo("15")

    private fun editServo(servoId: String) {
        val fragment = ServoEditorFragment.newInstance(servoId)
        fragment.show(activity.supportFragmentManager, null)
    }
}
