package com.apimorlabs.reluct.screens.tasks.completed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.CollapsingFloatingButton
import com.apimorlabs.reluct.compose.ui.components.buttons.ScrollToTop
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.EntryType
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.TaskEntry
import com.apimorlabs.reluct.compose.ui.components.dialogs.FullScreenLoading
import com.apimorlabs.reluct.compose.ui.new_task_button_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.util.BarsVisibility
import com.apimorlabs.reluct.compose.ui.util.BottomBarVisibilityHandler
import com.apimorlabs.reluct.compose.ui.util.FetchMoreDataHandler
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.features.tasks.states.CompletedTasksState
import com.apimorlabs.reluct.screens.tasks.components.FullEmptyTasksIndicator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CompletedTasksUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: State<CompletedTasksState>,
    onTaskClicked: (task: Task) -> Unit,
    onAddTaskClicked: (task: Task?) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    fetchMoreData: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val scope = rememberCoroutineScope()

    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            uiState.value.shouldUpdateData && uiState.value !is CompletedTasksState.Loading
        },
        onFetchData = fetchMoreData
    )

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
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
                text = stringResource(Res.string.new_task_button_text),
                icon = Icons.Rounded.Add,
                onClick = { onAddTaskClicked(null) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size),
            contentAlignment = Alignment.Center
        ) {
            FullScreenLoading(
                modifier = Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding()),
                isLoadingProvider = {
                    uiState.value is CompletedTasksState.Loading &&
                        uiState.value.tasksData.isEmpty()
                }
            )

            // Show Empty Graphic
            FullEmptyTasksIndicator(
                showAnimationProvider = {
                    uiState.value !is CompletedTasksState.Loading &&
                        uiState.value.tasksData.isEmpty()
                },
                modifier = Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding())
            )

            // Tasks
            CompletedTasksLazyList(
                uiStateProvider = { uiState.value },
                listState = listState,
                onTaskClicked = onTaskClicked,
                onToggleTaskDone = onToggleTaskDone,
                mainScaffoldPadding = mainScaffoldPadding
            )

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CompletedTasksLazyList(
    uiStateProvider: () -> CompletedTasksState,
    listState: LazyListState,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    mainScaffoldPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val uiState = remember { derivedStateOf { uiStateProvider() } }
    val isLoading = remember {
        derivedStateOf {
            uiState.value is CompletedTasksState.Loading && uiState.value.tasksData.isNotEmpty()
        }
    }
    val showData = remember {
        derivedStateOf { uiState.value.tasksData.isNotEmpty() }
    }

    AnimatedVisibility(
        visible = showData.value,
        modifier = modifier.fillMaxSize(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size)
        ) {
            uiState.value.tasksData.forEach { taskGroup ->
                stickyHeader {
                    ListGroupHeadingHeader(text = taskGroup.key)
                }

                items(
                    items = taskGroup.value,
                    key = { it.id }
                ) { item ->
                    TaskEntry(
                        task = item,
                        entryType = EntryType.CompletedTask,
                        onEntryClick = { onTaskClicked(item) },
                        onCheckedChange = { onToggleTaskDone(item, it) }
                    )
                }
            }

            // Loading when fetching more data
            item {
                if (isLoading.value) {
                    LinearProgressIndicator()
                }
            }

            // Bottom Space for spaceBy
            // Needed so that the load more indicator is shown
            item {
                Spacer(
                    modifier = Modifier.padding(mainScaffoldPadding)
                )
            }
        }
    }
}
