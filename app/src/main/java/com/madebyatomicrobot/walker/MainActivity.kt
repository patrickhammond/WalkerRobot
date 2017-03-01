package com.madebyatomicrobot.walker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
import com.madebyatomicrobot.walker.PCA9685
import com.madebyatomicrobot.walker.things.R
import com.madebyatomicrobot.walker.Servo
import java.io.IOException


class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    lateinit var hipYAngleLabel: TextView
    lateinit var hipXAngleLabel: TextView
    lateinit var hipZAngleLabel: TextView
    lateinit var kneeAngleLabel: TextView
    lateinit var ankleAngleLabel: TextView

    lateinit var i2c: I2cDevice

    lateinit var servo00: Servo
    lateinit var servo01: Servo
    lateinit var servo05: Servo
    lateinit var servo06: Servo
    lateinit var servo07: Servo

    lateinit var servo08: Servo
    lateinit var servo09: Servo
    lateinit var servo10: Servo
    lateinit var servo14: Servo
    lateinit var servo15: Servo

    lateinit var leftLeg: Leg
    lateinit var rightLeg: Leg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        hipYAngleLabel = findViewById(R.id.hip_y_angle_label) as TextView
        hipXAngleLabel = findViewById(R.id.hip_x_angle_label) as TextView
        hipZAngleLabel = findViewById(R.id.hip_z_angle_label) as TextView
        kneeAngleLabel = findViewById(R.id.knee_angle_label) as TextView
        ankleAngleLabel = findViewById(R.id.ankle_angle_label) as TextView

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "I2C: " + manager.i2cBusList)
            i2c = manager.openI2cDevice("I2C1", 0x40)

            val pca9685 = PCA9685(i2c)
            pca9685.resetI2C()
            pca9685.setPWMFreq(50.0)  // 50 Hz

            servo00 = Servo(pca9685, 0)
            servo01 = Servo(pca9685, 1)
            servo05 = Servo(pca9685, 5)
            servo06 = Servo(pca9685, 6, adjustmentAngle = 5.0)
            servo07 = Servo(pca9685, 7, adjustmentAngle = -4.5)

            servo08 = Servo(pca9685, 8)
            servo09 = Servo(pca9685, 9, adjustmentAngle = -5.0)
            servo10 = Servo(pca9685, 10, invertAngle = true)
            servo14 = Servo(pca9685, 14, invertAngle = true)
            servo15 = Servo(pca9685, 15, invertAngle = true)

            leftLeg = Leg(servo07, servo06, servo05, servo00, servo01)
            rightLeg = Leg(servo08, servo09, servo10, servo15, servo14)
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onCreate", ex)
        }

        findViewById(R.id.leg_reset).setOnClickListener { legReset() }
        findViewById(R.id.leg_down).setOnClickListener { legDown() }
        findViewById(R.id.leg_up).setOnClickListener { legUp() }
        findViewById(R.id.left_step).setOnClickListener { leftStep() }
        findViewById(R.id.right_step).setOnClickListener { rightStep() }

        setupSeekBar(R.id.hip_y_angle, Leg.defaultHipYAngle, fun(angle) {
            leftLeg.moveHipYAngle(angle)
            rightLeg.moveHipYAngle(angle)
        })
        setupSeekBar(R.id.hip_x_angle, Leg.defaultHipXAngle, fun(angle) {
            leftLeg.moveHipXAngle(angle)
            rightLeg.moveHipXAngle(angle)
        })
        setupSeekBar(R.id.hip_z_angle, Leg.defaultHipZAngle, fun(angle) {
            leftLeg.moveHipZAngle(angle)
            rightLeg.moveHipZAngle(angle)
        })
        setupSeekBar(R.id.knee_angle, Leg.defaultKneeAngle, fun(angle) {
            leftLeg.moveKneeAngle(angle)
            rightLeg.moveKneeAngle(angle)
        })
        setupSeekBar(R.id.ankle_angle, Leg.defaultAnkleAngle, fun(angle) {
            leftLeg.moveAnkleAngle(angle)
            rightLeg.moveAnkleAngle(angle)
        })

    }

    private fun setupSeekBar(
            seekBarResId: Int,
            defaultAngle: Double,
            action: (angle: Double) -> Unit) {
        val seekBar = findViewById(seekBarResId) as SeekBar
        seekBar.max = 180
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val angle = seekBar!!.progress
                action(angle.toDouble())
                updateLabels()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        seekBar.progress = defaultAngle.toInt()
    }

    private fun updateLabels() {
        hipYAngleLabel.text = "Hip-Y: ${leftLeg.hipYAngle}, ${rightLeg.hipYAngle}"
        hipXAngleLabel.text = "Hip-X: ${leftLeg.hipXAngle}, ${rightLeg.hipXAngle}"
        hipZAngleLabel.text = "Hip-Z: ${leftLeg.hipZAngle}, ${rightLeg.hipZAngle}"
        kneeAngleLabel.text = "Knee: ${leftLeg.kneeAngle}, ${rightLeg.kneeAngle}"
        ankleAngleLabel.text = "Ankle: ${leftLeg.ankleAngle}, ${rightLeg.ankleAngle}"
    }

    override fun onDestroy() {
        try {
            i2c.close()
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onDestroy", ex)
        }

        super.onDestroy()
    }

    fun legReset() {
        leftLeg.reset()
        rightLeg.reset()
        updateLabels()
    }

    fun legDown() {
        leftLeg.down()
        rightLeg.down()
        updateLabels()
    }

    fun legUp() {
        leftLeg.up()
        rightLeg.up()
        updateLabels()
    }

    fun leftStep() {

    }

    fun rightStep() {

    }

    class Leg(val hipY: Servo, val hipX: Servo, val hipZ: Servo, val knee: Servo, val ankle: Servo) {
        companion object {
            val defaultAngleAdjust = 2.0

            val defaultHipYAngle = 90.0
            val defaultHipXAngle = 90.0
            val defaultHipZAngle = 105.0
            val defaultKneeAngle = 150.0
            val defaultAnkleAngle = 135.0
        }

        var hipYAngle = defaultHipYAngle
        var hipXAngle = defaultHipXAngle
        var hipZAngle = defaultHipZAngle
        var kneeAngle = defaultKneeAngle
        var ankleAngle = defaultAnkleAngle

        fun reset() {
            hipYAngle = defaultHipYAngle
            hipXAngle = defaultHipXAngle
            hipZAngle = defaultHipZAngle
            kneeAngle = defaultKneeAngle
            ankleAngle = defaultAnkleAngle
            moveLeg()
        }

        fun up() {
            hipZAngle -= defaultAngleAdjust
            kneeAngle -= defaultAngleAdjust
            ankleAngle += defaultAngleAdjust
            moveLeg()
        }

        fun down() {
            hipZAngle += defaultAngleAdjust
            kneeAngle += defaultAngleAdjust
            ankleAngle -= defaultAngleAdjust
            moveLeg()
        }

        fun adjustHipYAngle(angleAdjust: Double) {
            hipZAngle += angleAdjust
            moveLeg()
        }

        fun adjustHipXAngle(angleAdjust: Double) {
            hipXAngle += angleAdjust
            moveLeg()
        }

        fun moveHipYAngle(angle: Double) {
            hipYAngle = angle
            moveLeg()
        }

        fun moveHipXAngle(angle: Double) {
            hipXAngle = angle
            moveLeg()
        }

        fun moveHipZAngle(angle: Double) {
            hipZAngle = angle
            moveLeg()
        }

        fun moveKneeAngle(angle: Double) {
            kneeAngle = angle
            moveLeg()
        }

        fun moveAnkleAngle(angle: Double) {
            ankleAngle = angle
            moveLeg()
        }

        fun moveLeg() {
            hipY.moveToAngle(hipYAngle)
            hipX.moveToAngle(hipXAngle)
            hipZ.moveToAngle(hipZAngle)
            knee.moveToAngle(kneeAngle)
            ankle.moveToAngle(ankleAngle)
        }
    }
}
