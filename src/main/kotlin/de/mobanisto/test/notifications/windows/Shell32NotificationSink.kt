package de.mobanisto.test.notifications.windows

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import de.mobanisto.test.notifications.NotificationSink
import de.mobanisto.test.notifications.windows.NOTIFYICONDATA.Companion.NIIF_NONE
import de.mobanisto.test.notifications.windows.NOTIFYICONDATA.Companion.NIIF_NOSOUND
import de.mobanisto.test.notifications.windows.Shell32.NIM_DELETE
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class Shell32NotificationSink(private val title: String) : NotificationSink {

    override var available: Boolean = false
        private set

    private enum class Error { NONE, LOAD }

    private var error = Error.NONE

    override val errorMessage: String
        get() = when (error) {
            Error.LOAD -> "Error while loading libnotify"
            else -> ""
        }

    override fun init() = uninit()

    override fun uninit() {
        val data = NOTIFYICONDATA()
        Shell32.Shell_NotifyIcon(NIM_DELETE, data)
    }

    override fun notify(message: String) {
        // Copy icon from resources to temporary file because LoadImage only works with files
        val tmp = File.createTempFile("test-notifications", ".ico")
        Thread.currentThread().contextClassLoader.getResourceAsStream("icon.ico").use {
            Files.copy(it, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        val image =
            User32.INSTANCE.LoadImage(null, tmp.absolutePath, WinUser.IMAGE_ICON, 0, 0, WinUser.LR_LOADFROMFILE)

        val data = NOTIFYICONDATA()
        data.setBalloon(title, message, 10000, NIIF_NONE or NIIF_NOSOUND)
        val icon = WinDef.HICON(image)
        data.setIcon(icon)
        val ret = Shell32.Shell_NotifyIcon(Shell32.NIM_ADD, data)
        User32.INSTANCE.DestroyIcon(icon)
        println("return value: $ret")
    }
}
