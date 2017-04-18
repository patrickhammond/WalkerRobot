package com.madebyatomicrobot.walker.robot

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.*
import com.madebyatomicrobot.walker.remote.RobotApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private val SERVO_UPDATE_PERIOD_MS = 200L
    }

    @Inject lateinit var connector: RemoteConnector

    private lateinit var i2c: I2cDevice
    private lateinit var pca9685: PCA9685

    private lateinit var servos: PhysicalServos
    private lateinit var robot: Robot

    private var config: ServosConfig.GlobalServosStatus? = null

    private lateinit var disposables: CompositeDisposable

    private var watchDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        RobotApplication.getApp(this).component.inject(this)
        super.onCreate(savedInstanceState)

        disposables = CompositeDisposable()

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "I2C: " + manager.i2cBusList)
            i2c = manager.openI2cDevice("I2C1", 0x40)  // 0x40 is the default PCA9685 address

            pca9685 = PCA9685(i2c)
            pca9685.resetI2C()
            pca9685.setPWMFreq(50.0)  // 50 Hz

            servos = PhysicalServos(pca9685)
            robot = Robot(servos)
        } catch (ex: IOException) {
            Log.e(TAG, "Error in onCreate", ex)
        }

        updateRobotTimestamp()

        disposables.add(connector.getActions().subscribe(
                this::handleActions,
                { Log.e(TAG, "Actions error", it) }))

        disposables.add(connector.getServosConfig().subscribe(
                this::handleServoConfig,
                { Log.e(TAG, "Servos config error", it) }))

        disposables.add(connector.getCommand().subscribe(
                this::handleServoCommand,
                { Log.e(TAG, "Command error", it) }))

        disposables.add(connector.getServosStatus().subscribe(
                this::handleServoStatus,
                { Log.e(TAG, "Servo status error", it) }))
    }

    override fun onDestroy() {
        endServoWatching()
        disposables.clear()

        try {
            i2c.close()
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onDestroy", ex)
        }

        super.onDestroy()
    }

    private fun updateRobotTimestamp() {
        val now = Date()
        val dateFormat = DateFormat.getDateTimeInstance()
        val timestamp = dateFormat.format(now)
        connector.setRobotUpdateTime(timestamp)
    }

    private fun handleActions(actions: Actions) {
        robot.handleActions(actions)
        updateRobotTimestamp()
    }

    private fun handleServoConfig(servosConfig: ServosConfig) {
        servos.updateServoConfig(servosConfig)
        config = servosConfig.global

        val watching = config!!.watchServos
        if (watching) {
            watchDisposable?.dispose()
            startServoWatching()
        } else {
            watchDisposable?.dispose()
        }

        updateRobotTimestamp()
    }

    private fun startServoWatching() {
        watchDisposable = Observable.interval(SERVO_UPDATE_PERIOD_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ publishServoPositions() })
    }

    private fun endServoWatching() {
        watchDisposable?.dispose()
    }

    private fun publishServoPositions() {
        connector.setServosStatus(robot.getServoStatus())
    }

    private fun handleServoCommand(command: Command) {
        robot.handleCommand(command)
        updateRobotTimestamp()
    }

    private fun handleServoStatus(status: ServosStatus) {
        if (config != null && config!!.controlServos) {
            robot.handleServoStatus(status)
        }
        updateRobotTimestamp()
    }
}
