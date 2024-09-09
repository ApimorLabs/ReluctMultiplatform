import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.FormatPaint
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.SyncLock
import androidx.compose.material.icons.sharp.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsScreen(
    listOfPeople: List<String>,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToMeeting: () -> Unit = {},
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Button(onClick = onGoBack) {
                Text(text = "Go Back")
            }
        }

        stickyHeader {
            Text(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                text = "Meeting Info"
            )
        }
        item {
            DetailsCardWithIcon(
                icon = Icons.Sharp.Timer,
                text = "Start Meeting",
                iconDescription = "Meeting Label",
                onClick = onNavigateToMeeting
            )
        }
        item { DetailsCardWithIcon(icon = Icons.Sharp.SyncLock, text = "15 minutes") }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(imageVector = Icons.Sharp.FormatPaint, contentDescription = "Theme")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Theme")
                    }
                    Box(
                        modifier = Modifier.background(
                            color = Color.Yellow,
                            shape = RoundedCornerShape(4.dp)
                        )
                    ) {
                        Text(modifier = Modifier.padding(4.dp), text = "Yellow")
                    }
                }
            }
        }

        stickyHeader {
            Text(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                text = "Attendees"
            )
        }
        items(listOfPeople) { item ->
            DetailsCardWithIcon(icon = Icons.Sharp.Person, text = item)
        }
    }
}

@Composable
fun DetailsCardWithIcon(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconDescription: String? = null,
    onClick: () -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = iconDescription)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}