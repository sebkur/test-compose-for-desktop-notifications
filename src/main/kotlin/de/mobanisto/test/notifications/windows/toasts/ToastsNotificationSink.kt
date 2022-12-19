package de.mobanisto.test.notifications.windows.toasts

import de.mobanisto.test.notifications.NotificationSink
import de.mobanisto.toast4j.ToastBuilder
import de.mobanisto.toast4j.ToastHandle
import de.mobanisto.toast4j.Toaster
import de.mobanisto.wintoast.WinToastTemplate.AudioOption
import de.mobanisto.wintoast.WinToastTemplate.WinToastTemplateType.ToastText02

class ToastsNotificationSink(private val aumi: String, private val title: String) : NotificationSink {

    private lateinit var toaster: Toaster

    override var available: Boolean = false
        private set

    override val errorMessage: String
        get() = ""

    override fun init() {
        toaster = Toaster.forAumi(aumi)
        toaster.initialize()
        toaster.initializeShortcut(title, true)
    }

    var currentToast: ToastHandle? = null

    override fun uninit() {
        currentToast?.hide()
    }

    override fun notify(message: String) {
        currentToast?.hide()
        currentToast = toaster.showToast(
            ToastBuilder(ToastText02).setLine1("Notification Tests").setLine2(message)
                .setSilent().setExpiration(10000).build()
        )
    }

}
