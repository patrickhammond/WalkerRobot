package com.madebyatomicrobot.walker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.madebyatomicrobot.walker.things.R


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}
