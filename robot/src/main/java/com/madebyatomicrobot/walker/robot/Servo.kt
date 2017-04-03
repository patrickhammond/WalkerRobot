package com.madebyatomicrobot.walker.robot

import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class Servo(
        val pca9685: PCA9685,
        val channel: Int,
        val minPulseDuration: Double = 0.5,
        val maxPulseDuration: Double = 2.5,
        val softwareMinAngle: Double = 0.0,
        val softwareMaxAngle: Double = 180.0,
        val physicalMin: Double = 20.0,
        val physicalMax: Double = 160.0,
        val defaultAngle: Double = 90.0,
        var enabled: Boolean = true,
        var inverted: Boolean = false,
        var adjustment: Double = 0.0) {  // Due to horn placement

    private val angleObservable = BehaviorSubject.create<Double>()

    init {
        moveToAngle(defaultAngle)
    }

    fun updateServoConfig(servoConfig: ServosConfig.Servo) {
        val enabledChanged = enabled != servoConfig.enabled

        enabled = servoConfig.enabled
        inverted = servoConfig.inverted
        adjustment = servoConfig.adjustment

        if (enabledChanged) {
            // FIXME - Turn the servo on or off
        }
    }

    fun getAngle() : Double = angleObservable.value

    fun getObservableAngle() : Observable<Double> = angleObservable

    fun moveToDefaultAngle() = moveToAngle(defaultAngle)

    fun moveToAngle(angle: Int) = moveToAngle(angle.toDouble())

    fun moveToAngle(angle: Float) = moveToAngle(angle.toDouble())

    fun moveToAngle(angle: Double) {
        angleObservable.onNext(angle)

        val range = (softwareMaxAngle - softwareMinAngle)
        var realAngle = angle + adjustment

        if (realAngle < physicalMin) {
            realAngle = physicalMin
        }

        if (realAngle > physicalMax) {
            realAngle = physicalMax
        }

        if (inverted) {
            realAngle = Math.abs(realAngle - range)
        }

        val percentOfRange = (realAngle - softwareMinAngle) / range  // FIXME need to be smarter about softwareMinAngle when not zero
        val pw = ((minPulseDuration + (maxPulseDuration - minPulseDuration) * percentOfRange) * 1000)
        pca9685.setPwm(channel, 0.0, pw)
    }
}