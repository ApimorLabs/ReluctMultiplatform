package com.apimorlabs.reluct.data.source.database.dao.tasks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import com.apimorlabs.reluct.data.source.database.models.TaskLabelDbObject
import com.apimorlabs.reluct.data.source.testData.TasksTestData

class FakeTasksDao : TasksDao {

    private val tasksSource = MutableStateFlow(TasksTestData.taskDbObjects)

    private val taskLabels = MutableStateFlow(TasksTestData.taskLabels)

    override fun replaceTasks(tasks: List<TaskDbObject>): Boolean {
        tasksSource.update { tasks }
        return tasksSource.value.containsAll(tasks)
    }

    override fun insertTask(task: TaskDbObject) {
        tasksSource.update {
            it.toMutableList().apply {
                add(task)
            }.toList()
        }
    }

    override fun getAllTasks(): Flow<List<TaskDbObject>> = tasksSource.asStateFlow()

    override fun getTask(id: String): Flow<TaskDbObject?> =
        tasksSource.transform { list -> list.firstOrNull { it.id == id } }

    override fun searchTasks(query: String, factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return tasksSource.transform { it.take(2) }
    }

    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return tasksSource.transform { list -> list.filterNot { it.done } }
    }

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return tasksSource.transform { list -> list.filter { it.done } }
    }

    override fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String
    ): Flow<List<TaskDbObject>> {
        val timeRangeString = startLocalDateTime..endLocalDateTime
        return tasksSource
            .transform { list -> list.filter { timeRangeString.contains(it.dueDateLocalDateTime) } }
    }

    override fun toggleTaskDone(
        id: String,
        isDone: Boolean,
        wasOverDue: Boolean,
        completedLocalDateTime: String?
    ) {
        tasksSource.update { list ->
            val item = list.firstOrNull { it.id == id }?.copy(
                done = isDone,
                overdue = wasOverDue,
                completedLocalDateTime = completedLocalDateTime
            )
            list.toMutableList().apply {
                item?.let { task ->
                    removeAll { it.id == id }
                    add(task)
                }
            }.toList()
        }
    }

    override fun deleteTask(id: String) {
        tasksSource.update { list -> list.toMutableList().apply { removeAll { it.id == id } }.toList() }
    }

    override fun deleteAllCompletedTasks() {
        tasksSource.update { list -> list.toMutableList().apply { removeAll { it.done } }.toList() }
    }

    override fun deleteAll() {
        tasksSource.update { listOf() }
    }

    override fun addTaskLabel(label: TaskLabelDbObject) {
        taskLabels.update { list -> list.toMutableList().apply { add(label) }.toList() }
    }

    override fun addAllTaskLabels(labels: List<TaskLabelDbObject>) {
        taskLabels.update { it + labels }
    }

    override fun getAllTaskLabels(): Flow<List<TaskLabelDbObject>> = taskLabels.asStateFlow()

    override fun getTaskLabel(id: String): Flow<TaskLabelDbObject?> =
        taskLabels.transform { list -> list.firstOrNull { it.id == id } }

    override fun getTaskLabelSync(id: String): TaskLabelDbObject? =
        taskLabels.value.firstOrNull { it.id == id }

    override fun deleteAllTaskLabels() {
        taskLabels.update { listOf() }
    }

    override fun deleteTaskLabel(id: String) {
        taskLabels.update { list -> list.toMutableList().apply { removeAll { it.id == id } } }
    }
}