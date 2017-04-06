package com.madebyatomicrobot.walker.connector.data

data class ServosConfig(
        var global: GlobalServosStatus = GlobalServosStatus(),
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
        private val DEFAULT_ADJUSTMENT_ANGLE = 0.0
        private val DEFAULT_ENABLED = false
        private val DEFAULT_INVERTED = false
    }

    data class GlobalServosStatus(
            var controlServos: Boolean = false,
            var watchServos: Boolean = false
    )

    data class Servo(
            var adjustment: Double = DEFAULT_ADJUSTMENT_ANGLE,
            var enabled: Boolean = DEFAULT_ENABLED,
            var inverted: Boolean = DEFAULT_INVERTED)
}