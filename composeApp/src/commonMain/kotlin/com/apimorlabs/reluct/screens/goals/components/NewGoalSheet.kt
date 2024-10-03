package com.apimorlabs.reluct.screens.goals.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.add_your_goal_text
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.goalEntry.GoalEntry
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.examples_text
import com.apimorlabs.reluct.compose.ui.new_goal_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.features.goals.states.DefaultGoals
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NewGoalSheet(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 6.dp,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit
) {
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
        shape = Shapes.large
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .padding(top = Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        ) {
            stickyHeader {
                ListGroupHeadingHeader(
                    text = stringResource(Res.string.new_goal_text),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    textAlign = TextAlign.Center
                )
            }

            item {
                ReluctButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = Shapes.large,
                    buttonText = stringResource(Res.string.add_your_goal_text),
                    icon = Icons.Rounded.Add,
                    onButtonClicked = { onAddGoal(null) }
                )
            }

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(Res.string.examples_text),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // NOTE: Using the LazyList itemsIndexed won't give you the correct index from the list
            DefaultGoals.predefined().forEachIndexed { index, goal ->
                item {
                    GoalEntry(goal = goal, onEntryClick = { onAddGoal(index) })
                }
            }

            item { Spacer(modifier = Modifier) }
        }
    }
}
