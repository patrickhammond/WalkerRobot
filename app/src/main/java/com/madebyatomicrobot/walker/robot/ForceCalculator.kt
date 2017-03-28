package com.madebyatomicrobot.walker.robot;

class ForceCalculator() {
    data class Force(val angle: Double, val magnitude: Double)

    fun calculateForce(readingX1: Int, readingX2: Int, readingY1: Int, readingY2: Int): Force {
        val xInt = (-readingX1 + readingX2)
        val yInt = (-readingY1 + readingY2)
        val xDouble = xInt.toDouble()
        val yDouble = yInt.toDouble()

        var angle = Math.toDegrees(Math.atan(yDouble / xDouble))
        @Suppress("ReplaceCallWithComparison")
        if (angle.equals(-0.0)) {  // DO NOT USE == because we can get a signed zero (-0.0)
            angle = 180.0
        }
        val magnitude = Math.sqrt(xDouble * xDouble + yDouble * yDouble)
        return Force(angle, magnitude)
    }
}
