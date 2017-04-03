package com.madebyatomicrobot.walker.robot

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
import com.madebyatomicrobot.things.drivers.ADS1015
import java.io.IOException

class ForceSensorActivity : Activity(), Runnable {
    companion object {
        private val TAG = ForceSensorActivity::class.java.simpleName

        private val MIN_FSR = 0
        private val MAX_FSR = 2047
    }

    lateinit var binding: ForceSensorBinding

    lateinit var i2c: I2cDevice
    lateinit var ads2015: ADS1015

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        handler = Handler()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ForceSensorBinding>(this, R.layout.activity_force_sensor)

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "I2C: " + manager.i2cBusList)
            i2c = manager.openI2cDevice("I2C1", 0x48)

            ads2015 = ADS1015(i2c)
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onCreate", ex)
        }

        handler.post(this)
    }

    override fun onDestroy() {
        handler.removeCallbacks(this)

        try {
            i2c.close()
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onDestroy", ex)
        }

        super.onDestroy()
    }

    override fun run() {
        val fsr1Reading = boundFsrReadings(ads2015.readADC(0))
        val fsr2Reading = boundFsrReadings(ads2015.readADC(1))
        val fsr3Reading = boundFsrReadings(ads2015.readADC(2))
        val fsr4Reading = boundFsrReadings(ads2015.readADC(3))

        binding.fsr11Label.text = "1 / FSR1 ($fsr1Reading)"
        binding.fsr12Label.text = "1 / FSR2 ($fsr2Reading)"
        binding.fsr13Label.text = "1 / FSR3 ($fsr3Reading)"
        binding.fsr14Label.text = "1 / FSR4 ($fsr4Reading)"

        binding.fsr11.progress = fsr1Reading
        binding.fsr12.progress = fsr2Reading
        binding.fsr13.progress = fsr3Reading
        binding.fsr14.progress = fsr4Reading

        val x = (-fsr1Reading + fsr4Reading)
        val y = (-fsr2Reading + fsr3Reading)

        //Log.v("DEBUG", "x: $x, y: $y")

        val angle = Math.toDegrees(Math.cos(y.toDouble() / x.toDouble()))
        val magnitude = (Math.sqrt((x * x).toDouble() + (y * y).toDouble()) / MAX_FSR) * 100

        binding.angle1.text = "1 / Angle = $angle"
        binding.magnitude1.text = "1 / Magnitude = $magnitude%"

        handler.post(this)
    }

    private fun boundFsrReadings(reading: Int) : Int {
        if (reading < MIN_FSR) {
            return MIN_FSR
        } else if (reading > MAX_FSR) {
            return MAX_FSR
        } else {
            return reading
        }
    }
}