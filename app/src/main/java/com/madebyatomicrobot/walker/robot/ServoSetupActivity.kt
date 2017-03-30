package com.madebyatomicrobot.walker.robot

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
import com.madebyatomicrobot.things.drivers.PCA9685
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException

class ServoSetupActivity : Activity() {
    companion object {
        private val TAG = ServoSetupActivity::class.java.simpleName
    }

    lateinit var disposables: CompositeDisposable

    lateinit var i2c: I2cDevice
    lateinit var pca9685: PCA9685

    lateinit var servo00: Servo
    lateinit var servo01: Servo
    lateinit var servo02: Servo
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
    lateinit var servo13: Servo
    lateinit var servo14: Servo
    lateinit var servo15: Servo

    lateinit var rightAnkle: Servo
    lateinit var rightKnee: Servo
    lateinit var rightHipBottom: Servo
    lateinit var rightHipMiddle: Servo
    lateinit var rightHipTop: Servo

    lateinit var leftAnkle: Servo
    lateinit var leftKnee: Servo
    lateinit var leftHipBottom: Servo
    lateinit var leftHipMiddle: Servo
    lateinit var leftHipTop: Servo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servo_setup)

        disposables = CompositeDisposable()

        val manager = PeripheralManagerService()
        try {
            Log.i(TAG, "I2C: " + manager.i2cBusList)
            i2c = manager.openI2cDevice("I2C1", 0x40)

            pca9685 = PCA9685(i2c)
            pca9685.resetI2C()
            pca9685.setPWMFreq(50.0)  // 50 Hz


            val defaultFeetAngle = 90.0
            val defaultAnkleAngle = 90.0
            val defaultKneeAngle = 90.0
            val defaultHipTwistAngle = 90.0
            val defaultHipTiltAngle = 90.0

            servo00 = Servo(pca9685, 0, defaultAngle = defaultFeetAngle)
            servo01 = Servo(pca9685, 1, defaultAngle = defaultAnkleAngle)
            servo02 = Servo(pca9685, 2)  // ignore
            servo03 = Servo(pca9685, 3)  // ignore
            servo04 = Servo(pca9685, 4)  // ignore
            servo05 = Servo(pca9685, 5, defaultAngle = defaultHipTiltAngle)
            servo06 = Servo(pca9685, 6, defaultAngle = defaultHipTwistAngle)
            servo07 = Servo(pca9685, 7, defaultAngle = defaultKneeAngle)

            servo08 = Servo(pca9685, 8, defaultAngle = defaultKneeAngle, invertAngle = true)
            servo09 = Servo(pca9685, 9, defaultAngle = defaultHipTwistAngle)
            servo10 = Servo(pca9685, 10, defaultAngle = defaultHipTiltAngle)
            servo11 = Servo(pca9685, 11)  // ignore
            servo12 = Servo(pca9685, 12)  // ignore
            servo13 = Servo(pca9685, 13)  // ignore
            servo14 = Servo(pca9685, 14, defaultAngle = defaultAnkleAngle, invertAngle = true)
            servo15 = Servo(pca9685, 15, defaultAngle = defaultFeetAngle, invertAngle = true)

            rightAnkle = servo01
            rightKnee = servo00
            rightHipBottom = servo05
            rightHipMiddle = servo06
            rightHipTop = servo07

            leftAnkle = servo14
            leftKnee = servo15
            leftHipBottom = servo10
            leftHipMiddle = servo09
            leftHipTop = servo08
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onCreate", ex)
        }

        setupServoSeekBar("00", R.id.angle_00, R.id.angle_label_00, servo00, true)
        setupServoSeekBar("01", R.id.angle_01, R.id.angle_label_01, servo01, true)
        setupServoSeekBar("02", R.id.angle_02, R.id.angle_label_02, servo02, false)
        setupServoSeekBar("03", R.id.angle_03, R.id.angle_label_03, servo03, false)
        setupServoSeekBar("04", R.id.angle_04, R.id.angle_label_04, servo04, false)
        setupServoSeekBar("05", R.id.angle_05, R.id.angle_label_05, servo05, true)
        setupServoSeekBar("06", R.id.angle_06, R.id.angle_label_06, servo06, true)
        setupServoSeekBar("07", R.id.angle_07, R.id.angle_label_07, servo07, true)

        setupServoSeekBar("08", R.id.angle_08, R.id.angle_label_08, servo08, true)
        setupServoSeekBar("09", R.id.angle_09, R.id.angle_label_09, servo09, true)
        setupServoSeekBar("10", R.id.angle_10, R.id.angle_label_10, servo10, true)
        setupServoSeekBar("11", R.id.angle_11, R.id.angle_label_11, servo11, false)
        setupServoSeekBar("12", R.id.angle_12, R.id.angle_label_12, servo12, false)
        setupServoSeekBar("13", R.id.angle_13, R.id.angle_label_13, servo13, false)
        setupServoSeekBar("14", R.id.angle_14, R.id.angle_label_14, servo14, true)
        setupServoSeekBar("15", R.id.angle_15, R.id.angle_label_15, servo15, true)

        setupServoPairSeekBar(R.id.angle_00_pair, servo00, servo15, true)
        setupServoPairSeekBar(R.id.angle_01_pair, servo01, servo14, true)
        setupServoPairSeekBar(R.id.angle_02_pair, servo02, servo13, false)
        setupServoPairSeekBar(R.id.angle_03_pair, servo03, servo12, false)
        setupServoPairSeekBar(R.id.angle_04_pair, servo04, servo11, false)
        setupServoPairSeekBar(R.id.angle_05_pair, servo05, servo10, true)
        setupServoPairSeekBar(R.id.angle_06_pair, servo06, servo09, true)
        setupServoPairSeekBar(R.id.angle_07_pair, servo07, servo08, true)

        findViewById(R.id.reset).setOnClickListener { reset() }
        findViewById(R.id.step_left).setOnClickListener { stepLeft() }
        findViewById(R.id.step_right).setOnClickListener { stepRight() }
    }

    private fun setupServoSeekBar(
            name: String,
            seekBarResId: Int,
            seekBarLabelResId: Int,
            servo: Servo,
            enabled: Boolean) {
        val seekBar = findViewById(seekBarResId) as SeekBar
        val label = findViewById(seekBarLabelResId) as TextView

        seekBar.isEnabled = enabled
        seekBar.max = 180
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val angle = seekBar!!.progress
                label.text = "$name ($angle)"
                servo.moveToAngle(angle.toDouble())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        disposables.add(servo.getObservableAngle().subscribe({ angle -> seekBar.progress = angle.toInt() }))
    }

    private fun setupServoPairSeekBar(
            seekBarResId: Int,
            leftServo: Servo,
            rightSero: Servo,
            enabled: Boolean) {
        val seekBar = findViewById(seekBarResId) as SeekBar

        seekBar.isEnabled = enabled
        seekBar.max = 180
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val angle = seekBar!!.progress
                leftServo.moveToAngle(angle.toDouble())
                rightSero.moveToAngle(angle.toDouble())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Don't care
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        seekBar.progress = leftServo.getAngle().toInt()
    }

    private fun reset() {
        servo00.moveToDefaultAngle()
        servo01.moveToDefaultAngle()
        servo02.moveToDefaultAngle()
        servo03.moveToDefaultAngle()
        servo04.moveToDefaultAngle()
        servo05.moveToDefaultAngle()
        servo06.moveToDefaultAngle()
        servo07.moveToDefaultAngle()
        servo08.moveToDefaultAngle()
        servo09.moveToDefaultAngle()
        servo10.moveToDefaultAngle()
        servo11.moveToDefaultAngle()
        servo12.moveToDefaultAngle()
        servo13.moveToDefaultAngle()
        servo14.moveToDefaultAngle()
        servo15.moveToDefaultAngle()
    }

    private fun stepLeft() {
        step(false)
    }

    private fun stepRight() {
        step(true)
    }

    private fun step(inverse: Boolean) {
        val leftHipTopAngle = (if (inverse) leftHipTop else rightHipTop).getAngle().toFloat()
        val leftHipMiddleAngle = (if (inverse) leftHipMiddle else rightHipMiddle).getAngle().toFloat()
        val leftHipBottomAngle = (if (inverse) leftHipBottom else rightHipBottom).getAngle().toFloat()
        val leftKneeAngle = (if (inverse) leftKnee else rightKnee).getAngle().toFloat()
        val leftAnkleAngle = (if (inverse) leftAnkle else rightAnkle).getAngle().toFloat()

        val rightHipTopAngle = (if (inverse) rightHipTop else leftHipTop).getAngle().toFloat()
        val rightHipMiddleAngle = (if (inverse) rightHipMiddle else leftHipMiddle).getAngle().toFloat()
        val rightHipBottomAngle = (if (inverse) rightHipBottom else leftHipBottom).getAngle().toFloat()
        val rightKneeAngle = (if (inverse) rightKnee else leftKnee).getAngle().toFloat()
        val rightAnkleAngle = (if (inverse) rightAnkle else leftAnkle).getAngle().toFloat()

        val leftHipTopAnimator = ValueAnimator.ofFloat(leftHipTopAngle, leftHipTopAngle - 20, leftHipTopAngle).setDuration(1500)
        leftHipTopAnimator.addUpdateListener({ (if (inverse) leftHipTop else rightHipTop).moveToAngle(it.animatedValue as Float) })

        val leftHipMiddleAnimator = ValueAnimator.ofFloat(leftHipMiddleAngle, leftHipMiddleAngle - 20, leftHipMiddleAngle).setDuration(1500)
        leftHipMiddleAnimator.addUpdateListener { (if (inverse) leftHipMiddle else rightHipMiddle).moveToAngle(it.animatedValue as Float) }

        val leftHipBottomAnimator = ValueAnimator.ofFloat(leftHipBottomAngle, leftHipBottomAngle + 25, leftHipBottomAngle).setDuration(1500)
        leftHipBottomAnimator.addUpdateListener { (if (inverse) leftHipBottom else rightHipBottom).moveToAngle(it.animatedValue as Float) }

        val leftKneeAnimator = ValueAnimator.ofFloat(leftKneeAngle, leftKneeAngle - 25, leftKneeAngle).setDuration(1500)
        leftKneeAnimator.addUpdateListener { (if (inverse) leftKnee else rightKnee).moveToAngle(it.animatedValue as Float) }

        val leftAnkleAnimator = ValueAnimator.ofFloat(leftAnkleAngle, leftAnkleAngle - 30, leftAnkleAngle).setDuration(1500)
        leftAnkleAnimator.addUpdateListener { (if (inverse) leftAnkle else rightAnkle).moveToAngle(it.animatedValue as Float) }




        val rightHipTopAnimator = ValueAnimator.ofFloat(rightHipTopAngle, rightHipTopAngle - 20, rightHipTopAngle).setDuration(1500)
        rightHipTopAnimator.addUpdateListener({ (if (inverse) rightHipTop else leftHipTop).moveToAngle(it.animatedValue as Float) })

        val rightHipMiddleAnimator = ValueAnimator.ofFloat(rightHipMiddleAngle, rightHipMiddleAngle - 20, rightHipMiddleAngle).setDuration(1500)
        rightHipMiddleAnimator.addUpdateListener { (if (inverse) rightHipMiddle else leftHipMiddle).moveToAngle(it.animatedValue as Float) }

        val rightHipBottomAnimator = ValueAnimator.ofFloat(rightHipBottomAngle, rightHipBottomAngle - 25, rightHipBottomAngle).setDuration(3000)
        rightHipBottomAnimator.addUpdateListener { (if (inverse) rightHipBottom else leftHipBottom).moveToAngle(it.animatedValue as Float) }

        val rightKneeAnimator = ValueAnimator.ofFloat(rightKneeAngle, rightKneeAngle + 20, rightKneeAngle).setDuration(3000)
        rightKneeAnimator.addUpdateListener { (if (inverse) rightKnee else leftKnee).moveToAngle(it.animatedValue as Float) }

        val rightAnkleAnimator = ValueAnimator.ofFloat(rightAnkleAngle, rightAnkleAngle + 50, rightAnkleAngle).setDuration(3000)
        rightAnkleAnimator.addUpdateListener { (if (inverse) rightAnkle else leftAnkle).moveToAngle(it.animatedValue as Float) }



        leftHipTopAnimator.start()
        rightHipTopAnimator.start()

        leftHipMiddleAnimator.start()
        rightHipMiddleAnimator.start()

        //leftHipBottomAngle.toString()
        rightHipBottomAnimator.start()

        //leftKneeAnimator.start()
        rightKneeAnimator.start()

        //leftAnkleAnimator.start()
        rightAnkleAnimator.start()
    }

    override fun onDestroy() {
        disposables.clear()

        try {
            i2c.close()
        } catch (ex: IOException) {
            Log.w(TAG, "Error in onDestroy", ex)
        }

        super.onDestroy()
    }
}
