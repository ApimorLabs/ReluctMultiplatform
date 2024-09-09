package com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes

@Composable
fun ReluctDescriptionCard(
    title: @Composable () -> Unit,
    description: @Composable ColumnScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    leftItems: @Composable RowScope.() -> Unit = {},
    rightItems: @Composable RowScope.() -> Unit = {},
    bottomContent: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
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
            leftItems()

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

            rightItems()
        }
        bottomContent()
    }
}