package com.madebyatomicrobot.walker.connector.data

data class Servos(
        var controlServos: Boolean = false,
        var watchServos: Boolean = false,
        var servo00: Servo = Servo(),
        var servo01: Servo = Servo(),
        var servo02: Servo = Servo(),
        var servo03: Servo = Servo(),
        var servo04: Servo = Servo(),
        var servo05: Servo = Servo(),
        var servo06: Servo = Servo(),
        var servo07: Servo = Servo(),
        var servo08: Servo = Servo(),
        var servo09: Servo = Servo(),
        var servo10: Servo = Servo(),
        var servo11: Servo = Servo(),
        var servo12: Servo = Servo(),
        var servo13: Servo = Servo(),
        var servo14: Servo = Servo(),
        var servo15: Servo = Servo()) {

    companion object {
        private val DEFAULT_SERVO_ANGLE = 90
        private val DEFAULT_ADJUSTMENT_ANGLE = 90
    }

    data class Servo(
            var position: Int = DEFAULT_SERVO_ANGLE,
            var adjustment: Int = DEFAULT_ADJUSTMENT_ANGLE)
}