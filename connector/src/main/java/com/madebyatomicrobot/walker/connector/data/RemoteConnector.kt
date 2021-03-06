package com.madebyatomicrobot.walker.connector.data

import com.google.firebase.database.*
import io.reactivex.Observable

class RemoteConnector(val database: FirebaseDatabase, val robotId: String) {
    private val robotUpdatedRef = getRobotDatabaseRef().child("robot_updated")
    private val actionsRef = getRobotDatabaseRef().child("actions")
    private val servosStatusRef = getRobotDatabaseRef().child("servos_status")
    private val servosConfigRef = getRobotDatabaseRef().child("servos_config")
    private val commandRef = getRobotDatabaseRef().child("command")

    private fun getRobotDatabaseRef() = database.reference.child("robots").child(robotId)

    fun getRobotUpdateTime(): Observable<String> {
        return getValueEventListener(robotUpdatedRef)
    }

    fun setRobotUpdateTime(timestamp: String) {
        robotUpdatedRef.setValue(timestamp)
    }

    fun getCommand(): Observable<Command> {
        return getValueEventListener(commandRef)
    }

    fun setCommand(command: Command) {
        commandRef.setValue(command)
    }

    fun getServosStatus(): Observable<ServosStatus> {
        return getValueEventListener(servosStatusRef)
    }

    fun setServosStatus(servosStatus: ServosStatus) {
        servosStatusRef.setValue(servosStatus)
    }

    fun getServosConfig(): Observable<ServosConfig> {
        return getValueEventListener(servosConfigRef)
    }

    fun setServosConfig(servosStatus: ServosConfig) {
        servosConfigRef.setValue(servosStatus)
    }

    fun getServoConfig(servoId : String): Observable<ServosConfig.Servo> {
        return getValueEventListener(servosConfigRef.child(servoId))
    }

    fun setServoConfig(servoId: String, servoConfig: ServosConfig.Servo) {
        servosConfigRef.child(servoId).setValue(servoConfig)
    }

    fun getActions(): Observable<Actions> {
        return getValueEventListener(actionsRef)
    }

    fun setActions(actions: Actions) {
        actionsRef.setValue(actions)
    }

    private inline fun <reified T> getValueEventListener(reference: DatabaseReference) : Observable<T> {
        return Observable.create({ subscriber ->
            run {
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        if (!subscriber.isDisposed) {
                            try {
                                val value = dataSnapshot?.getValue(T::class.java)
                                if (value != null) {
                                    subscriber.onNext(value)
                                }
                            } catch (ex: DatabaseException) {
                                subscriber.onError(ex)
                            }
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