package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.madebyatomicrobot.walker.remote.data.Command
import com.madebyatomicrobot.walker.remote.data.Command.Companion.FORWARD
import com.madebyatomicrobot.walker.remote.data.Command.Companion.REVERSE
import com.madebyatomicrobot.walker.remote.data.Command.Companion.STOPPED

class CommandViewModel(database: DatabaseReference) : BaseObservable() {
    private val currentCommandRef = database.child("command")

    init {
        currentCommandRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                command = dataSnapshot!!.getValue(Command::class.java)
                notifyChange()
            }

            override fun onCancelled(error: DatabaseError?) { }
        })
    }

    var command: Command = Command()
        @Bindable get() = field
        set(value) {
            field = value
            notifyChange()
        }

    val stopped: Boolean
        @Bindable get() = command.current == STOPPED

    val forward: Boolean
        @Bindable get() = command.current == FORWARD

    val reverse: Boolean
        @Bindable get() = command.current == REVERSE

    fun stop() {
        command.current = STOPPED
        currentCommandRef.setValue(command)
        notifyChange()
    }

    fun forward() {
        command.current = FORWARD
        currentCommandRef.setValue(command)
        notifyChange()
    }

    fun reverse() {
        command.current = REVERSE
        currentCommandRef.setValue(command)
        notifyChange()
    }
}
