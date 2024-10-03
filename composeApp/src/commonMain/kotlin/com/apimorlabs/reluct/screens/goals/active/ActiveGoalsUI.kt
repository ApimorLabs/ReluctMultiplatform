package com.apimorlabs.reluct.screens.goals.active

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.CollapsingFloatingButton
import com.apimorlabs.reluct.compose.ui.components.buttons.ScrollToTop
import com.apimorlabs.reluct.compose.ui.components.cards.goalEntry.GoalEntry
import com.apimorlabs.reluct.compose.ui.new_goal_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.features.goals.states.ActiveGoalsState
import com.apimorlabs.reluct.features.goals.states.GoalsListState
import com.apimorlabs.reluct.screens.goals.components.EmptyGoalsIndicator
import com.apimorlabs.reluct.screens.goals.components.NewGoalSheet
import com.apimorlabs.reluct.screens.util.BottomBarVisibilityHandler
import com.apimorlabs.reluct.screens.util.FetchMoreDataHandler
import com.apimorlabs.reluct.screens.util.getSnackbarModifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ActiveGoalsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: State<ActiveGoalsState>,
    fetchMoreData: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onGoalClicked: (Goal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val showNewGoalDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // React to scroll position
    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            val canUpdate = uiState.value.goalsListState.shouldUpdateData
            val isNotLoading =
                uiState.value.goalsListState !is GoalsListState.Loading && !uiState.value.isSyncing
            canUpdate && isNotLoading
        },
        onFetchData = fetchMoreData
    )

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    val showEmptyIndicator = remember {
        derivedStateOf {
            uiState.value.goalsListState.goals.isEmpty() &&
                    uiState.value.goalsListState !is GoalsListState.Loading
        }
    }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = snackbarModifier.value,
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            CollapsingFloatingButton(
                scrollContextState = scrollContext,
                mainScaffoldPadding = mainScaffoldPadding,
                text = stringResource(Res.string.new_goal_text),
                icon = Icons.Rounded.Add,
                onClick = { showNewGoalDialog.value = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size),
            contentAlignment = Alignment.Center
        ) {
            // Center Loading Indicator
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState.value.goalsListState is GoalsListState.Loading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(mainScaffoldPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Empty Goals Indicator
            EmptyGoalsIndicator(
                showAnimationProvider = { showEmptyIndicator.value },
                modifier = Modifier.padding(mainScaffoldPadding)
            )

            if (!showEmptyIndicator.value) {
                // Show Active Goals
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.SmallPadding.size)
                ) {
                    // Top Space
                    item { Spacer(modifier = Modifier) }

                    // Goals
                    showActiveGoals(
                        uiStateProvider = { uiState.value },
                        onGoalClicked = onGoalClicked
                    )

                    // Bottom Space for spaceBy
                    item {
                        Spacer(
                            modifier = Modifier.padding(mainScaffoldPadding)
                        )
                    }
                }
            }

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }

        NewGoalDialog(
            openDialog = showNewGoalDialog,
            onClose = { showNewGoalDialog.value = false },
            onAddGoal = onAddGoal
        )
    }
}

@Composable
internal fun NewGoalDialog(
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit
) {
    if (openDialog.value) {
        Dialog(onDismissRequest = onClose) {
            NewGoalSheet(
                modifier = Modifier.height(500.dp),
                onAddGoal = onAddGoal
            )
        }
    }
}

private fun LazyListScope.showActiveGoals(
    uiStateProvider: () -> ActiveGoalsState,
    onGoalClicked: (Goal) -> Unit
) {
    // Syncing Goals Data Loading
    item {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = uiStateProvider().isSyncing,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator()
            }
        }
    }

    items(
        items = uiStateProvider().goalsListState.goals,
        key = { it.id }
    ) { item ->
        GoalEntry(
            modifier = Modifier.animateItem(),
            goal = item,
            onEntryClick = { onGoalClicked(item) }
        )
    }

    // Loading when fetching more data
    item {
        if (uiStateProvider().goalsListState is GoalsListState.Loading &&
            uiStateProvider().goalsListState.goals.isNotEmpty() && !uiStateProvider().isSyncing
        ) {
            LinearProgressIndicator()
        }
    }
}