package com.apimorlabs.reluct.system.services.notifications

import work.racka.reluct.data.model.domain.appInfo.Icon

data class NotificationData(
    val iconProvider: Icon?,
    val title: String,
    val content: String,
    val notificationId: Int,
    val notificationTag: String,
    val category: String? = null
)
