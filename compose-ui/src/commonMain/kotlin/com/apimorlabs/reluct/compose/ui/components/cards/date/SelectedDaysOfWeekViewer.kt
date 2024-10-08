package com.apimorlabs.reluct.compose.ui.components.cards.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditGoal.ReluctSelectionButton
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun SelectedDaysOfWeekViewer(
    selectedDays: ImmutableList<Week>,
    onUpdateDaysOfWeek: (ImmutableList<Week>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        Week.entries.forEach { day ->
            ReluctSelectionButton(
                modifier = Modifier.weight(1f),
                isSelected = selectedDays.contains(day),
                content = {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                horizontal = Dimens.SmallPadding.size,
                                vertical = Dimens.MediumPadding.size
                            ),
                        text = day.dayAcronym.first().toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                onClick = {
                    val new = selectedDays.toPersistentList().builder().apply {
                        if (selectedDays.contains(day)) remove(day) else add(day)
                    }.build().toImmutableList()
                    onUpdateDaysOfWeek(new)
                }
            )
        }
    }
}
