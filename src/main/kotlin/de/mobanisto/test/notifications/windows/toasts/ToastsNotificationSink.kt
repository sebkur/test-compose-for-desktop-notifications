package de.mobanisto.test.notifications.windows.toasts

import de.mobanisto.test.notifications.NotificationSink
import de.mobanisto.wintoast.WinToastTemplate.AudioOption
import de.mobanisto.wintoast.WinToastTemplate.WinToastTemplateType.ToastText02
import de.mobanisto.wintoast.helper.ToastBuilder
import de.mobanisto.wintoast.helper.ToastHandle
import de.mobanisto.wintoast.helper.WinToastHelper

class ToastsNotificationSink(private val aumi: String, private val title: String) : NotificationSink {

    private lateinit var winToastHelper: WinToastHelper

    override var available: Boolean = false
        private set

    override val errorMessage: String
        get() = ""

    override fun init() {
        winToastHelper = WinToastHelper(aumi, title)
        winToastHelper.initialize()
        winToastHelper.initializeShortcut()
    }

    var currentToast: ToastHandle? = null

    override fun uninit() {
        currentToast?.hide()
    }

    override fun notify(message: String) {
        currentToast?.hide()
        currentToast = winToastHelper.showToast(
            ToastBuilder(ToastText02).setLine1("Notification Tests").setLine2(message)
                .setAudioOption(AudioOption.Silent).setExpiration(10000).build()
        )
    }

}
