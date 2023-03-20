package com.penguenlabs.pushnote.pushnotification.counter

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotificationCounterModule {

    @Singleton
    @Binds
    fun provideNotificationCounter(notificationCounterImpl: NotificationCounterImpl): NotificationCounter
}