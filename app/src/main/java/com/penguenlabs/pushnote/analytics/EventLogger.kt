package com.penguenlabs.pushnote.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class EventLogger @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun log(event: Event, params: Bundle? = null) {
        firebaseAnalytics.logEvent(event.name, params)
    }
}