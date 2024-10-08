package com.apimorlabs.reluct.compose.ui.components.textFields.search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.clear_icon
import com.apimorlabs.reluct.compose.ui.search_icon
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaterialSearchBar(
    value: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search here",
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    onDismissSearchClicked: () -> Unit = { },
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    defaultPadding: Dp = Dimens.MediumPadding.size,
    smallestPadding: Dp = Dimens.ExtraSmallPadding.size,
    keyboardController: SoftwareKeyboardController? =
        LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester: FocusRequester = remember { FocusRequester() },
    extraButton: @Composable BoxScope.() -> Unit = { },
) {
    var isHintActive by remember {
        mutableStateOf(hint.isNotEmpty())
    }
    val isTyping by remember(value) {
        mutableStateOf(value.isNotBlank())
    }

    val externalPadding: Dp by animateDpAsState(
        targetValue = if (isHintActive) defaultPadding else smallestPadding
    )

    val middlePadding: Dp by animateDpAsState(
        targetValue = if (isHintActive) {
            Dimens.SmallPadding.size
        } else {
            smallestPadding
        }
    )

    val angle: Float by animateFloatAsState(
        targetValue = if (isHintActive) -90F else 0F,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )
    val searchAndOptionsAngle: Float by animateFloatAsState(
        targetValue = if (isHintActive) 0F else 90F,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    val hintAlpha: Float by animateFloatAsState(
        targetValue = if (isHintActive) 1F else 0.5f
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = externalPadding)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = containerColor,
                    shape = shape
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close/Search - Left
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (isHintActive) {
                    IconButton(
                        onClick = { focusRequester.requestFocus() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(Res.string.search_icon),
                            tint = contentColor,
                            modifier = Modifier
                                .rotate(searchAndOptionsAngle)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            onDismissSearchClicked()
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = stringResource(Res.string.clear_icon),
                            tint = contentColor,
                            modifier = Modifier.rotate(angle)
                        )
                    }
                }
            }

            // Search - Center
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .padding(vertical = Dimens.SmallPadding.size)
            ) {
                if (!isTyping) {
                    Text(
                        text = hint,
                        color = contentColor
                            .copy(alpha = hintAlpha),
                        style = textStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primary
                            .copy(alpha = .3f)
                    )
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = {
                            onSearch(it)
                        },
                        maxLines = 1,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        textStyle = textStyle.copy(color = contentColor),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                if (value.isBlank()) {
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = defaultPadding)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isHintActive = !it.isFocused
                            }
                    )
                }
            }
        }

        Spacer(Modifier.width(middlePadding))

        // Extra Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = shape
                )
        ) {
            extraButton(this)
        }
    }
}

@Composable
fun PlaceholderMaterialSearchBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search here",
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    extraButton: @Composable BoxScope.() -> Unit = { },
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(shape)
                .clickable { onClick() }
                .background(color = containerColor),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search - Left
            Box(
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(Res.string.search_icon),
                        tint = contentColor
                    )
                }
            }

            // Search - Center
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .padding(vertical = Dimens.SmallPadding.size)
            ) {
                Text(
                    text = hint,
                    color = contentColor,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.width(Dimens.SmallPadding.size))

        // Extra Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = shape
                )
        ) {
            extraButton(this)
        }
    }
}
