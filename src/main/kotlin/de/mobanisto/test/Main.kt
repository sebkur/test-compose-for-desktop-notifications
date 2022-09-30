package de.mobanisto.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

fun main() {
    Main().run()
}

class Main {

    fun run() {
        println("Test")

        singleWindowApplication(title = "Test") {
            val (value, setValue) = remember { mutableStateOf("input") }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Test")
                TextField(value, setValue)
                TextButton({ setValue("") }) {
                    Text("Clear")
                }
            }
        }
    }
}
