package com.madebyatomicrobot.walker.remote.model

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

class AngleDelegate(val bindingResource: Int, val slave: KMutableProperty<Int>, var angle: Int = 90) : ReadWriteProperty<Servos, Int> {
    override fun getValue(thisRef: Servos, property: KProperty<*>): Int {
        return angle
    }

    override fun setValue(thisRef: Servos, property: KProperty<*>, value: Int) {
        angle = value
        thisRef.notifyPropertyChanged(bindingResource)
        if (thisRef.oppositeServosSlaved && (angle != slave.getter.call())) {
            slave.setter.call(value)
        }
    }
}