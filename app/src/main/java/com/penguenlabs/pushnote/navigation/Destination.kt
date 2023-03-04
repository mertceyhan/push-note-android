package com.penguenlabs.pushnote.navigation

sealed class Destination(val route: String) {
    object Home : Destination(route = "home") {
        const val PARAM_PUSH_NOTIFICATION_TEXT = "param_push_notification_text"
    }

    object Settings : Destination(route = "settings")
    object History : Destination(route = "history")
    object Theme : Destination(route = "theme")
    object Color : Destination(route = "color")
    object Typography : Destination(route = "typography")

    object NotificationPermission : Destination(route = "notification_permission") {
        const val PARAM_PUSH_NOTIFICATION_TEXT = "param_push_notification_text"
    }
}