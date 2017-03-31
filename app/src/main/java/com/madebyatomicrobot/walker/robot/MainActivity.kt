package com.madebyatomicrobot.walker.robot

import android.app.Activity
import android.os.Bundle
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.remote.RobotApplication
import javax.inject.Inject


class MainActivity : Activity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    @Inject lateinit var connector: RemoteConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        RobotApplication.getApp(this).component.inject(this)
        super.onCreate(savedInstanceState)
    }
}
