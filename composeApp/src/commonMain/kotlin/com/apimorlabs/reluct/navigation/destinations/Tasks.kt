package com.apimorlabs.reluct.navigation.destinations

import com.apimorlabs.reluct.common.models.util.AppURI
import kotlinx.serialization.Serializable

@Serializable
data object TasksStatisticsDestination

@Serializable
data class TaskDetailsDestination(val taskId: String?)

@Serializable
data class AddEditTaskDestination(val taskId: String?)

@Serializable
data object CompletedTasksDestination

@Serializable
data object PendingTasksDestination

@Serializable
data object SearchTasksDestination

// Deep-links
object TaskDetailsLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/tasks"
    fun deepLink(taskId: String) = "$DEEP_LINK?taskId=$taskId"
}

object PendingTasksLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/pending_tasks"
}
