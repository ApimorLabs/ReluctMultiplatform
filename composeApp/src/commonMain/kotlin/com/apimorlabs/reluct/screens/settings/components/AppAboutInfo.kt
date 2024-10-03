package com.apimorlabs.reluct.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_version
import com.apimorlabs.reluct.compose.ui.components.textFields.text.HighlightTextProps
import com.apimorlabs.reluct.compose.ui.components.textFields.text.HyperlinkText
import com.apimorlabs.reluct.compose.ui.copyright_year
import com.apimorlabs.reluct.compose.ui.developer_name
import com.apimorlabs.reluct.compose.ui.made_with_text
import com.apimorlabs.reluct.compose.ui.privacy_policy_n_terms_hyperlink_text
import com.apimorlabs.reluct.compose.ui.reluct_polices_hyperlink_url
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppAboutInfo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.developer_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(Res.string.copyright_year),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(Res.string.app_version, "1.0.0-test"),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = stringResource(Res.string.made_with_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        // Terms of Service and Privacy Policy
        HyperlinkText(
            fullText = stringResource(Res.string.privacy_policy_n_terms_hyperlink_text),
            textAlign = TextAlign.Center,
            hyperLinks = persistentListOf(
                HighlightTextProps(
                    text = stringResource(Res.string.privacy_policy_n_terms_hyperlink_text),
                    url = stringResource(Res.string.reluct_polices_hyperlink_url),
                    color = MaterialTheme.colorScheme.primary
                )
            )
        )
    }
}
