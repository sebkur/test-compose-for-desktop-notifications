package de.mobanisto.test.notifications.windows.toasts

import com.sun.jna.platform.win32.WinDef.HWND
import de.mobanisto.test.notifications.NotificationSink
import de.mobanisto.wintoast.helper.ToastHandle
import de.mobanisto.wintoast.helper.WinToastHelper

class ToastsNotificationSink(private val hWnd: HWND, private val title: String) : NotificationSink {

    private lateinit var winToastHelper: WinToastHelper

    override var available: Boolean = false
        private set

    override val errorMessage: String
        get() = ""

    override fun init() {
        winToastHelper = WinToastHelper(title)
    }

    var currentToast: ToastHandle? = null

    override fun uninit() {
        currentToast?.hide()
    }

    override fun notify(message: String) {
        currentToast?.hide()
        currentToast = winToastHelper.showTextToast("Notification Test", message)
    }

}
