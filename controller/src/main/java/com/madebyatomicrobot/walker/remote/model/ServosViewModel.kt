package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.madebyatomicrobot.walker.remote.data.Servos
import kotlin.reflect.KMutableProperty

class ServosViewModel(database: DatabaseReference) : BaseObservable() {
    private val currentServosRef = database.child("servos")

    init {
        currentServosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.let {
                    servos = dataSnapshot.getValue(Servos::class.java)
                    notifyChange()
                }
            }

            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    var servos: Servos = Servos()
        @Bindable get() = field
        set(value) {
            field = value
            notifyChange()
        }

    var oppositeServosSlaved = true
        @Bindable get
        set(value) {
            field = value
            notifyChange()
        }

    fun servoLabel(servo: String, value: Int): String {
        return "$servo ($value)"
    }

    fun getControlServos(): Boolean {
        return servos.controlServos
    }

    fun setControlServos(controlServos: Boolean) {
        servos.controlServos = controlServos
        currentServosRef.setValue(servos)
    }

    fun setServo(servo: Int, progress: Int) {
        servoFunctions[servo].invoke(progress)
    }

    private val servoFunctions: Array<(Int) -> Unit> = arrayOf(
            { progress -> updateServoFields(progress, servos::servo00, servos::servo15) },
            { progress -> updateServoFields(progress, servos::servo01, servos::servo14) },
            { progress -> updateServoFields(progress, servos::servo02, servos::servo13) },
            { progress -> updateServoFields(progress, servos::servo03, servos::servo12) },
            { progress -> updateServoFields(progress, servos::servo04, servos::servo11) },
            { progress -> updateServoFields(progress, servos::servo05, servos::servo10) },
            { progress -> updateServoFields(progress, servos::servo06, servos::servo09) },
            { progress -> updateServoFields(progress, servos::servo07, servos::servo08) },
            { progress -> updateServoFields(progress, servos::servo08, servos::servo07) },
            { progress -> updateServoFields(progress, servos::servo09, servos::servo06) },
            { progress -> updateServoFields(progress, servos::servo10, servos::servo05) },
            { progress -> updateServoFields(progress, servos::servo11, servos::servo04) },
            { progress -> updateServoFields(progress, servos::servo12, servos::servo03) },
            { progress -> updateServoFields(progress, servos::servo13, servos::servo02) },
            { progress -> updateServoFields(progress, servos::servo14, servos::servo01) },
            { progress -> updateServoFields(progress, servos::servo15, servos::servo00) })

    private fun updateServoFields(angle: Int, servo: KMutableProperty<Int>, slaveServo: KMutableProperty<Int>) {
        servo.setter.call(angle)
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                slaveServo.setter.call(angle)
            }

            currentServosRef.setValue(servos)
        }
    }
}
