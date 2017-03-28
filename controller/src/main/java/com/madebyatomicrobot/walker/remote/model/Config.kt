package com.madebyatomicrobot.walker.remote.model;

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Config(database: DatabaseReference) : BaseObservable() {
    private val currentConfigRef = database.child("config")

    data class ServoConfig(
            var action: String = "",
            var duration: String = "")

    data class LegConfig(
            val hipY: ServoConfig = ServoConfig(),
            val hipX: ServoConfig = ServoConfig(),
            val hipZ: ServoConfig = ServoConfig(),
            val knee: ServoConfig = ServoConfig(),
            val ankle: ServoConfig = ServoConfig())

    data class WalkerConfig(
            val left: LegConfig = LegConfig(),
            val right: LegConfig = LegConfig())

    var config: WalkerConfig = WalkerConfig()
        @Bindable get() = field

    init {
        currentConfigRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                config = dataSnapshot!!.getValue(WalkerConfig::class.java)
                notifyChange()
            }

            override fun onCancelled(p0: DatabaseError?) {}
        })
    }

    fun save() {
        currentConfigRef.setValue(config)
    }
}
