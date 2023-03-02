package com.penguenlabs.pushnote.navigation

sealed class Destination(val route: String) {
    object Home : Destination(route = "home")
    object Settings : Destination(route = "settings")
    object History : Destination(route = "history")
    object Theme : Destination(route = "theme")
    object Color : Destination(route = "color")
    object Typography : Destination(route = "typography")
}