package com.madebyatomicrobot.walker.connector.data

import com.google.firebase.database.DatabaseError
import java.lang.Exception

data class RemoteConnectorException(val error: DatabaseError?) : Exception()
