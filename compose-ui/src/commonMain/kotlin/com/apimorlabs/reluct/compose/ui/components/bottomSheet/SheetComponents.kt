package com.apimorlabs.reluct.compose.ui.components.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.close_icon
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopSheetSection(
    sheetTitle: String,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    containerColor: Color = Color.Transparent,
    rightButtonIcon: ImageVector? = null,
    rightButtonContentDescription: String? = null,
    onRightButtonClicked: () -> Unit = { },
    closeButtonVisible: Boolean = true,
    rightButtonEnabled: Boolean = true,
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            Box(
                modifier = Modifier
                    .size(height = 5.dp, width = 32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground
                            .copy(alpha = .1f),
                        shape = CircleShape
                    )
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onCloseClicked,
                    enabled = closeButtonVisible
                ) {
                    if (closeButtonVisible) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(Res.string.close_icon),
                            tint = contentColor
                        )
                    }
                }

                Text(
                    text = sheetTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = onRightButtonClicked,
                    enabled = rightButtonEnabled
                ) {
                    if (rightButtonIcon != null) {
                        Icon(
                            imageVector = rightButtonIcon,
                            contentDescription = rightButtonContentDescription,
                            tint = contentColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
        }
    }
}

/*
@Preview
@Composable
private fun TopSheetPreview() {
    ReluctAppTheme {
        TopSheetSection(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            sheetTitle = "Sheet Title",
            onCloseClicked = { }
        )
    }
}*/
