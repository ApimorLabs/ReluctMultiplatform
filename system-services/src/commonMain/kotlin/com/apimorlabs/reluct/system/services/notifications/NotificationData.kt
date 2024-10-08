package com.apimorlabs.reluct.system.services.notifications

import com.apimorlabs.reluct.common.models.domain.appInfo.Icon

data class NotificationData(
    val iconProvider: Icon?,
    val title: String,
    val content: String,
    val notificationId: Int,
    val notificationTag: String,
    val category: String? = null
)
