package com.madebyatomicrobot.things.drivers

import com.google.android.things.pio.I2cDevice

/**
 * @see <a href="http://www.nxp.com/documents/data_sheet/PCA9685.pdf">http://www.nxp.com/documents/data_sheet/PCA9685.pdf</a>
 */
class PCA9685(
        val i2c: I2cDevice,
        var pulseLength: Double = 0.0) {

    companion object {
        val MODE1 = 0x00
        val MODE2 = 0x01

        val SUBADR1 = 0x02
        val SUBADR2 = 0x03
        val SUBADR3 = 0x04

        val PRESCALE = 0xFE

        val LED0_ON_L = 0x06
        val LED0_ON_H = 0x07
        val LED0_OFF_L = 0x08
        val LED0_OFF_H = 0x09

        val ALL_LED_ON_L = 0xFA
        val ALL_LED_ON_H = 0xFB
        val ALL_LED_OFF_L = 0xFC
        val ALL_LED_OFF_H = 0xFD

        val RESTART = 0x80
        val SLEEP = 0x10
        val ALLCALL = 0x01
        val INVRT = 0x10
        val OUTDRV = 0x04
    }

    fun restart() {
        writeByte(MODE1, 0x06)  // Software reset
        Thread.sleep(5)
    }

    fun resetI2C() {
        writeByte(MODE1, 0x0)  // FIXME - MAGIC NUMBER
        Thread.sleep(5)
    }

    fun setPWMFreq(frequency: Double) {
        // 1,000,000 microseconds per second / freq / 12bits (2^12)
        pulseLength = 1000000.0 / frequency / 4096

        // See https://github.com/adafruit/Adafruit-PWM-Servo-Driver-Library/issues/11 for
        // why the frequency is adjusted due to overshoot
        val correctedFrequency = frequency * 0.9

        // See page 25 of the data sheet
        var prescaleval = 25000000.0  // PCA9685 internal clock is 25 MHz
        prescaleval /= 4096f
        prescaleval /= correctedFrequency
        prescaleval -= 1f

        val prescale = Math.round(prescaleval).toInt()

        val oldMode = readByte(MODE1)
        val newMode = oldMode.toInt() and 0x7F or 0x10  // FIXME - MAGIC NUMBER
        writeByte(MODE1, newMode.toByte()) // go to sleep
        writeByte(PRESCALE, prescale.toByte())
        writeByte(MODE1, oldMode)
        Thread.sleep(5)
        writeByte(MODE1, (oldMode.toInt() or 0xA1).toByte())  // FIXME - MAGIC NUMBER
    }

    fun setPwm(channel: Int, on: Float, off: Float) {
        val onPw = (on / pulseLength).toInt()
        val offPw = (off / pulseLength).toInt()

        setPwm(channel, onPw, offPw)

        writeByte(LED0_ON_L + 4 * channel, onPw.toByte())
        writeByte(LED0_ON_H + 4 * channel, (onPw shr 8).toByte())
        writeByte(LED0_OFF_L + 4 * channel, offPw.toByte())
        writeByte(LED0_OFF_H + 4 * channel, (offPw shr 8).toByte())
    }

    fun setPwm(channel: Int, onPw: Int, offPw: Int) {
        writeByte(LED0_ON_L + 4 * channel, onPw.toByte())
        writeByte(LED0_ON_H + 4 * channel, (onPw shr 8).toByte())
        writeByte(LED0_OFF_L + 4 * channel, offPw.toByte())
        writeByte(LED0_OFF_H + 4 * channel, (offPw shr 8).toByte())
    }

    fun readByte(reg: Int): Byte {
        return i2c.readRegByte(reg)
    }

    fun writeByte(reg: Int, value: Byte) {
        i2c.writeRegByte(reg, value)
    }
}