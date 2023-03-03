package com.penguenlabs.pushnote.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun Toast(text: String, length: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    Toast.makeText(context, text, length).show()
}