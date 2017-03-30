package com.madebyatomicrobot.walker.remote.model;

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.madebyatomicrobot.walker.remote.data.Config

class ConfigViewModel(database: DatabaseReference) : BaseObservable() {
    private val currentConfigRef = database.child("config")

    init {
        currentConfigRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                config = dataSnapshot!!.getValue(Config::class.java)
                notifyChange()
            }

            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    var config: Config = Config()
        @Bindable get() = field

    fun save() {
        currentConfigRef.setValue(config)
    }
}
