package com.madebyatomicrobot.walker.connector.data

data class Actions(
        val walk: Action = Action()) {

    data class Action(
            val left: LegConfig = LegConfig(),
            val right: LegConfig = LegConfig())

    data class LegConfig(
            val hipY: ServoConfig = ServoConfig(),  // TOP
            val hipX: ServoConfig = ServoConfig(),  // MIDDLE
            val hipZ: ServoConfig = ServoConfig(),  // BOTTOM
            val knee: ServoConfig = ServoConfig(),
            val ankle: ServoConfig = ServoConfig())

    data class ServoConfig(
            var action: String = "",
            var duration: String = "")
}