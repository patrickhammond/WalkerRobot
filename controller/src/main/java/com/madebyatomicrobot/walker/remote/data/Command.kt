package com.madebyatomicrobot.walker.remote.data

class Command(var current: String = STOPPED) {
    companion object {
        val STOPPED = "stopped"
        val FORWARD = "forward"
        val REVERSE = "reverse"
    }
}