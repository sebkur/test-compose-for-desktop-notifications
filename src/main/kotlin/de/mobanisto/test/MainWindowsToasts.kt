package de.mobanisto.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import de.mobanisto.test.notifications.NotificationSink
import de.mobanisto.test.notifications.StubNotificationSink
import de.mobanisto.test.notifications.linux.LibNotifyNotificationSink
import de.mobanisto.test.notifications.windows.toasts.ToastsNotificationSink

fun main() {
    MainWindowsToasts().run()
}

class MainWindowsToasts {

    private lateinit var notificationSink: NotificationSink

    fun run() {
        println("Test")

        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Testing Notifications",
                icon = painterResource("icon.png")
            ) {
                DisposableEffect(Unit) {
                    val title = "Test Notifications"
                    notificationSink = when {
                        OsUtils.isLinux -> LibNotifyNotificationSink(title)
                        OsUtils.isWindows -> ToastsNotificationSink(title)
                        else -> StubNotificationSink
                    }
                    notificationSink.init()
                    onDispose {
                        notificationSink.uninit()
                    }
                }

                val (value, setValue) = remember { mutableStateOf("message text in notification") }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Enter some text and press 'send' to display the message as a notification:")
                    TextField(value, setValue)
                    TextButton({ sendNotification(value) }) {
                        Text("Send")
                    }
                }
            }
        }
    }

    private fun sendNotification(message: String) {
        notificationSink.notify(message)
    }
}
