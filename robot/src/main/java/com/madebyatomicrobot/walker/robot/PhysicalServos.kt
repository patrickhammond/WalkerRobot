package com.madebyatomicrobot.walker.robot

import com.madebyatomicrobot.things.drivers.PCA9685
import com.madebyatomicrobot.walker.connector.data.ServosConfig

data class PhysicalServos(
        private val pca9685: PCA9685,
        val servo00: Servo = Servo(pca9685, 0),
        val servo01: Servo = Servo(pca9685, 1),
        val servo02: Servo = Servo(pca9685, 2),
        val servo03: Servo = Servo(pca9685, 3),
        val servo04: Servo = Servo(pca9685, 4),
        val servo05: Servo = Servo(pca9685, 5),
        val servo06: Servo = Servo(pca9685, 6),
        val servo07: Servo = Servo(pca9685, 7),
        val servo08: Servo = Servo(pca9685, 8),
        val servo09: Servo = Servo(pca9685, 9),
        val servo10: Servo = Servo(pca9685, 10),
        val servo11: Servo = Servo(pca9685, 11),
        val servo12: Servo = Servo(pca9685, 12),
        val servo13: Servo = Servo(pca9685, 13),
        val servo14: Servo = Servo(pca9685, 14),
        val servo15: Servo = Servo(pca9685, 15)) {

    fun updateServoConfig(servoConfig: ServosConfig) {
        servo00.updateServoConfig(servoConfig.servo00)
        servo01.updateServoConfig(servoConfig.servo01)
        servo02.updateServoConfig(servoConfig.servo02)
        servo03.updateServoConfig(servoConfig.servo03)
        servo04.updateServoConfig(servoConfig.servo04)
        servo05.updateServoConfig(servoConfig.servo05)
        servo06.updateServoConfig(servoConfig.servo06)
        servo07.updateServoConfig(servoConfig.servo07)
        servo08.updateServoConfig(servoConfig.servo08)
        servo09.updateServoConfig(servoConfig.servo09)
        servo10.updateServoConfig(servoConfig.servo10)
        servo11.updateServoConfig(servoConfig.servo11)
        servo12.updateServoConfig(servoConfig.servo12)
        servo13.updateServoConfig(servoConfig.servo13)
        servo14.updateServoConfig(servoConfig.servo14)
        servo15.updateServoConfig(servoConfig.servo15)
    }
}