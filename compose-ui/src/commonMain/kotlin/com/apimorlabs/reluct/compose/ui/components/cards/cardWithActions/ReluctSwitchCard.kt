package com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes

@Composable
fun ReluctSwitchCard(
    title: @Composable () -> Unit,
    description: @Composable ColumnScope.() -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    bottomContent: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
        enabled = enabled,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth() then modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            icon?.let {
                Icon(imageVector = icon, contentDescription = null)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.Start
            ) {
                title()
                description()
            }

            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) },
                enabled = enabled
            )
        }
        bottomContent()
    }
}