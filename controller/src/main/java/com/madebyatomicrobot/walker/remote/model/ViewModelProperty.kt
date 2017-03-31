package com.madebyatomicrobot.walker.remote.model

import android.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *
 * Make sure to add <pre>@Bindable get{code}</pre> to your property.
 */
class ViewModelProperty<T>(var currentValue: T) : ReadWriteProperty<BaseObservable, T> {
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T {
        return currentValue
    }

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, updated: T) {
        currentValue = updated
        thisRef.notifyChange()
    }
}
