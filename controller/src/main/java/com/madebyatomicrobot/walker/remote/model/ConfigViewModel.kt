package com.madebyatomicrobot.walker.remote.model;

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import com.madebyatomicrobot.walker.connector.data.Config
import io.reactivex.disposables.CompositeDisposable

class ConfigViewModel(val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ConfigViewModel::class.java.simpleName
    }

    var config: Config by ViewModelProperty(Config())
        @Bindable get

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getConfig().subscribe(
                        { _config -> config = _config },
                        { error -> Log.e(TAG, "Config error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun save() {
        connector.setConfig(config)
    }
}
