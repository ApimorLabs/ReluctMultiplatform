package com.apimorlabs.reluct.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.ok
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.themes_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun ThemesDialog(
    openDialog: State<Boolean>,
    onDismiss: () -> Unit,
    currentTheme: Int,
    onSaveTheme: (value: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val bottomButtonHeight = Dimens.ExtraLargePadding.size
    if (openDialog.value) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                modifier = modifier,
                shape = Shapes.large,
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .padding(Dimens.MediumPadding.size),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = bottomButtonHeight + Dimens.SmallPadding.size)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ListGroupHeadingHeader(text = stringResource(Res.string.themes_text))

                        getThemes().forEach { item ->
                            ThemeSelectCard(
                                themeData = item,
                                isSelected = currentTheme == item.theme.themeValue,
                                onSelectTheme = onSaveTheme
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Dimens.SmallPadding.size)
                                .height(bottomButtonHeight),
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
}