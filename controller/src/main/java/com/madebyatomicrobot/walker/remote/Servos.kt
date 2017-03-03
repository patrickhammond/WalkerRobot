package com.madebyatomicrobot.walker.remote

import android.databinding.BaseObservable
import android.databinding.Bindable

class Servos : BaseObservable() {
    var readOnly = true
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.readOnly)
        }

    var oppositeServosSlaved = true
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.oppositeServosSlaved)
        }

    var angle00 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle00)

            if (oppositeServosSlaved && angle00 != angle15) {
                angle15 = value
            }
        }

    var angle01 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle01)

            if (oppositeServosSlaved && angle01 != angle14) {
                angle14 = value
            }
        }

    var angle02 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle02)

            if (oppositeServosSlaved && angle02 != angle13) {
                angle13 = value
            }
        }
    var angle03 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle03)

            if (oppositeServosSlaved && angle03 != angle12) {
                angle12 = value
            }
        }

    var angle04 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle04)

            if (oppositeServosSlaved && angle04 != angle11) {
                angle11 = value
            }
        }

    var angle05 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle05)

            if (oppositeServosSlaved && angle05 != angle10) {
                angle10 = value
            }
        }

    var angle06 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle06)

            if (oppositeServosSlaved && angle06 != angle09) {
                angle09 = value
            }
        }

    var angle07 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle07)

            if (oppositeServosSlaved && angle07 != angle08) {
                angle08 = value
            }
        }

    var angle08 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle08)

            if (oppositeServosSlaved && angle08 != angle07) {
                angle07 = value
            }
        }

    var angle09 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle09)

            if (oppositeServosSlaved && angle09 != angle06) {
                angle06 = value
            }
        }

    var angle10 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle10)

            if (oppositeServosSlaved && angle10 != angle05) {
                angle05 = value
            }
        }

    var angle11 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle11)

            if (oppositeServosSlaved && angle11 != angle04) {
                angle04 = value
            }
        }

    var angle12 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle12)

            if (oppositeServosSlaved && angle12 != angle03) {
                angle03 = value
            }
        }

    var angle13 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle13)

            if (oppositeServosSlaved && angle13 != angle02) {
                angle02 = value
            }
        }

    var angle14 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle14)

            if (oppositeServosSlaved && angle14 != angle01) {
                angle01 = value
            }
        }

    var angle15 = 90
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.angle15)

            if (oppositeServosSlaved && angle15 != angle00) {
                angle00 = value
            }
        }
}
