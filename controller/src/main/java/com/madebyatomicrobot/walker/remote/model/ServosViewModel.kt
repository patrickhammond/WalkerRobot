package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import com.madebyatomicrobot.walker.connector.data.ServosStatus
import com.madebyatomicrobot.walker.remote.ServoEditorFragment
import io.reactivex.disposables.CompositeDisposable

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

    fun isControlServos(): Boolean = servosConfig.global.controlServos

    fun setControlServos(controlServos: Boolean) {
        servosConfig.global.controlServos = controlServos
        servosConfig.global.watchServos = false
        saveServoConfig()
    }

    fun isWatchServos(): Boolean = servosConfig.global.watchServos

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
    private fun isReadOnly(): Boolean = !isControlServos() && !isWatchServos()

    fun servo00Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo00, servosStatus.servo15)
    fun servo01Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo01, servosStatus.servo14)
    fun servo02Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo02, servosStatus.servo13)
    fun servo03Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo03, servosStatus.servo12)
    fun servo04Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo04, servosStatus.servo11)
    fun servo05Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo05, servosStatus.servo10)
    fun servo06Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo06, servosStatus.servo09)
    fun servo07Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo07, servosStatus.servo08)
    fun servo08Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo08, servosStatus.servo07)
    fun servo09Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo09, servosStatus.servo06)
    fun servo10Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo10, servosStatus.servo05)
    fun servo11Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo11, servosStatus.servo04)
    fun servo12Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo12, servosStatus.servo03)
    fun servo13Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo13, servosStatus.servo02)
    fun servo14Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo14, servosStatus.servo01)
    fun servo15Changed(v: SeekBar, progress: Int, fromUser: Boolean) = updateServo(progress, servosStatus.servo15, servosStatus.servo00)

    private fun updateServo(angle: Int, servo: ServosStatus.Servo, slaveServo: ServosStatus.Servo) {
        servo.position = angle
        if (servosConfig.global.controlServos) {
            if (oppositeServosSlaved) {
                slaveServo.position = angle
            }

            if (servosConfig.global.controlServos) {
                connector.setServosStatus(servosStatus)
            }
        }
    }

    fun enableAllServos(v: View) = setAllServosEnabled(true)
    fun disableAllServos(v: View) = setAllServosEnabled(false)

    private fun setAllServosEnabled(enabled: Boolean) {

    }

    fun editServo00(v: View) = editServo("servo00")
    fun editServo01(v: View) = editServo("servo01")
    fun editServo02(v: View) = editServo("servo02")
    fun editServo03(v: View) = editServo("servo03")
    fun editServo04(v: View) = editServo("servo04")
    fun editServo05(v: View) = editServo("servo05")
    fun editServo06(v: View) = editServo("servo06")
    fun editServo07(v: View) = editServo("servo07")
    fun editServo08(v: View) = editServo("servo08")
    fun editServo09(v: View) = editServo("servo09")
    fun editServo10(v: View) = editServo("servo10")
    fun editServo11(v: View) = editServo("servo11")
    fun editServo12(v: View) = editServo("servo12")
    fun editServo13(v: View) = editServo("servo13")
    fun editServo14(v: View) = editServo("servo14")
    fun editServo15(v: View) = editServo("servo15")

    private fun editServo(servoId: String) {
        val fragment = ServoEditorFragment.newInstance(servoId)
        fragment.show(activity.supportFragmentManager, null)
    }
}
