package com.penguenlabs.pushnote.analytics

sealed class Event(val name: String) {
    object Push : Event(name = "push") {
        const val PARAM_KEY_PUSH_NOTIFICATION_TEXT = "push_notification_text"
        const val PARAM_KEY_IS_PINNED_NOTE = "is_pinned_note"
    }

    object ShareApplication : Event(name = "share_application")
    object ReportProblem : Event(name = "report_problem")
    object RateUs : Event(name = "rate_us")
    object HistoryDelete : Event(name = "delete") {
        const val PARAM_KEY_DELETED_ITEM_COUNT = "deleted_item_count"
    }

    object HistorySelectAll : Event(name = "history_select_all")
    object HistoryDeselectAll : Event(name = "history_deselect_all")
    object HistoryOnLongClicked : Event(name = "history_on_long_clicked")
    object InAppReviewLaunched : Event(name = "in_app_review_launched")

    /**
     * Represents an event when a user's device restarts successfully.
     */
    object BootReceived : Event(name = "boot_received")

    /**
     * Represents an event when a user's device restarts, and active notifications are saved and sent again successfully.
     */
    object NotificationsSavedAfterBoot : Event(name = "notifications_saved_after_boot")

    /**
     * Represents an event when a user clicks on the "Copy" button for a notification.
     */
    object Copy : Event(name = "copy")

    /**
     * Represents an event when a user clicks on the "Unpin" button for a pinned note.
     */
    object Unpin : Event(name = "unpin")

    /**
     * Represents an event when a user cancels their notification by swiping or using the "Clear All" button.
     */
    object NotificationCancelled : Event(name = "notification_cancelled")
}