package com.madebyatomicrobot.things

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
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

    lateinit var servo03: Servo
    lateinit var servo04: Servo
    lateinit var servo05: Servo
    lateinit var servo06: Servo
    lateinit var servo07: Servo

    lateinit var servo08: Servo
    lateinit var servo09: Servo
    lateinit var servo10: Servo
    lateinit var servo11: Servo
    lateinit var servo12: Servo

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

            servo03 = Servo(pca9685, 3)
            servo04 = Servo(pca9685, 4)
            servo05 = Servo(pca9685, 5)
            servo06 = Servo(pca9685, 6)
            servo07 = Servo(pca9685, 7)

            servo08 = Servo(pca9685, 8, invertAngle = false)
            servo09 = Servo(pca9685, 9, invertAngle = false)
            servo10 = Servo(pca9685, 10, invertAngle = true, adjustmentAngle = -2.0)
            servo11 = Servo(pca9685, 11, invertAngle = true, adjustmentAngle = 3.0)
            servo12 = Servo(pca9685, 12, invertAngle = true, adjustmentAngle = 5.0)

            leftLeg = Leg(servo07, servo06, servo05, servo04, servo03)
            rightLeg = Leg(servo08, servo09, servo10, servo11, servo12)
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onCreate", ex)
        }

        findViewById(R.id.leg_reset).setOnClickListener { legReset() }
        findViewById(R.id.leg_down).setOnClickListener { legDown() }
        findViewById(R.id.leg_up).setOnClickListener { legUp() }
        findViewById(R.id.left_step).setOnClickListener { leftStep() }
        findViewById(R.id.right_step).setOnClickListener { rightStep() }

        setupSeekBar(R.id.hip_y_angle, Leg.defaultHipYAngle, fun(angle) {
//            servo07.moveToAngle(angle)
//            servo08.moveToAngle(angle)
            leftLeg.moveHipYAngle(angle)
            rightLeg.moveHipYAngle(angle)
        })
        setupSeekBar(R.id.hip_x_angle, Leg.defaultHipXAngle, fun(angle) {
//            servo06.moveToAngle(angle)
//            servo09.moveToAngle(angle)
            leftLeg.moveHipXAngle(angle)
            rightLeg.moveHipXAngle(angle)
        })
        setupSeekBar(R.id.hip_z_angle, Leg.defaultHipZAngle, fun(angle) {
//            servo05.moveToAngle(angle)
//            servo10.moveToAngle(angle)
            leftLeg.moveHipZAngle(angle)
            rightLeg.moveHipZAngle(angle)
        })
        setupSeekBar(R.id.knee_angle, Leg.defaultKneeAngle, fun(angle) {
//            servo04.moveToAngle(angle)
//            servo11.moveToAngle(angle)
            leftLeg.moveKneeAngle(angle)
            rightLeg.moveKneeAngle(angle)
        })
        setupSeekBar(R.id.ankle_angle, Leg.defaultAnkleAngle, fun(angle) {
            //servo03.moveToAngle(angle)
            //servo12.moveToAngle(angle)
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
        Thread({
            leftLeg.adjustHipYAngle(-10.0)
            rightLeg.adjustHipYAngle(-10.0)
            updateLabels()
            Thread.sleep(250)

            rightLeg.up()
            rightLeg.up()
            rightLeg.up()
            rightLeg.up()
            rightLeg.up()
            updateLabels()
            Thread.sleep(250)

            leftLeg.adjustHipXAngle(10.0)
            rightLeg.adjustHipXAngle(10.0)
            updateLabels()
            Thread.sleep(250)

//            leftLeg.down()
//            leftLeg.down()
//            updateLabels()
//            Thread.sleep(250)

            leftLeg.adjustHipYAngle(10.0)
            rightLeg.adjustHipYAngle(10.0)
            updateLabels()
            Thread.sleep(250)

//            leftLeg.adjustHipXAngle(10.0)
//            rightLeg.adjustHipXAngle(10.0)
//            updateLabels()
        }).run()
    }

    fun rightStep() {

    }

    class Leg(val hipY: Servo, val hipX: Servo, val hipZ: Servo, val knee: Servo, val ankle: Servo) {
        companion object {
            val defaultAngleAdjust = 2.0

            val defaultHipYAngle = 90.0
            val defaultHipXAngle = 90.0
            val defaultHipZAngle = 35.0
            val defaultKneeAngle = 70.0
            val defaultAnkleAngle = 45.0
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
