package com.madebyatomicrobot.walker.robot

import org.junit.Assert.assertEquals
import org.junit.Test

class ForceCalculatorTest {
    @Test fun testAllZeros() {
        val calculator = ForceCalculator()
        val force = calculator.calculateForce(0, 0, 0, 0)
        assertEquals(0.0, force.magnitude, 0.00001)
        assertEquals(Double.NaN, force.angle, 0.00001)
    }

    @Test fun testRight() {
        val calculator = ForceCalculator()
        val force = calculator.calculateForce(0, 100, 0, 0)
        assertEquals(100.0, force.magnitude, 0.00001)
        assertEquals(0.0, force.angle, 0.00001)
    }

    @Test fun testFront() {
        val calculator = ForceCalculator()
        val force = calculator.calculateForce(0, 0, 100, 0)
        assertEquals(100.0, force.magnitude, 0.00001)
        assertEquals(-90.0, force.angle, 0.00001)
    }

    @Test fun testLeft() {
        val calculator = ForceCalculator()
        val force = calculator.calculateForce(100, 0, 0, 0)
        assertEquals(100.0, force.magnitude, 0.00001)
        assertEquals(180.0, force.angle, 0.00001)
    }

    @Test fun testBack() {
        val calculator = ForceCalculator()
        val force = calculator.calculateForce(0, 0, 0, 100)
        assertEquals(100.0, force.magnitude, 0.00001)
        assertEquals(90.0, force.angle, 0.00001)
    }
}