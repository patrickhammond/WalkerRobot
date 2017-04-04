package com.madebyatomicrobot.walker.robot

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.Actions
import com.madebyatomicrobot.walker.connector.data.Command
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import com.madebyatomicrobot.walker.remote.RobotApplication
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException
import javax.inject.Inject


class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    @Inject lateinit var connector: RemoteConnector

    private lateinit var i2c: I2cDevice
    private lateinit var pca9685: PCA9685
    private lateinit var servos: PhysicalServos
    private lateinit var walker: Walker

    private lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        RobotApplication.getApp(this).component.inject(this)
        super.onCreate(savedInstanceState)

        disposables = CompositeDisposable()

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "I2C: " + manager.i2cBusList)
            i2c = manager.openI2cDevice("I2C1", 0x40)

            pca9685 = PCA9685(i2c)
            pca9685.resetI2C()
            pca9685.setPWMFreq(50.0)  // 50 Hz

            servos = PhysicalServos(pca9685)
            walker = Walker(servos)

            disposables.add(
                    connector.getServosConfig().subscribe(
                            this::handleServoConfig,
                            { Log.e(TAG, "Servos config error", it) }))

            disposables.add(
                    connector.getCommand().subscribe(
                            this::handleServoCommand,
                            { Log.e(TAG, "Command error", it) }))

            disposables.add(
                    connector.getActions().subscribe(
                            this::handleActions,
                            { Log.e(TAG, "Actions error", it) }))

        } catch (ex: IOException) {
            Log.w(TAG, "Error in onCreate", ex)
        }
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

    private fun handleServoConfig(servosConfig: ServosConfig) = servos.updateServoConfig(servosConfig)

    private fun handleServoCommand(command: Command) = walker.handleCommand(command)

    private fun handleActions(actions: Actions) = walker.handleActions(actions)
}
