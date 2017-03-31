package com.madebyatomicrobot.walker.remote

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

class RobotApplication : Application() {
    companion object {
        fun getApp(context: Context): RobotApplication {
            return (context.applicationContext as RobotApplication)
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
                .robotModule(ApplicationComponent.RobotModule())
                .build()
    }


}
