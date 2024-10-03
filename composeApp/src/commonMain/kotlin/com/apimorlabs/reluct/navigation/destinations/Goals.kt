package com.apimorlabs.reluct.navigation.destinations

import com.apimorlabs.reluct.common.models.util.AppURI
import kotlinx.serialization.Serializable

@Serializable
object InactiveGoalsDestination

@Serializable
object ActiveGoalsDestination

@Serializable
data class AddEditGoalDestination(val goalId: String?, val defaultGoalIndex: Int = -1)

object AddEditGoalLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/add_edit_goal"
    fun deepLink(goalId: String, defaultGoalIndex: Int = -1) =
        "$DEEP_LINK?goalId=$goalId?defaultGoalIndex=$defaultGoalIndex"
}

@Serializable
data class GoalDetailsDestination(val goalId: String?)

object GoalDetailsLink {
    const val DEEP_LINK = "${AppURI.BASE_URI}/goals"
    fun deepLink(goalId: String) = "$DEEP_LINK?goalId=$goalId"
}
