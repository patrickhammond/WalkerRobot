package com.madebyatomicrobot.things

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException

class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    lateinit var startView: Button
    lateinit var stopView: Button

    var gpioPins: MutableList<Gpio> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startView = findViewById(R.id.start) as Button
        stopView = findViewById(R.id.stop) as Button

        startView.setOnClickListener { start() }
        stopView.setOnClickListener { stop() }

        startView.isEnabled = true
        stopView.isEnabled = false

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "Pins: " + manager.gpioList)

            listOf(4, 18, 17, 27, 22, 23, 12 /*24*/, 25).forEach {
                val pin : Gpio = manager.openGpio("BCM$it")
                gpioPins.add(pin)

                pin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
                pin.setActiveType(Gpio.ACTIVE_LOW)
            }
        } catch (ex: IOException) {
            Log.w(TAG, "Unable to access GPIO", ex)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            gpioPins.forEach(Gpio::close)
        } catch (ex: IOException) {
            Log.w(TAG, "Unable to close BCM17", ex)
        }
    }

    private fun start() {
        startView.isEnabled = false
        stopView.isEnabled = true

        gpioPins.forEach({ it.value = false })
        Thread().run {
            gpioPins.forEach {
                it.value = true
                Thread.sleep(250)
                it.value = false
            }
        }
    }

    private fun stop() {
        startView.isEnabled = true
        stopView.isEnabled = false

        gpioPins.forEach({ it.value = false })
    }
}
