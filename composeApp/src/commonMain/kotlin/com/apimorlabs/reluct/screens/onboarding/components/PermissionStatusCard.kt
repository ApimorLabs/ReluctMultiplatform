package com.apimorlabs.reluct.screens.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.click_to_grant_text
import com.apimorlabs.reluct.compose.ui.permission_granted_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PermissionStatusCard(
    isGranted: Boolean,
    modifier: Modifier = Modifier,
    onPermissionRequest: () -> Unit = {},
) {
    val containerColor by animateColorAsState(
        targetValue = if (isGranted) Color.Green.copy(alpha = .7f) else Color.Red.copy(alpha = .7f)
    )
    val contentColor by animateColorAsState(
        targetValue = if (isGranted) Color.Black.copy(alpha = .8f) else Color.White
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onPermissionRequest,
        shape = Shapes.large,
        modifier = Modifier
            .fillMaxWidth() then modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = if (isGranted) Icons.Rounded.CheckCircle else Icons.Rounded.Error,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))

            Text(
                text = if (isGranted) {
                    stringResource(Res.string.permission_granted_text)
                } else {
                    stringResource(Res.string.click_to_grant_text)
                },
                style = MaterialTheme.typography.titleLarge,
                color = contentColor
            )
        }
    }
}
