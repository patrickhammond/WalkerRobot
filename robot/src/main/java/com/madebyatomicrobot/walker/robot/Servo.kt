package com.madebyatomicrobot.walker.robot

import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.ServosConfig
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class Servo(
        private val pca9685: PCA9685,
        private val channel: Int,
        private val minPulseDuration: Float = 0.5F,
        private val maxPulseDuration: Float = 2.5F,
        private val softwareMinAngle: Float = 0.0F,
        private val softwareMaxAngle: Float = 180.0F,
        private val physicalMin: Float = 20.0F,
        private val physicalMax: Float = 160.0F,
        private var enabled: Boolean = true,
        private var inverted: Boolean = false,
        private var adjustment: Float = 0.0F,
        private var defaultAngle: Float = 90.0F) {

    private val angleObservable = BehaviorSubject.createDefault(defaultAngle)

    init {
        moveToAngle(defaultAngle)
    }

    fun updateServoConfig(servoConfig: ServosConfig.Servo) {
        enabled = servoConfig.enabled
        inverted = servoConfig.inverted
        adjustment = servoConfig.adjustment
        defaultAngle = servoConfig.defaultAngle

        if (enabled) {
            moveToAngle(defaultAngle)
        } else {
            turnOff()
        }
    }

    fun getAngle() : Float = angleObservable.value

    fun getDefaultAngle() = defaultAngle

    fun moveToAngle(angle: Float) {
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
        pca9685.setPwm(channel, 0.0F, pw)
    }

    fun turnOff() {
        pca9685.setPwm(channel, 0, 4096)  // FIXME Magic number for off
    }
}