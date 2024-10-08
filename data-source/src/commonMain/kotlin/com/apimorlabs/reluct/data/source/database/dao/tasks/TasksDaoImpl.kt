package com.apimorlabs.reluct.data.source.database.dao.tasks

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.apimorlabs.reluct.data.source.database.dao.DatabaseWrapper
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksHelpers.asDbObject
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksHelpers.insertAllLabelsToDb
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksHelpers.insertLabelToDb
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksHelpers.insertTaskToDb
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksHelpers.replaceTasksInDb
import com.apimorlabs.reluct.data.source.database.models.TaskDbObject
import com.apimorlabs.reluct.data.source.database.models.TaskLabelDbObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

internal class TasksDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    databaseWrapper: DatabaseWrapper,
) : TasksDao {

    private val tasksQueries = databaseWrapper.instance?.tasksTableQueries
    private val labelQueries = databaseWrapper.instance?.taskLabelsTableQueries

    /**
     * Delete all tasks in the Database then adds the provided ones
     */
    override fun replaceTasks(tasks: List<TaskDbObject>): Boolean =
        tasksQueries?.replaceTasksInDb(tasks) ?: false

    override fun insertTask(task: TaskDbObject) {
        tasksQueries?.insertTaskToDb(task)
    }

    override fun getAllTasks(): Flow<List<TaskDbObject>> {
        val dbTasks = tasksQueries?.getAllTasks()?.asFlow()?.mapToList(dispatcher)
            ?: flowOf(emptyList())
        val dbLabels = getAllTaskLabels()
        return combine(dbTasks, dbLabels) { tasks, labels ->
            withContext(dispatcher) {
                tasks.map { task -> task.asDbObject(labels) }
            }
        }
    }

    override fun getTask(id: String): Flow<TaskDbObject?> {
        val dbTasks = tasksQueries?.getTask(id)?.asFlow()
            ?.mapToOneOrNull(dispatcher) ?: flowOf(null)
        val dbLabels = getAllTaskLabels()
        return combine(dbTasks, dbLabels) { task, labels ->
            withContext(dispatcher) {
                task?.asDbObject(labels)
            }
        }
    }

    override fun searchTasks(query: String, factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        val dbTasks =
            tasksQueries?.searchTasks(query = "%$query%", factor = factor, limitBy = limitBy)
                ?.asFlow()?.mapToList(dispatcher) ?: flowOf(emptyList())
        val dbLabels = getAllTaskLabels()
        return combine(dbTasks, dbLabels) { tasks, labels ->
            withContext(dispatcher) {
                tasks.map { task -> task.asDbObject(labels) }
            }
        }
    }

    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        val dbTasks = tasksQueries?.getPendingTasks(factor, limitBy)
            ?.asFlow()?.mapToList(dispatcher) ?: flowOf(emptyList())
        val dbLabels = getAllTaskLabels()
        return combine(dbTasks, dbLabels) { tasks, labels ->
            withContext(dispatcher) {
                tasks.map { task -> task.asDbObject(labels) }
            }
        }
    }

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        val dbTasks = tasksQueries?.getCompeletedTasks(factor, limitBy)
            ?.asFlow()?.mapToList(dispatcher) ?: flowOf(emptyList())
        val dbLabels = getAllTaskLabels()
        return combine(dbTasks, dbLabels) { tasks, labels ->
            withContext(dispatcher) {
                tasks.map { task -> task.asDbObject(labels) }
            }
        }
    }

    override fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String,
    ): Flow<List<TaskDbObject>> {
        val dbTasks = tasksQueries?.getTasksBetweenDateTimeStrings(
            startLocalDateTime = startLocalDateTime,
            endLocalDateTime = endLocalDateTime,
        )?.asFlow()?.mapToList(dispatcher) ?: flowOf(emptyList())
        val dbLabels = getAllTaskLabels()
        return combine(dbTasks, dbLabels) { tasks, labels ->
            withContext(dispatcher) {
                tasks.map { task -> task.asDbObject(labels) }
            }
        }
    }

    override fun toggleTaskDone(
        id: String,
        isDone: Boolean,
        wasOverDue: Boolean,
        completedLocalDateTime: String?,
    ) {
        tasksQueries?.transaction {
            tasksQueries.toggleTaskDone(
                isDone = isDone,
                wasOverdue = wasOverDue,
                id = id,
                completeLocalDateTime = completedLocalDateTime
            )
        }
    }

    override fun deleteTask(id: String) {
        tasksQueries?.deleteTask(id)
    }

    override fun deleteAllCompletedTasks() {
        tasksQueries?.deleteAllCompletedTasks()
    }

    override fun deleteAll() {
        tasksQueries?.deleteAll()
    }

    override fun addAllTaskLabels(labels: List<TaskLabelDbObject>) {
        labelQueries?.insertAllLabelsToDb(labels)
    }

    override fun addTaskLabel(label: TaskLabelDbObject) {
        labelQueries?.insertLabelToDb(label)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTaskLabels(): Flow<List<TaskLabelDbObject>> =
        labelQueries?.getLables(mapper = TasksHelpers.taskLabelsMapper)?.asFlow()
            ?.mapToList(dispatcher)?.mapLatest { it.asReversed() }
            ?: flowOf(emptyList())

    override fun getTaskLabel(id: String): Flow<TaskLabelDbObject?> =
        labelQueries?.getLabelById(id = id, mapper = TasksHelpers.taskLabelsMapper)?.asFlow()
            ?.mapToOneOrNull(dispatcher) ?: flowOf(null)

    override fun getTaskLabelSync(id: String): TaskLabelDbObject? =
        labelQueries?.getLabelById(id = id, mapper = TasksHelpers.taskLabelsMapper)
            ?.executeAsOneOrNull()

    override fun deleteTaskLabel(id: String) {
        labelQueries?.deleteLabel(id)
    }

    override fun deleteAllTaskLabels() {
        labelQueries?.deleteAll()
    }
}
