package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Servos(database: DatabaseReference) : BaseObservable() {
    companion object {
        private val DEFAULT_SERVO_ANGLE = 90
    }

    class ServoPositions {
        var controlServos = false
        var servo00: Int = DEFAULT_SERVO_ANGLE
        var servo01: Int = DEFAULT_SERVO_ANGLE
        var servo02: Int = DEFAULT_SERVO_ANGLE
        var servo03: Int = DEFAULT_SERVO_ANGLE
        var servo04: Int = DEFAULT_SERVO_ANGLE
        var servo05: Int = DEFAULT_SERVO_ANGLE
        var servo06: Int = DEFAULT_SERVO_ANGLE
        var servo07: Int = DEFAULT_SERVO_ANGLE
        var servo08: Int = DEFAULT_SERVO_ANGLE
        var servo09: Int = DEFAULT_SERVO_ANGLE
        var servo10: Int = DEFAULT_SERVO_ANGLE
        var servo11: Int = DEFAULT_SERVO_ANGLE
        var servo12: Int = DEFAULT_SERVO_ANGLE
        var servo13: Int = DEFAULT_SERVO_ANGLE
        var servo14: Int = DEFAULT_SERVO_ANGLE
        var servo15: Int = DEFAULT_SERVO_ANGLE
    }

    var servos: ServoPositions = ServoPositions()
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

    private val currentServosRef = database.child("servos")

    init {
        currentServosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.let {
                    servos = dataSnapshot.getValue(ServoPositions::class.java)
                    notifyChange()
                }
            }

            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    fun servoLabel(servo: String, value: Int) : String {
        return "$servo ($value)"
    }

    fun getControlServos() : Boolean {
        return servos.controlServos
    }

    fun setControlServos(controlServos: Boolean) {
        servos.controlServos = controlServos
        currentServosRef.setValue(servos)
    }

    fun setServo00(progress: Int) {
        servos.servo00 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo15 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo01(progress: Int) {
        servos.servo01 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo14 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo02(progress: Int) {
        servos.servo02 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo13 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo03(progress: Int) {
        servos.servo03 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo12 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo04(progress: Int) {
        servos.servo04 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo11 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo05(progress: Int) {
        servos.servo05 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo10 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo06(progress: Int) {
        servos.servo06 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo09 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo07(progress: Int) {
        servos.servo07 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo08 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo08(progress: Int) {
        servos.servo08 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo07 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo09(progress: Int) {
        servos.servo09 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo06 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo10(progress: Int) {
        servos.servo10 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo05 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo11(progress: Int) {
        servos.servo11 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo04 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo12(progress: Int) {
        servos.servo12 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo03 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo13(progress: Int) {
        servos.servo13 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo02 = progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo14(progress: Int) {
        servos.servo14 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo01= progress
            }

            currentServosRef.setValue(servos)
        }
    }

    fun setServo15(progress: Int) {
        servos.servo15 = progress
        if (servos.controlServos) {
            if (oppositeServosSlaved) {
                servos.servo00 = progress
            }

            currentServosRef.setValue(servos)
        }
    }
}
