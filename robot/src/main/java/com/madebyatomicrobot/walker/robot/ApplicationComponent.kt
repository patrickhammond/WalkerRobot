package com.madebyatomicrobot.walker.remote

import com.google.firebase.database.FirebaseDatabase
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.robot.MainActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = arrayOf(ApplicationComponent.RobotModule::class))
interface ApplicationComponent {
    @Module class RobotModule {
        @Provides fun provideRemoteConnector(): RemoteConnector {
            return RemoteConnector(FirebaseDatabase.getInstance(), "walker1")
        }
    }

    fun inject(activity: MainActivity)
}