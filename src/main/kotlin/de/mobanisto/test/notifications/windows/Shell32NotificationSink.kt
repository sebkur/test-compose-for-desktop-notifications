package de.mobanisto.test.notifications.windows

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.win32.W32APIOptions
import de.mobanisto.test.notifications.NotificationSink
import de.mobanisto.test.notifications.windows.NOTIFYICONDATA.Companion.NIIF_NONE
import de.mobanisto.test.notifications.windows.NOTIFYICONDATA.Companion.NIIF_NOSOUND
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class Shell32NotificationSink(private val hWnd: HWND, private val title: String) : NotificationSink {

    companion object {
        const val NIM_ADD = 0x0
        const val NIM_MODIFY = 0x1
        const val NIM_DELETE = 0x2
    }

    private lateinit var libShell32: Shell32

    override var available: Boolean = false
        private set

    private enum class Error { NONE, LOAD }

    private var error = Error.NONE

    override val errorMessage: String
        get() = when (error) {
            Error.LOAD -> "Error while loading libnotify"
            else -> ""
        }

    override fun init() {
        val clazz = Shell32::class.java
        val options: MutableMap<String, Any> = HashMap()
        options[Library.OPTION_CLASSLOADER] = clazz.classLoader
        val entries: Set<Map.Entry<String, Any>> = W32APIOptions.DEFAULT_OPTIONS.entries
        for ((key, value) in entries) {
            options[key] = value
        }
        libShell32 = Native.load("shell32", Shell32::class.java, options)
    }

    override fun uninit() {
        val data = NOTIFYICONDATA()
        data.hWnd = hWnd
        libShell32.Shell_NotifyIcon(NIM_DELETE, data)
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
        data.hWnd = hWnd
        data.setBalloon(title, message, 10000, NIIF_NONE or NIIF_NOSOUND)
        val icon = WinDef.HICON(image)
        data.setIcon(icon)
        val ret = libShell32.Shell_NotifyIcon(NIM_ADD, data)
        User32.INSTANCE.DestroyIcon(icon)
        println("return value: $ret")
    }

    interface Shell32 : Library {
        fun Shell_NotifyIcon(dwMessage: Int, lpdata: NOTIFYICONDATA?): Boolean
    }
}
