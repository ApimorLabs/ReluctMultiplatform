package com.apimorlabs.reluct.navigation.destinations

import com.apimorlabs.reluct.common.models.util.AppURI
import kotlinx.serialization.Serializable

@Serializable
object TasksStatisticsDestination

@Serializable
data class TaskDetailsDestination(val taskId: String?)

@Serializable
data class AddEditTaskDestination(val taskId: String?)

object TaskDetailsLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/tasks"
    fun deepLink(taskId: String) = "$DEEP_LINK?taskId=$taskId"
}

@Serializable
object CompletedTasksDestination

@Serializable
object PendingTasksDestination

object PendingTasksLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/pending_tasks"
}

@Serializable
object SearchTasksDestination
