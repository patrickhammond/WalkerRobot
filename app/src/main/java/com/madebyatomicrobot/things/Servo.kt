package com.madebyatomicrobot.things

class Servo(
        val pca9685: PCA9685,
        val channel: Int,
        val minPulseDuration: Double = 0.5,
        val maxPulseDuration: Double = 2.5,
        val softwareMinAngle: Double = 0.0,
        val softwareMaxAngle: Double = 180.0,
        val physicalMin: Double = 10.0,
        val physicalMax: Double = 158.0,
        val adjustmentAngle: Double = 0.0,  // Due to horn placement
        val invertAngle: Boolean = false) {

    fun moveToAngle(angle: Double) {
        val range = (softwareMaxAngle - softwareMinAngle)
        var realAngle = angle + adjustmentAngle

        if (realAngle < physicalMin) {
            realAngle = physicalMin
        }

        if (realAngle > physicalMax) {
            realAngle = physicalMax
        }

        if (invertAngle) {
            realAngle = Math.abs(realAngle - range)
        }

        val percentOfRange = (realAngle - softwareMinAngle) / range  // FIXME need to be smarter about softwareMinAngle when not zero
        val pw = ((minPulseDuration + (maxPulseDuration - minPulseDuration) * percentOfRange) * 1000)
        pca9685.setPwm(channel, 0.0, pw)
    }
}