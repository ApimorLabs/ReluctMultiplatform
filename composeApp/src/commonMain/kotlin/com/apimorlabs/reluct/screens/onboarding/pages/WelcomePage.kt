package com.apimorlabs.reluct.screens.onboarding.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_name
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.components.textFields.text.HighlightTextProps
import com.apimorlabs.reluct.compose.ui.components.textFields.text.HighlightedText
import com.apimorlabs.reluct.compose.ui.goals_highlight_text
import com.apimorlabs.reluct.compose.ui.tasks_highlight_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.welcome_mobile
import com.apimorlabs.reluct.compose.ui.welcome_text
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WelcomePage(
    modifier: Modifier = Modifier
) {
    val drawableSize = 400.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(Res.string.app_name),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize)
                    .padding(Dimens.MediumPadding.size),
                painter = painterResource(Res.drawable.welcome_mobile),
                contentDescription = null
            )
        }

        item {
            HighlightedText(
                fullText = stringResource(Res.string.welcome_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.titleLarge,
                highlights = persistentListOf(
                    HighlightTextProps(
                        text = stringResource(Res.string.tasks_highlight_text),
                        color = MaterialTheme.colorScheme.primary
                    ),
                    HighlightTextProps(
                        text = stringResource(Res.string.goals_highlight_text),
                        color = Color.Green
                    )
                )
            )
        }
    }
}
