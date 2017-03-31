package com.madebyatomicrobot.walker.connector.data

import com.google.firebase.database.*
import io.reactivex.Observable

class RemoteConnector(val database: FirebaseDatabase) {
    private val commandRef = database.reference.child("command")
    private val servosRef = database.reference.child("servos")
    private val configRef = database.reference.child("config")

    fun getCommand(): Observable<Command> {
        return getValueEventListener(commandRef)
    }

    fun setCommand(command: Command) {
        commandRef.setValue(command)
    }

    fun getServos(): Observable<Servos> {
        return getValueEventListener(servosRef)
    }

    fun setServos(servos: Servos) {
        servosRef.setValue(servos)
    }

    fun getConfig(): Observable<Config> {
        return getValueEventListener(configRef)
    }

    fun setConfig(config: Config) {
        configRef.setValue(config)
    }

    private inline fun <reified T> getValueEventListener(reference: DatabaseReference) : Observable<T> {
        return Observable.create({ subscriber ->
            run {
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        if (!subscriber.isDisposed) {
                            subscriber.onNext(dataSnapshot!!.getValue(T::class.java))
                        }
                    }

                    override fun onCancelled(error: DatabaseError?) {
                        if (!subscriber.isDisposed) {
                            subscriber.onError(RemoteConnectorException(error))
                        }
                    }
                })
            }
        })
    }
}