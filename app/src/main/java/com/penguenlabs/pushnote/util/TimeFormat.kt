package com.penguenlabs.pushnote.util

import java.text.DateFormat

object TimeFormat {

    private val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)

    fun format(time: Long): String {
        return dateFormat.format(time)
    }
}