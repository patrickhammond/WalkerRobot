package com.madebyatomicrobot.walker.remote

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

class ControllerApplication : Application() {
    companion object {
        fun getApp(context: Context): ControllerApplication {
            return (context.applicationContext as ControllerApplication)
        }
    }

    lateinit var component: ApplicationComponent
        private set

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
                .controllerModule(ApplicationComponent.ControllerModule())
                .build()
    }


}
