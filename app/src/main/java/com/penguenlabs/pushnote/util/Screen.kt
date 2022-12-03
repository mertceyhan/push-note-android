package com.penguenlabs.pushnote.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.penguenlabs.pushnote.navigation.Destination
import kotlinx.coroutines.CoroutineScope

@Composable
fun Screen(
    context: Context = LocalContext.current,
    destination: Destination,
    launchedEffect: suspend CoroutineScope.() -> Unit = {
        logScreenViewEvent(
            context = context,
            screenName = destination.route
        )
    },
    content: @Composable () -> Unit
) {
    content()
    LaunchedEffect(Unit, launchedEffect)
}

fun logScreenViewEvent(context: Context, screenName: String) {
    FirebaseAnalytics.getInstance(context).logEvent(
        FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf(
            FirebaseAnalytics.Param.SCREEN_NAME to screenName,
            FirebaseAnalytics.Param.SCREEN_CLASS to screenName
        )
    )
}