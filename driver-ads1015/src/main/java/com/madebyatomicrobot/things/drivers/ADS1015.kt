package com.madebyatomicrobot.things.drivers

import com.google.android.things.pio.I2cDevice

/**
 * https://github.com/adafruit/Adafruit_Python_ADS1x15/blob/master/Adafruit_ADS1x15/ADS1x15.py
 * https://github.com/adafruit/Adafruit_ADS1X15/blob/master/Adafruit_ADS1015.cpp
 *
 * @see <a href="http://www.ti.com/lit/ds/symlink/ads1015.pdf">http://www.ti.com/lit/ds/symlink/ads1015.pdf</a>
 */
class ADS1015(val i2c: I2cDevice) {

    companion object {
        // Default from datasheet page 19, config register DR bit default.
        val DATA_RATE_DEFAULT = 1600

        val ADS1x15_DEFAULT_ADDRESS = 0x48
        val ADS1x15_POINTER_CONVERSION = 0x00
        val ADS1x15_POINTER_CONFIG = 0x01
        val ADS1x15_POINTER_LOW_THRESHOLD = 0x02
        val ADS1x15_POINTER_HIGH_THRESHOLD = 0x03
        val ADS1x15_CONFIG_OS_SINGLE = 0x8000
        val ADS1x15_CONFIG_MUX_OFFSET = 12

        val ADS1x15_CONFIG_MODE_CONTINUOUS = 0x0000
        val ADS1x15_CONFIG_MODE_SINGLE = 0x0100

        val ADS1015_CONFIG_DR = mapOf(
                128 to 0x0000,
                250 to 0x0020,
                490 to 0x0040,
                920 to 0x0060,
                1600 to 0x0080,
                2400 to 0x00A0,
                3300 to 0x00C0
        )

        val ADS1x15_CONFIG_COMP_WINDOW = 0x0010
        val ADS1x15_CONFIG_COMP_ACTIVE_HIGH = 0x0008
        val ADS1x15_CONFIG_COMP_LATCHING = 0x0004

        val ADS1x15_CONFIG_COMP_QUE = mapOf(
                1 to 0x0000,
                2 to 0x0001,
                4 to 0x0002
        )

        val ADS1x15_CONFIG_COMP_QUE_DISABLE = 0x0003
    }

    fun dataRateConfig(dataRate: Int) {
        ADS1015_CONFIG_DR[dataRate]
    }

    fun conversionValue(low: Int, high: Int): Int {
        var value = ((high.and(0xFF).shl(4)).or((low.and(0xFF)).shr(4)))
        if (value.and(0x800) != 0) {
            value -= 1.shl(12)
        }
        return value
    }

    fun readADC(channel: Int, gain: Int = 1, dataRate: Int? = null) {

    }

    fun startADC(channel: Int, gain: Int = 1, dataRate: Int? = null) {

    }

    fun stopADC() {

    }

    fun getLastResult() {

    }
}