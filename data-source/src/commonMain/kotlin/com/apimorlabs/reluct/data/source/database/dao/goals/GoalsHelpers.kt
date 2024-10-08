package com.apimorlabs.reluct.data.source.database.dao.goals

import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.data.source.database.models.GoalDbObject
import com.apimorlabs.reluct.data.source.database.tables.GoalsTable
import com.apimorlabs.reluct.data.source.database.tables.GoalsTableQueries

object GoalsHelpers {

    fun GoalsTableQueries.insertGoalToDb(goal: GoalDbObject) {
        transaction { insertGoal(goal.asGoalsTable()) }
    }

    fun GoalsTableQueries.insertAllGoalsToDb(goals: List<GoalDbObject>) {
        transaction {
            goals.forEach { goal -> insertGoal(goal.asGoalsTable()) }
        }
    }

    val goalDbObjectMapper: (
        id: String,
        name: String,
        description: String,
        isActive: Boolean,
        relatedApps: List<String>,
        targetValue: Long,
        currentValue: Long,
        goalInterval: GoalInterval,
        timeInterval: LongRange?,
        daysOfWeekSelected: List<Week>,
        goalType: GoalType
    ) -> GoalDbObject = { id, name, description, isActive, relatedApps, targetValue, currentValue,
            goalInterval, timeInterval, daysOfWeekSelected, goalType ->
        GoalDbObject(
            id = id,
            name = name,
            description = description,
            isActive = isActive,
            relatedApps = relatedApps,
            targetValue = targetValue,
            currentValue = currentValue,
            goalInterval = goalInterval,
            timeInterval = timeInterval,
            daysOfWeekSelected = daysOfWeekSelected,
            goalType = goalType
        )
    }

    private fun GoalDbObject.asGoalsTable() = GoalsTable(
        id = id,
        name = name,
        description = description,
        isActive = isActive,
        relatedApps = relatedApps,
        targetValue = targetValue,
        currentValue = currentValue,
        goalInterval = goalInterval,
        timeInterval = timeInterval,
        daysOfWeekSelected = daysOfWeekSelected,
        goalType = goalType
    )
}
