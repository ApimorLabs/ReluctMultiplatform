package com.apimorlabs.reluct.features.tasks.addEdit

import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.util.time.TimeUtils.plus
import com.apimorlabs.reluct.common.models.util.UUIDGen
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DefaultTasks {
    fun emptyEditTask(): EditTask {
        val advancedTime = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .plus(hours = 1)

        return EditTask(
            id = UUIDGen.getString(),
            title = "",
            description = "",
            done = false,
            overdue = false,
            dueDateLocalDateTime = advancedTime.toString(),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        )
    }
}
