package com.madebyatomicrobot.walker.connector.data

data class Command(var current: String = RESET) {
    companion object {
        val RESET = "reset"
        val STOPPED = "stopped"
        val FORWARD = "forward"
        val REVERSE = "reverse"
    }
}