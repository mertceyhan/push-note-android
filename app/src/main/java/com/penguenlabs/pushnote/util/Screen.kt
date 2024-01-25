package com.penguenlabs.pushnote.util

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.penguenlabs.pushnote.navigation.Destination

@Composable
fun Screen(
    context: Context = LocalContext.current,
    destination: Destination,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding(),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            content()
        }
        LaunchedEffect(Unit) {
            logScreenViewEvent(context = context, screenName = destination.route)
        }
    }
}

private fun logScreenViewEvent(context: Context, screenName: String) {
    FirebaseAnalytics.getInstance(context).logEvent(
        FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf(
            FirebaseAnalytics.Param.SCREEN_NAME to screenName,
            FirebaseAnalytics.Param.SCREEN_CLASS to screenName
        )
    )
}