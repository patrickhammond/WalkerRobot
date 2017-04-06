package com.madebyatomicrobot.walker.robot

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.Actions
import com.madebyatomicrobot.walker.connector.data.Command

class Robot(servos: PhysicalServos) {
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

    private var command: Command? = null
    private var leftStep = false

    fun handleActions(actions: Actions) {
        this.actions = actions
    }

    fun handleCommand(command: Command) {
        animators?.stop()
        this.command = command

        handleCommand()
    }

    private fun handleCommand() {
        when (command!!.current) {
            Command.RESET -> reset({ })
            Command.STOPPED -> pause()
            Command.FORWARD -> reset({ stepForward(leftStep, { switchLegWhileWalking() }) })
            else -> reset({ })
        }
    }

    private fun switchLegWhileWalking() {
        leftStep = !leftStep
        reset({ handleCommand() })
    }

    private fun reset(completeCallback: () -> Unit) {
        Log.v("DEBUG", "action: reset")
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
                setupServoForReset(rightAnkle),
                completeCallback)
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
        Log.v("DEBUG", "action: pause")
        animators?.stop()
    }

    private fun stepForward(inverse: Boolean = false, completeCallback: () -> Unit) {
        Log.v("DEBUG", "action: step")
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
                setupServoForWalking(rightAnkle, leftAnkle, inverse, actions!!.walk.right.ankle),
                completeCallback)
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
            val rightAnkleAnimator: ValueAnimator,
            val animationCompleteCallback: () -> Unit) {

        private val listener: AnimatorListener = object : AnimatorListener {
            private var completedCount = 0

            override fun onAnimationStart(animator: Animator?) {
                // Don't care
            }

            override fun onAnimationRepeat(animator: Animator?) {
                // Don't care
            }

            override fun onAnimationEnd(animator: Animator?) {
                completedCount += 1

                if (completedCount == 10) {  // Because we have 10 animators
                    animationCompleteCallback.invoke()
                }
            }

            override fun onAnimationCancel(animator: Animator?) {
                // Don't care
            }
        }

        init {
            leftHipTopAnimator.addListener(listener)
            leftHipMiddleAnimator.addListener(listener)
            leftHipBottomAnimator.addListener(listener)
            leftKneeAnimator.addListener(listener)
            leftAnkleAnimator.addListener(listener)
            rightHipTopAnimator.addListener(listener)
            rightHipMiddleAnimator.addListener(listener)
            rightHipBottomAnimator.addListener(listener)
            rightKneeAnimator.addListener(listener)
            rightAnkleAnimator.addListener(listener)
        }

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