package work.racka.reluct.data.source.database.dao.goals

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import work.racka.reluct.data.source.database.models.GoalDbObject
import work.racka.reluct.data.source.testData.GoalsTestData

class FakeGoalsDao : GoalsDao {

    private val goalsSource = MutableStateFlow(GoalsTestData.goalsDbObjects)

    override suspend fun insertGoal(goal: GoalDbObject) {
        goalsSource.update { list ->
            list.toMutableList().apply { add(goal) }
                .toList()
        }
    }

    override suspend fun insertGoals(goals: List<GoalDbObject>) {
        goalsSource.update { list ->
            list.toMutableList().apply { addAll(goals) }
                .toList()
        }
    }

    override fun getGoalById(id: String): Flow<GoalDbObject?> {
        return goalsSource.transform { list -> list.firstOrNull { it.id == id } }
    }

    override suspend fun getGoalByIdSync(id: String): GoalDbObject? {
        return goalsSource.firstOrNull()?.firstOrNull { it.id == id }
    }

    override fun getAllGoals(factor: Long, limitBy: Long): Flow<List<GoalDbObject>> =
        goalsSource.asStateFlow()

    override fun getActiveGoals(factor: Long, limitBy: Long): Flow<List<GoalDbObject>> =
        goalsSource.transform { list -> list.filter { it.isActive } }

    override fun getInActiveGoals(factor: Long, limitBy: Long): Flow<List<GoalDbObject>> =
        goalsSource.transform { list -> list.filterNot { it.isActive } }

    override suspend fun toggleGoalActiveState(id: String, isActive: Boolean) {
        goalsSource.update { list ->
            val item = getGoalByIdSync(id)?.copy(isActive = isActive)
            list.toMutableList().apply {
                item?.let { goal ->
                    removeAll { it.id == id }
                    add(goal)
                }
            }.toList()
        }
    }

    override suspend fun updateGoalCurrentValue(id: String, currentValue: Long) {
        goalsSource.update { list ->
            val item = getGoalByIdSync(id)?.copy(currentValue = currentValue)
            list.toMutableList().apply {
                item?.let { goal ->
                    removeAll { it.id == id }
                    add(goal)
                }
            }.toList()
        }
    }

    override suspend fun deleteGoal(id: String) {
        goalsSource.update { list ->
            list.toMutableList().apply { removeAll { it.id == id } }.toList()
        }
    }

    override suspend fun deleteAllInActiveGoals(id: String) {
        goalsSource.update { list ->
            list.toMutableList().apply { removeAll { !it.isActive } }.toList()
        }
    }

    override suspend fun deleteAllActiveGoals(id: String) {
        goalsSource.update { list ->
            list.toMutableList().apply { removeAll { it.isActive } }.toList()
        }
    }

    override suspend fun deleteAllGoals() {
        goalsSource.update { listOf() }
    }
}