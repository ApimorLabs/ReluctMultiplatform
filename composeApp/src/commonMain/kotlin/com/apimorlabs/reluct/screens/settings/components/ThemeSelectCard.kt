package com.apimorlabs.reluct.screens.settings.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctDescriptionCard
import com.apimorlabs.reluct.compose.ui.dark_theme_text
import com.apimorlabs.reluct.compose.ui.dark_theme_text_desc
import com.apimorlabs.reluct.compose.ui.default_theme_system
import com.apimorlabs.reluct.compose.ui.default_theme_system_desc
import com.apimorlabs.reluct.compose.ui.light_theme_text
import com.apimorlabs.reluct.compose.ui.light_theme_text_desc
import com.apimorlabs.reluct.compose.ui.material_you_theme_text
import com.apimorlabs.reluct.compose.ui.material_you_theme_text_desc
import com.apimorlabs.reluct.compose.ui.theme.Theme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ThemeSelectCard(
    themeData: ThemeHolder,
    isSelected: Boolean,
    onSelectTheme: (themeValue: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    ReluctDescriptionCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        title = {
            Text(
                text = stringResource(themeData.themeNameRes),
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current
            )
        },
        description = {
            Text(
                text = stringResource(themeData.themeDescriptionRes),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
        },
        leftItems = { Icon(imageVector = themeData.theme.icon, contentDescription = null) },
        onClick = { onSelectTheme(themeData.theme.themeValue) }
    )
}

internal data class ThemeHolder(
    val theme: Theme,
    val themeNameRes: StringResource,
    val themeDescriptionRes: StringResource,
)

internal fun getThemes() = arrayOf(
    ThemeHolder(
        theme = Theme.FOLLOW_SYSTEM,
        themeNameRes = Res.string.default_theme_system,
        themeDescriptionRes = Res.string.default_theme_system_desc
    ),
    ThemeHolder(
        theme = Theme.MATERIAL_YOU,
        themeNameRes = Res.string.material_you_theme_text,
        themeDescriptionRes = Res.string.material_you_theme_text_desc
    ),
    ThemeHolder(
        theme = Theme.LIGHT_THEME,
        themeNameRes = Res.string.light_theme_text,
        themeDescriptionRes = Res.string.light_theme_text_desc
    ),
    ThemeHolder(
        theme = Theme.DARK_THEME,
        themeNameRes = Res.string.dark_theme_text,
        themeDescriptionRes = Res.string.dark_theme_text_desc
    )
)
