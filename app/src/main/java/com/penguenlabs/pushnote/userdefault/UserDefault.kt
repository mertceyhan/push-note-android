package com.penguenlabs.pushnote.userdefault

interface UserDefault {
    fun setUserDefault(defaultValue: Boolean)
    fun getUserDefault(): Boolean
}