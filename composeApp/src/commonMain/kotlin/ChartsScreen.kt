import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.charts.barChart.model.BarParameters
import com.apimorlabs.reluct.compose.charts.barChart.ungrouped.BarChart
import com.apimorlabs.reluct.compose.charts.baseComponets.model.LegendPosition
import com.apimorlabs.reluct.compose.charts.donutChart.DonutChart
import com.apimorlabs.reluct.compose.charts.donutChart.model.ChartLabelType
import com.apimorlabs.reluct.compose.charts.donutChart.model.PieChartData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ChartsScreen(
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isBarChartSelected by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(modifier = Modifier.align(Alignment.Start), onClick = onGoBack) {
                    Text(text = "Go Back")
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Select Type of Chart"
                )
                RadioGroup(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Bar Chart",
                    selected = isBarChartSelected,
                    onClick = { isBarChartSelected = true }
                )
                RadioGroup(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Pie Chart",
                    selected = !isBarChartSelected,
                    onClick = { isBarChartSelected = false }
                )

                if (isBarChartSelected) {
                    BarChartSample()
                } else {
                    DonutChartSample()
                }
            }
        }
    }
}

@Composable
private fun RadioGroup(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled
        )
        Text(text = text, style = style, color = textColor)
    }
}

@Composable
private fun BarChartSample() {
    val testBarParameters: ImmutableList<BarParameters> = persistentListOf(
        BarParameters(
            dataName = "Mon",
            data = 23.5,
            barColor = Color(0xFF6C3428)
        ),
        BarParameters(
            dataName = "Tuesday",
            data = 60.7,
            barColor = Color(0xFFBA704F),
        ),
        BarParameters(
            dataName = "Wednesday",
            data = 58.0,
            barColor = Color(0xFFDFA878),
        ),
        BarParameters(
            dataName = "Thursday",
            data = 12.0,
            barColor = Color(0xFFDFA878),
        ),
        BarParameters(
            dataName = "Friday",
            data = 72.0,
            barColor = Color(0xFFDFA878),
        ),
        BarParameters(
            dataName = "Saturday",
            data = 90.0,
            barColor = Color(0xFFDFA878),
        ),
    )
    val xAxisData by remember {
        derivedStateOf {
            testBarParameters.map { it.dataName }.toImmutableList()
        }
    }

    val selectedBarColor = Color.Blue
    val selectedBarIndex = remember { mutableStateOf<Int?>(null) }
    val labelColor = MaterialTheme.colorScheme.onSurface

    BarChart(
        chartParameters = testBarParameters,
        gridColor = Color.DarkGray,
        xAxisData = xAxisData,
        animateChart = true,
        showYAxis = false,
        showYAxisLabels = false,
        yAxisStyle = MaterialTheme.typography.labelMedium.copy(labelColor),
        xAxisStyle = MaterialTheme.typography.labelMedium.copy(color = labelColor),
        selectedBarColor = selectedBarColor,
        selectedBarIndex = selectedBarIndex.value,
        onBarClicked = { index ->
            selectedBarIndex.value = index
        }
    )
}

@Composable
private fun DonutChartSample() {
    val testPieChartData: ImmutableList<PieChartData> = persistentListOf(
        PieChartData(
            partName = "WhatsApp",
            data = 500.0,
            color = Color(0xFF0B666A),
        ),
        PieChartData(
            partName = "Office 365",
            data = 700.0,
            color = Color(0xFF35A29F),
        ),
        PieChartData(
            partName = "Twitter",
            data = 500.0,
            color = Color(0xFF97FEED),
        ),
        PieChartData(
            partName = "Reddit",
            data = 100.0,
            color = Color(0xFF071952),
        ),
        PieChartData(
            partName = "Chrome",
            data = 300.0,
            color = Color(0xFF071952),
        ),
        PieChartData(
            partName = "Camera",
            data = 400.0,
            color = Color(0xFF071952),
        ),
        PieChartData(
            partName = "Instagram",
            data = 200.0,
            color = Color(0xFF071952),
        ),
    )

    DonutChart(
        modifier = Modifier.fillMaxSize(),
        pieChartData = testPieChartData,
        centerTitle = "9hrs 23min",
        centerTitleStyle = MaterialTheme.typography.bodySmall
            .copy(color = MaterialTheme.colorScheme.onSurface),
        textRatioStyle = MaterialTheme.typography.bodySmall
            .copy(color = MaterialTheme.colorScheme.onSurface),
        outerCircularColor = Color.LightGray,
        innerCircularColor = Color.Gray,
        ratioLineColor = Color.LightGray,
        legendPosition = LegendPosition.DISAPPEAR,
        chartLabelType = ChartLabelType.NONE,
        animation = TweenSpec(500)
    )
}
