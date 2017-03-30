package com.madebyatomicrobot.walker.remote.model

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

class AngleDelegate(val bindingResource: Int, val slave: KMutableProperty<Int>, var angle: Int = 90) : ReadWriteProperty<ServosViewModel, Int> {
    override fun getValue(thisRef: ServosViewModel, property: KProperty<*>): Int {
        return angle
    }

    override fun setValue(thisRef: ServosViewModel, property: KProperty<*>, value: Int) {
        angle = value
        thisRef.notifyPropertyChanged(bindingResource)
        if (thisRef.oppositeServosSlaved && (angle != slave.getter.call())) {
            slave.setter.call(value)
        }
    }
}