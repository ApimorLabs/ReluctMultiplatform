package com.apimorlabs.reluct.screens.screentime.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowDown
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.no_apps_text
import com.apimorlabs.reluct.compose.ui.ok
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@Composable
fun ManageAppsDialog(
    openDialog: State<Boolean>,
    onDismiss: () -> Unit,
    isLoadingProvider: () -> Boolean,
    topItemsHeading: String,
    bottomItemsHeading: String,
    topItems: () -> ImmutableList<AppInfo>,
    bottomItems: () -> ImmutableList<AppInfo>,
    onTopItemClicked: (app: AppInfo) -> Unit,
    onBottomItemClicked: (app: AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                modifier = modifier,
                shape = Shapes.large,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.MediumPadding.size),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ManageAppsUI(
                        modifier = Modifier.height(480.dp),
                        isLoading = isLoadingProvider(),
                        topItemsHeading = topItemsHeading,
                        bottomItemsHeading = bottomItemsHeading,
                        topItems = topItems(),
                        bottomItems = bottomItems(),
                        onTopItemClicked = onTopItemClicked,
                        onBottomItemClicked = onBottomItemClicked,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        ReluctButton(
                            buttonText = stringResource(Res.string.ok),
                            icon = Icons.Rounded.Done,
                            onButtonClicked = onDismiss
                        )
                    }
                }
            }
        }
    }
}

/** This is a LazyColum, be careful when nesting it inside other Columns **/
@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun ManageAppsUI(
    isLoading: Boolean,
    topItemsHeading: String,
    bottomItemsHeading: String,
    topItems: ImmutableList<AppInfo>,
    bottomItems: ImmutableList<AppInfo>,
    onTopItemClicked: (app: AppInfo) -> Unit,
    onBottomItemClicked: (app: AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentColor: Color = LocalContentColor.current,
    headerTonalElevation: Dp = 0.dp,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = topItemsHeading,
                contentColor = contentColor,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = headerTonalElevation
            )
        }

        if (isLoading) {
            item {
                LinearProgressIndicator(Modifier.padding(Dimens.LargePadding.size))
            }
        } else if (topItems.isEmpty()) {
            item {
                Text(
                    modifier = Modifier
                        .padding(Dimens.LargePadding.size),
                    text = stringResource(Res.string.no_apps_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(items = topItems, key = { it.packageName }) { item ->
                AppNameEntry(
                    modifier = Modifier
                        .animateItem()
                        .clip(Shapes.large)
                        .clickable { onTopItemClicked(item) },
                    appName = item.appName,
                    icon = item.appIcon.icon.decodeToImageBitmap(),
                    contentColor = contentColor
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardDoubleArrowDown,
                        contentDescription = "Move Down",
                        tint = contentColor
                    )
                }
            }
        }

        stickyHeader {
            ListGroupHeadingHeader(
                text = bottomItemsHeading,
                contentColor = contentColor,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = headerTonalElevation
            )
        }

        if (isLoading) {
            item {
                LinearProgressIndicator(Modifier.padding(Dimens.LargePadding.size))
            }
        } else if (bottomItems.isEmpty()) {
            item {
                Text(
                    modifier = Modifier
                        .padding(Dimens.LargePadding.size),
                    text = stringResource(Res.string.no_apps_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(items = bottomItems, key = { it.packageName }) { item ->
                AppNameEntry(
                    modifier = Modifier
                        .animateItem()
                        .clip(Shapes.large)
                        .clickable { onBottomItemClicked(item) },
                    appName = item.appName,
                    icon = item.appIcon.icon.decodeToImageBitmap(),
                    contentColor = contentColor
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardDoubleArrowUp,
                        contentDescription = "Move Up",
                        tint = contentColor
                    )
                }
            }
        }
    }
}
