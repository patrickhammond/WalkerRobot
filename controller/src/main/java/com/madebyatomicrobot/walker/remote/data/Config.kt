package com.madebyatomicrobot.walker.remote.data

class Config(
        val left: LegConfig = LegConfig(),
        val right: LegConfig = LegConfig()) {

    class LegConfig(
            val hipY: ServoConfig = ServoConfig(),
            val hipX: ServoConfig = ServoConfig(),
            val hipZ: ServoConfig = ServoConfig(),
            val knee: ServoConfig = ServoConfig(),
            val ankle: ServoConfig = ServoConfig())

    class ServoConfig(
            var action: String = "",
            var duration: String = "")
}