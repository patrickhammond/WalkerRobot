package com.madebyatomicrobot.walker.robot

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.*
import com.madebyatomicrobot.walker.remote.RobotApplication
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    @Inject lateinit var connector: RemoteConnector

    private lateinit var i2c: I2cDevice
    private lateinit var pca9685: PCA9685

    private var servos: PhysicalServos? = null
    private var robot: Robot? = null

    private lateinit var disposables: CompositeDisposable

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
            robot = Robot(servos!!)
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
        Log.v("DEBUG", "handleActions: $actions")
        robot?.handleActions(actions)
        updateRobotTimestamp()
    }

    private fun handleServoConfig(servosConfig: ServosConfig) {
        Log.v("DEBUG", "handleServoConfig: $servosConfig")
        servos?.updateServoConfig(servosConfig)
        updateRobotTimestamp()
    }

    private fun handleServoCommand(command: Command) {
        Log.v("DEBUG", "handleServoCommand: $command")
        robot?.handleCommand(command)
        updateRobotTimestamp()
    }

    private fun handleServoStatus(status: ServosStatus) {
        Log.v("DEBUG", "handleServoStatus: $status")
        robot?.handleServoStatus(status)
        updateRobotTimestamp()
    }
}
