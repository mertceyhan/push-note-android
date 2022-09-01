package com.penguenlabs.pushnote.analytics

sealed class Event(val name: String) {
    object Push : Event(name = "push")
    object ShareApplication : Event(name = "share_application")
    object ReportProblem : Event(name = "report_problem")
    object RateUs : Event(name = "rate_us")
    object Copy : Event(name = "copy")
    object Share : Event(name = "share")
    object Delete : Event(name = "delete")
}