package com.madebyatomicrobot.walker.robot

import android.animation.ValueAnimator
import com.madebyatomicrobot.walker.connector.data.Actions
import com.madebyatomicrobot.walker.connector.data.Command

class Walker(servos: PhysicalServos) {
    private var actions: Actions? = null

    private val rightAnkle: Servo = servos.servo01
    private val rightKnee: Servo = servos.servo00
    private val rightHipBottom: Servo = servos.servo05
    private val rightHipMiddle: Servo = servos.servo06
    private val rightHipTop: Servo = servos.servo07
    private val leftAnkle: Servo = servos.servo14
    private val leftKnee: Servo = servos.servo15
    private val leftHipBottom: Servo = servos.servo10
    private val leftHipMiddle: Servo = servos.servo09
    private val leftHipTop: Servo = servos.servo08

    private var animators: Animators? = null

    fun handleActions(actions: Actions) {
        this.actions = actions
    }

    fun handleCommand(command: Command) {
        animators?.stop()

        when(command.current) {
            Command.RESET -> reset()
            Command.STOPPED -> pause()
            Command.FORWARD -> stepForward()
            else -> reset()
        }
    }

    private fun reset() {
        animators = Animators(
                setupServoForReset(leftHipTop),
                setupServoForReset(leftHipMiddle),
                setupServoForReset(leftHipBottom),
                setupServoForReset(leftKnee),
                setupServoForReset(leftAnkle),
                setupServoForReset(rightHipTop),
                setupServoForReset(rightHipMiddle),
                setupServoForReset(rightHipBottom),
                setupServoForReset(rightKnee),
                setupServoForReset(rightAnkle))
        animators!!.start()
    }

    private fun setupServoForReset(servo: Servo): ValueAnimator {
        val angle = servo.getAngle().toFloat()
        val movements: FloatArray = arrayOf(angle, 0.0.toFloat()).toFloatArray()

        return buildAnimator(
                servo,
                movements,
                1000)
    }

    private fun pause() {
        animators?.stop()
    }

    private fun stepForward(inverse: Boolean = false) {
        animators = Animators(
                setupServoForWalking(leftHipTop, rightHipTop, inverse, actions!!.walk.left.hipY),
                setupServoForWalking(leftHipMiddle, rightHipMiddle, inverse, actions!!.walk.left.hipX),
                setupServoForWalking(leftHipBottom, rightHipBottom, inverse, actions!!.walk.left.hipZ),
                setupServoForWalking(leftKnee, rightKnee, inverse, actions!!.walk.left.knee),
                setupServoForWalking(leftAnkle, rightAnkle, inverse, actions!!.walk.left.ankle),
                setupServoForWalking(rightHipTop, leftHipTop, inverse, actions!!.walk.right.hipY),
                setupServoForWalking(rightHipMiddle, leftHipMiddle, inverse, actions!!.walk.right.hipX),
                setupServoForWalking(rightHipBottom, leftHipBottom, inverse, actions!!.walk.right.hipZ),
                setupServoForWalking(rightKnee, leftKnee, inverse, actions!!.walk.right.knee),
                setupServoForWalking(rightAnkle, leftAnkle, inverse, actions!!.walk.right.ankle))
        animators!!.start()
    }

    private fun setupServoForWalking(
            left: Servo,
            right: Servo,
            inverse: Boolean,
            config: Actions.ServoConfig): ValueAnimator {
        val angle = (if (inverse) left else right).getAngle().toFloat()
        val movements: FloatArray = config.action.split(",")
                .map(String::toFloat)
                .map { angle + it }
                .toFloatArray()

        return buildAnimator(
                (if (inverse) left else right),
                movements,
                config.duration.toLong())
    }

    private fun buildAnimator(servo: Servo, movements: FloatArray, duration: Long): ValueAnimator {
        val animator = ValueAnimator.ofFloat(*movements).setDuration(duration)
        animator.addUpdateListener({ servo.moveToAngle(it.animatedValue as Float) })
        return animator
    }

    private class Animators(
            val leftHipTopAnimator: ValueAnimator,
            val leftHipMiddleAnimator: ValueAnimator,
            val leftHipBottomAnimator: ValueAnimator,
            val leftKneeAnimator: ValueAnimator,
            val leftAnkleAnimator: ValueAnimator,
            val rightHipTopAnimator: ValueAnimator,
            val rightHipMiddleAnimator: ValueAnimator,
            val rightHipBottomAnimator: ValueAnimator,
            val rightKneeAnimator: ValueAnimator,
            val rightAnkleAnimator: ValueAnimator) {

        fun start() {
            leftHipTopAnimator.start()
            leftHipMiddleAnimator.start()
            leftHipBottomAnimator.start()
            leftKneeAnimator.start()
            leftAnkleAnimator.start()

            rightHipTopAnimator.start()
            rightHipMiddleAnimator.start()
            rightHipBottomAnimator.start()
            rightKneeAnimator.start()
            rightAnkleAnimator.start()
        }

        fun stop() {
            leftHipTopAnimator.cancel()
            leftHipMiddleAnimator.cancel()
            leftHipBottomAnimator.cancel()
            leftKneeAnimator.cancel()
            leftAnkleAnimator.cancel()

            rightHipTopAnimator.cancel()
            rightHipMiddleAnimator.cancel()
            rightHipBottomAnimator.cancel()
            rightKneeAnimator.cancel()
            rightAnkleAnimator.cancel()
        }
    }
}
