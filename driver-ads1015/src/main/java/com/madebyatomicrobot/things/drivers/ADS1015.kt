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

        val ADS1x15_CONFIG_GAIN = mapOf(
                (2 / 3) to 0x0000,
                1 to 0x0200,
                2 to 0x0400,
                4 to 0x0600,
                8 to 0x0800,
                16 to 0x0A00
        )

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

        // From the C header

        val ADS1015_REG_POINTER_MASK = (0x03)
        val ADS1015_REG_POINTER_CONVERT = (0x00)
        val ADS1015_REG_POINTER_CONFIG = (0x01)
        val ADS1015_REG_POINTER_LOWTHRESH = (0x02)
        val ADS1015_REG_POINTER_HITHRESH = (0x03)

        val ADS1015_REG_CONFIG_OS_MASK = (0x8000)
        val ADS1015_REG_CONFIG_OS_SINGLE = (0x8000)  // Write: Set to start a single-conversion
        val ADS1015_REG_CONFIG_OS_BUSY = (0x0000)  // Read: Bit = 0 when conversion is in progress
        val ADS1015_REG_CONFIG_OS_NOTBUSY = (0x8000)  // Read: Bit = 1 when device is not performing a conversion

        val ADS1015_REG_CONFIG_MUX_MASK = (0x7000)
        val ADS1015_REG_CONFIG_MUX_DIFF_0_1 = (0x0000)  // Differential P = AIN0, N = AIN1 =(default)
        val ADS1015_REG_CONFIG_MUX_DIFF_0_3 = (0x1000)  // Differential P = AIN0, N = AIN3
        val ADS1015_REG_CONFIG_MUX_DIFF_1_3 = (0x2000)  // Differential P = AIN1, N = AIN3
        val ADS1015_REG_CONFIG_MUX_DIFF_2_3 = (0x3000)  // Differential P = AIN2, N = AIN3
        val ADS1015_REG_CONFIG_MUX_SINGLE_0 = (0x4000)  // Single-ended AIN0
        val ADS1015_REG_CONFIG_MUX_SINGLE_1 = (0x5000)  // Single-ended AIN1
        val ADS1015_REG_CONFIG_MUX_SINGLE_2 = (0x6000)  // Single-ended AIN2
        val ADS1015_REG_CONFIG_MUX_SINGLE_3 = (0x7000)  // Single-ended AIN3

        val ADS1015_REG_CONFIG_PGA_MASK = (0x0E00)
        val ADS1015_REG_CONFIG_PGA_6_144V = (0x0000)  // +/-6.144V range = Gain 2/3
        val ADS1015_REG_CONFIG_PGA_4_096V = (0x0200)  // +/-4.096V range = Gain 1
        val ADS1015_REG_CONFIG_PGA_2_048V = (0x0400)  // +/-2.048V range = Gain 2 =(default)
        val ADS1015_REG_CONFIG_PGA_1_024V = (0x0600)  // +/-1.024V range = Gain 4
        val ADS1015_REG_CONFIG_PGA_0_512V = (0x0800)  // +/-0.512V range = Gain 8
        val ADS1015_REG_CONFIG_PGA_0_256V = (0x0A00)  // +/-0.256V range = Gain 16

        val ADS1015_REG_CONFIG_MODE_MASK = (0x0100)
        val ADS1015_REG_CONFIG_MODE_CONTIN = (0x0000)  // Continuous conversion mode
        val ADS1015_REG_CONFIG_MODE_SINGLE = (0x0100)  // Power-down single-shot mode =(default)

        val ADS1015_REG_CONFIG_DR_MASK = (0x00E0)
        val ADS1015_REG_CONFIG_DR_128SPS = (0x0000)  // 128 samples per second
        val ADS1015_REG_CONFIG_DR_250SPS = (0x0020)  // 250 samples per second
        val ADS1015_REG_CONFIG_DR_490SPS = (0x0040)  // 490 samples per second
        val ADS1015_REG_CONFIG_DR_920SPS = (0x0060)  // 920 samples per second
        val ADS1015_REG_CONFIG_DR_1600SPS = (0x0080)  // 1600 samples per second =(default)
        val ADS1015_REG_CONFIG_DR_2400SPS = (0x00A0)  // 2400 samples per second
        val ADS1015_REG_CONFIG_DR_3300SPS = (0x00C0)  // 3300 samples per second

        val ADS1015_REG_CONFIG_CMODE_MASK = (0x0010)
        val ADS1015_REG_CONFIG_CMODE_TRAD = (0x0000)  // Traditional comparator with hysteresis =(default)
        val ADS1015_REG_CONFIG_CMODE_WINDOW = (0x0010)  // Window comparator

        val ADS1015_REG_CONFIG_CPOL_MASK = (0x0008)
        val ADS1015_REG_CONFIG_CPOL_ACTVLOW = (0x0000)  // ALERT/RDY pin is low when active =(default)
        val ADS1015_REG_CONFIG_CPOL_ACTVHI = (0x0008)  // ALERT/RDY pin is high when active

        val ADS1015_REG_CONFIG_CLAT_MASK = (0x0004)  // Determines if ALERT/RDY pin latches once asserted
        val ADS1015_REG_CONFIG_CLAT_NONLAT = (0x0000)  // Non-latching comparator =(default)
        val ADS1015_REG_CONFIG_CLAT_LATCH = (0x0004)  // Latching comparator

        val ADS1015_REG_CONFIG_CQUE_MASK = (0x0003)
        val ADS1015_REG_CONFIG_CQUE_1CONV = (0x0000)  // Assert ALERT/RDY after one conversions
        val ADS1015_REG_CONFIG_CQUE_2CONV = (0x0001)  // Assert ALERT/RDY after two conversions
        val ADS1015_REG_CONFIG_CQUE_4CONV = (0x0002)  // Assert ALERT/RDY after four conversions
        val ADS1015_REG_CONFIG_CQUE_NONE = (0x0003)  // Disable the comparator and put ALERT/RDY in high state =(default)

        val ADS1015_REG_CONFIG_MUX_SINGLE = mapOf(
                0 + 0x04 to ADS1015_REG_CONFIG_MUX_SINGLE_0,
                1 + 0x04 to ADS1015_REG_CONFIG_MUX_SINGLE_1,
                2 + 0x04 to ADS1015_REG_CONFIG_MUX_SINGLE_2,
                3 + 0x04 to ADS1015_REG_CONFIG_MUX_SINGLE_3
        )
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

    fun readADC(channel: Int, gain: Int = 1, dataRate: Int? = null): Int {
        return read(channel + 0x04, gain, dataRate, ADS1x15_CONFIG_MODE_SINGLE)
    }

    private fun read(mux: Int, gain: Int, dataRate: Int?, mode: Int): Int {
        var config = ADS1015_REG_CONFIG_CQUE_NONE or // Disable the comparator (default val)
                ADS1015_REG_CONFIG_CLAT_NONLAT or // Non-latching (default val)
                ADS1015_REG_CONFIG_CPOL_ACTVLOW or // Alert/Rdy active low   (default val)
                ADS1015_REG_CONFIG_CMODE_TRAD or // Traditional comparator (default val)
                ADS1015_REG_CONFIG_DR_1600SPS or // 1600 samples per second (default)
                ADS1015_REG_CONFIG_MODE_SINGLE   // Single-shot mode (default)

        config = config or ADS1x15_CONFIG_GAIN[gain]!!
        config = config or ADS1015_REG_CONFIG_MUX_SINGLE[mux]!!
        config = config or ADS1015_REG_CONFIG_OS_SINGLE


        val bytes = ByteArray(2)
        bytes[0] = (config.shr(8)).and(0xFF).toByte()
        bytes[1] = (config.and(0xFF)).toByte()
        i2c.writeRegBuffer(ADS1x15_POINTER_CONFIG, bytes, bytes.size)
        Thread.sleep(1)

        val result = ByteArray(2)
        i2c.readRegBuffer(ADS1x15_POINTER_CONVERSION, result, 2)
        return conversionValue(result[1].toInt(), result[0].toInt())

        /*
        var config = ADS1x15_CONFIG_OS_SINGLE
        config = config.or(mux.and(0x07)).shl(ADS1x15_CONFIG_MUX_OFFSET)  // config |= (mux & 0x07) << ADS1x15_CONFIG_MUX_OFFSET
        config = config.or(ADS1x15_CONFIG_GAIN[gain]!!)
        config = config.or(mode)

        config = config.or(ADS1015_CONFIG_DR[DATA_RATE_DEFAULT]!!)
        config = config.or(ADS1x15_CONFIG_COMP_QUE_DISABLE)

        val bytes = ByteArray(2)
        bytes[0] = (config.shr(8)).and(0xFF).toByte()
        bytes[1] = (config.and(0xFF)).toByte()

        i2c.writeRegBuffer(ADS1x15_POINTER_CONFIG, bytes, bytes.size)

        Thread.sleep((1.0 / dataRate + 0.0001).toLong())

        val result = ByteArray(2)
        i2c.readRegBuffer(ADS1x15_POINTER_CONVERSION, result, 2)
        return conversionValue(result[1].toInt(), result[0].toInt())
        */
    }
}