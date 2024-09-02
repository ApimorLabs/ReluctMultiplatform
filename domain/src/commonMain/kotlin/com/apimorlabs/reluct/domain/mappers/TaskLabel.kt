package com.apimorlabs.reluct.domain.mappers

import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.data.source.database.models.TaskLabelDbObject

fun TaskLabelDbObject.asTaskLabel() = TaskLabel(
    id = id,
    name = name,
    description = description,
    colorHexString = colorHexString
)

fun TaskLabel.asTaskLabelDbObject() = TaskLabelDbObject(
    id = id,
    name = name,
    description = description,
    colorHexString = colorHexString
)
