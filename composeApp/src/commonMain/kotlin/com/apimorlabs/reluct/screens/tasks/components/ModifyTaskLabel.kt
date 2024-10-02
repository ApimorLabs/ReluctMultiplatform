package com.apimorlabs.reluct.screens.tasks.components

import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel

internal sealed class ModifyTaskLabel {
    class Delete(val label: TaskLabel) : ModifyTaskLabel()
    class SaveLabel(val label: TaskLabel) : ModifyTaskLabel()
}