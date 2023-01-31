package com.penguenlabs.pushnote.analytics

sealed class Event(val name: String) {
    object Push : Event(name = "push") {
        const val PARAM_KEY_PUSH_NOTIFICATION_TEXT = "push_notification_text"
        const val PARAM_KEY_IS_PINNED_NOTE = "is_pinned_note"
    }

    object ShareApplication : Event(name = "share_application")
    object ReportProblem : Event(name = "report_problem")
    object RateUs : Event(name = "rate_us")
    object Copy : Event(name = "copy")
    object Share : Event(name = "share")
    object Delete : Event(name = "delete")
}