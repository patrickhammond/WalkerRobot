package com.madebyatomicrobot.walker.remote.model;

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.madebyatomicrobot.walker.connector.data.Actions
import com.madebyatomicrobot.walker.connector.data.RemoteConnector
import io.reactivex.disposables.CompositeDisposable

class ActionsViewModel(val connector: RemoteConnector) : BaseObservable() {
    companion object {
        val TAG: String = ActionsViewModel::class.java.simpleName
    }

    var actions: Actions by ViewModelProperty(Actions())
        @Bindable get

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onResume() {
        disposables.add(
                connector.getActions().subscribe(
                        { _actions -> actions = _actions },
                        { error -> Log.e(TAG, "Actions error", error) }))
    }

    fun onPause() {
        disposables.clear()
    }

    fun save() {
        connector.setActions(actions)
    }
}
