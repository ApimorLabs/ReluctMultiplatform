package com.apimorlabs.reluct.compose.charts.barChart.ungrouped.helpers

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters

internal class BarDrawer(
    private val cornerRadius: Dp,
) {
    private val barPaint = Paint().apply {
        this.isAntiAlias = true
    }

    fun drawBar(
        drawScope: DrawScope,
        barArea: Rect,
        bar: BarParameters,
        selected: Boolean,
        selectedBarColor: Color
    ) {
        val path = Path()
        path.addRoundRect(
            RoundRect(
                barArea,
                topLeft = CornerRadius(cornerRadius.value),
                topRight = CornerRadius(cornerRadius.value),
                bottomLeft = CornerRadius.Zero,
                bottomRight = CornerRadius.Zero
            )
        )

        drawScope.drawContext.canvas.drawPath(
            path,
            barPaint.apply {
                color = if (selected) selectedBarColor else bar.barColor
            }
        )
    }
}
