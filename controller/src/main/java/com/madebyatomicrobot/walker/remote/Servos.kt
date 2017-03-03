package com.madebyatomicrobot.walker.remote

import android.databinding.BaseObservable
import android.databinding.Bindable
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

class Servos : BaseObservable() {
    var readOnly = true
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.readOnly)
        }

    var oppositeServosSlaved = true
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.oppositeServosSlaved)
        }

    var angle00: Int by AngleDelegate(BR.angle00, this::angle15)
        @Bindable get
    var angle01: Int by AngleDelegate(BR.angle01, this::angle14)
        @Bindable get
    var angle02: Int by AngleDelegate(BR.angle02, this::angle13)
        @Bindable get
    var angle03: Int by AngleDelegate(BR.angle03, this::angle12)
        @Bindable get
    var angle04: Int by AngleDelegate(BR.angle04, this::angle11)
        @Bindable get
    var angle05: Int by AngleDelegate(BR.angle05, this::angle10)
        @Bindable get
    var angle06: Int by AngleDelegate(BR.angle06, this::angle09)
        @Bindable get
    var angle07: Int by AngleDelegate(BR.angle07, this::angle08)
        @Bindable get
    var angle08: Int by AngleDelegate(BR.angle08, this::angle07)
        @Bindable get
    var angle09: Int by AngleDelegate(BR.angle09, this::angle06)
        @Bindable get
    var angle10: Int by AngleDelegate(BR.angle10, this::angle05)
        @Bindable get
    var angle11: Int by AngleDelegate(BR.angle11, this::angle04)
        @Bindable get
    var angle12: Int by AngleDelegate(BR.angle12, this::angle03)
        @Bindable get
    var angle13: Int by AngleDelegate(BR.angle13, this::angle02)
        @Bindable get
    var angle14: Int by AngleDelegate(BR.angle14, this::angle01)
        @Bindable get
    var angle15: Int by AngleDelegate(BR.angle15, this::angle00)
        @Bindable get
}
