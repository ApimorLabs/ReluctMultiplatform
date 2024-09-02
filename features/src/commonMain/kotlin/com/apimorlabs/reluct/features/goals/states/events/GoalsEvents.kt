package com.apimorlabs.reluct.features.goals.states.events

sealed class GoalsEvents {
    data object Nothing : GoalsEvents()
    class GoalSavedMessage(val goalName: String) : GoalsEvents()
    class ChangedGoalState(val isActive: Boolean, val msg: String) : GoalsEvents()
    class DeletedGoal(val goalId: String, val goalName: String) : GoalsEvents()
    data object Exit : GoalsEvents()
}
