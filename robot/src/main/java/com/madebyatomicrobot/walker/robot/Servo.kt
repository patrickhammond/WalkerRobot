package com.madebyatomicrobot.walker.robot

import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class Servo(
        private val pca9685: PCA9685,
        private val channel: Int,
        private val minPulseDuration: Double = 0.5,
        private val maxPulseDuration: Double = 2.5,
        private val softwareMinAngle: Double = 0.0,
        private val softwareMaxAngle: Double = 180.0,
        private val physicalMin: Double = 20.0,
        private val physicalMax: Double = 160.0,
        private val defaultAngle: Double = 90.0,
        private var enabled: Boolean = true,
        private var inverted: Boolean = false,
        private var adjustment: Double = 0.0) {  // Due to horn placement

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
            if (enabled) {
                moveToAngle(defaultAngle)
            } else {
                turnOff()
            }
        }
    }

    fun getAngle() : Double = angleObservable.value

    fun getObservableAngle() : Observable<Double> = angleObservable

    fun moveToDefaultAngle() = moveToAngle(defaultAngle)

    fun moveToAngle(angle: Float) = moveToAngle(angle.toDouble())

    fun moveToAngle(angle: Double) {
        if (!enabled) {
            return
        }

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

    fun turnOff() {
        pca9685.setPwm(channel, 0, 4096)  // FIXME Magic number for off
    }
}