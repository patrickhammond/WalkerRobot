package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Command(database: DatabaseReference) : BaseObservable() {
    companion object {
        private val STOPPED = "stopped"
        private val FORWARD = "forward"
        private val REVERSE = "reverse"
    }

    private val currentCommandRef = database.child("command").child("current")

    private var currentCommand = STOPPED

    val stopped: Boolean
        @Bindable get() = currentCommand == STOPPED

    val forward: Boolean
        @Bindable get() = currentCommand == FORWARD

    val reverse: Boolean
        @Bindable get() = currentCommand == REVERSE

    init {
        currentCommandRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val commandName = dataSnapshot!!.getValue(String::class.java)
                when(commandName) {
                    FORWARD -> forward()
                    REVERSE -> reverse()
                    STOPPED -> stop()
                }
            }

            override fun onCancelled(error: DatabaseError?) { }
        })
    }

    fun stop() {
        currentCommandRef.setValue(STOPPED)
        currentCommand = STOPPED
        notifyChange()
    }

    fun forward() {
        currentCommandRef.setValue(FORWARD)
        currentCommand = FORWARD
        notifyChange()
    }

    fun reverse() {
        currentCommandRef.setValue(REVERSE)
        currentCommand = REVERSE
        notifyChange()
    }
}
