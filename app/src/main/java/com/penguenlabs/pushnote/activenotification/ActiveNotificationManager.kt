package com.penguenlabs.pushnote.activenotification

import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.history.data.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 * Manages active notifications and provides methods to mark notifications as cancelled.
 */
interface ActiveNotificationManager {

    /**
     * Marks the notification with the specified ID as cancelled in the system.
     *
     * @param notificationId The ID of the notification to be cancelled.
     */
    suspend fun markAsCancelledById(notificationId: Long)

    /**
     * Retrieves a list of currently active notifications.
     *
     * @return A list of [HistoryEntity] objects representing active notifications.
     */
    suspend fun getActiveNotifications(): List<HistoryEntity>
}


class ActiveNotificationManagerImpl @Inject constructor(
    private val historyRepository: HistoryRepository
) : ActiveNotificationManager {

    override suspend fun markAsCancelledById(notificationId: Long) {
        return historyRepository.markAsCancelled(notificationId)
    }

    override suspend fun getActiveNotifications(): List<HistoryEntity> {
        return historyRepository.getActiveHistoryEntities()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface ActiveNotificationManagerModule {

    @Binds
    fun bindActiveNotificationManagerImpl(
        activeNotificationManagerImpl: ActiveNotificationManagerImpl
    ): ActiveNotificationManager
}


