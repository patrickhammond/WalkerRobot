package com.madebyatomicrobot.walker.remote

import com.google.firebase.database.FirebaseDatabase
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = arrayOf(ApplicationComponent.ControllerModule::class))
interface ApplicationComponent {
    @Module class ControllerModule {
        @Provides fun provideRemoteConnector(): RemoteConnector {
            return RemoteConnector(FirebaseDatabase.getInstance())
        }
    }

    fun inject(fragment: CommandFragment)
    fun inject(fragment: ServosFragment)
    fun inject(fragment: ConfigFragment)
    fun inject(fragment: ServoEditorFragment)
}