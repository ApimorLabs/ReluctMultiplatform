package com.apimorlabs.reluct.compose.ui.components.cards.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.allow_permission
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun PermissionsCard(
    imageSlot: @Composable () -> Unit,
    permissionDetails: String,
    onPermissionRequest: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .padding(horizontal = Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
        ) {
            imageSlot()

            Text(
                text = permissionDetails,
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current,
                textAlign = TextAlign.Center
            )

            ReluctButton(
                buttonText = stringResource(Res.string.allow_permission),
                icon = Icons.Rounded.AutoAwesome,
                onButtonClicked = onPermissionRequest
            )
        }
    }
}