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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import de.mobanisto.test.notifications.linux.LibNotifyNotificationSink
import de.mobanisto.test.notifications.NotificationSink

fun main() {
    Main().run()
}

class Main {

    private lateinit var notificationSink: NotificationSink

    fun run() {
        println("Test")

        notificationSink = LibNotifyNotificationSink("Test Notifications")
        notificationSink.init()

        singleWindowApplication(title = "Testing Notifications") {
            DisposableEffect(Unit) {
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

    private fun sendNotification(message: String) {
        notificationSink.notify(message)
    }
}
