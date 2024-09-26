package com.apimorlabs.reluct.compose.ui.components.numberPicker

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.collections.immutable.toImmutableList

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: Iterable<Int>,
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString()
    },
    dividersColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    ListItemPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        list = range.toImmutableList(),
        textStyle = textStyle
    )
}
