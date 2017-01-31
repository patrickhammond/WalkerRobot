package com.madebyatomicrobot.things

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.google.android.things.pio.PeripheralManagerService
import com.google.android.things.pio.Pwm
import java.io.IOException


class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private val DEFAULT_FREQUENCY_HZ = 50.0

        private val DEFAULT_MIN_PULSE_DURATION_MS = 0.5
        private val DEFAULT_MAX_PULSE_DURATION_MS = 2.5

        private val DEFAULT_MIN_ANGLE_DEG = 0
        private val DEFAULT_MAX_ANGLE_DEG = 180.0
    }

    lateinit var angle1View: SeekBar
    lateinit var angle2View: SeekBar

    lateinit var pwm0: Pwm
    lateinit var pwm1: Pwm

    private var minPulseDuration = DEFAULT_MIN_PULSE_DURATION_MS
    private var maxPulseDuration = DEFAULT_MAX_PULSE_DURATION_MS

    private var minAngle = DEFAULT_MIN_ANGLE_DEG
    private var maxAngle = DEFAULT_MAX_ANGLE_DEG

    private var period = 1000.0 / DEFAULT_FREQUENCY_HZ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        angle1View = findViewById(R.id.angle1) as SeekBar
        angle2View = findViewById(R.id.angle2) as SeekBar

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "PWM: " + manager.pwmList)
            pwm0 = manager.openPwm("PWM0")
            pwm0.setEnabled(true)
            pwm0.setPwmFrequencyHz(DEFAULT_FREQUENCY_HZ)

            pwm1 = manager.openPwm("PWM1")
            pwm1.setEnabled(true)
            pwm1.setPwmFrequencyHz(DEFAULT_FREQUENCY_HZ)
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onCreate", ex)
        }

        angle1View.max = (maxAngle - minAngle).toInt()
        angle2View.max = (maxAngle - minAngle).toInt()

        angle1View.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                moveToAngle(pwm0, progress.toDouble() + minAngle)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }
        })

        angle2View.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                moveToAngle(pwm1, progress.toDouble() + minAngle)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }
        })

        angle1View.progress = 90.0.toInt()
        angle2View.progress = 90.0.toInt()
    }

    fun moveToAngle(pwm: Pwm, angle: Double) {
        updateDutyCycle(pwm, angle)
    }

    fun updateDutyCycle(pwm: Pwm, angle: Double) {
        val t = (angle - minAngle) / (maxAngle - minAngle)
        val pw = minPulseDuration + (maxPulseDuration - minPulseDuration) * t
        val dutyCycle = 100.0 * pw / period
        pwm.setPwmDutyCycle(dutyCycle)
    }

    override fun onDestroy() {
        try {
            pwm0.close()
            pwm1.close()
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onDestroy", ex)
        }

        super.onDestroy()
    }
}
