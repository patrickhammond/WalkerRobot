package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.madebyatomicrobot.walker.remote.data.Servos

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
