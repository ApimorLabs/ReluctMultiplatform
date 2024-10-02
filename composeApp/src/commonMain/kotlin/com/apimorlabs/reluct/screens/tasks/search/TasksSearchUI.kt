package com.apimorlabs.reluct.screens.tasks.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ScrollToTop
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.EntryType
import com.apimorlabs.reluct.compose.ui.components.cards.taskEntry.TaskEntry
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.components.textFields.search.MaterialSearchBar
import com.apimorlabs.reluct.compose.ui.file_search
import com.apimorlabs.reluct.compose.ui.search_not_found_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.util.FetchMoreDataHandler
import com.apimorlabs.reluct.compose.ui.util.rememberScrollContext
import com.apimorlabs.reluct.features.tasks.states.SearchData
import com.apimorlabs.reluct.features.tasks.states.SearchTasksState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TasksSearchUI(
    snackbarState: SnackbarHostState,
    uiState: State<SearchTasksState>,
    fetchMoreData: () -> Unit,
    onSearch: (query: String) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val focusRequester = remember {
        FocusRequester()
    }

    val scope = rememberCoroutineScope()

    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            uiState.value.shouldUpdateData && uiState.value.searchData !is SearchData.Loading
        },
        onFetchData = fetchMoreData
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MaterialSearchBar(
                modifier = Modifier
                    .padding(vertical = Dimens.SmallPadding.size)
                    .statusBarsPadding(),
                value = uiState.value.searchQuery,
                onSearch = { onSearch(it) },
                onDismissSearchClicked = { onSearch("") },
                focusRequester = focusRequester
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            // Show Empty Graphic
            AnimatedVisibility(
                visible = uiState.value.searchData is SearchData.Empty,
                modifier = Modifier.fillMaxWidth(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ImageWithDescription(
                        painter = painterResource(Res.drawable.file_search),
                        imageSize = 200.dp,
                        description = stringResource(Res.string.search_not_found_text),
                        descriptionTextStyle = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Tasks
            TasksLazyColumn(
                uiStateProvider = { uiState.value },
                onTaskClicked = onTaskClicked,
                onToggleTaskDone = onToggleTaskDone,
                listState = listState
            )

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }
    }
}

@Composable
private fun TasksLazyColumn(
    uiStateProvider: () -> SearchTasksState,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val uiState = remember { derivedStateOf { uiStateProvider() } }

    AnimatedVisibility(
        visible = uiState.value.searchData !is SearchData.Empty,
        modifier = modifier.fillMaxSize(),
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) { // Show Searched Tasks
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size)
        ) {
            items(
                items = uiState.value.searchData.tasksData,
                key = { it.id }
            ) { item ->
                TaskEntry(
                    modifier = Modifier.animateItem(),
                    task = item,
                    entryType = EntryType.CompletedTask,
                    onEntryClick = { onTaskClicked(item) },
                    onCheckedChange = { onToggleTaskDone(item, it) }
                )
            }

            // Loading when fetching more data
            item {
                if (uiState.value.searchData is SearchData.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(Dimens.MediumPadding.size)
                    )
                }
            }

            // Bottom Space for spaceBy
            // Needed so that the load more indicator is shown
            item {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = Dimens.ExtraLargePadding.size)
                        .navigationBarsPadding()
                )
            }
        }
    }
}
