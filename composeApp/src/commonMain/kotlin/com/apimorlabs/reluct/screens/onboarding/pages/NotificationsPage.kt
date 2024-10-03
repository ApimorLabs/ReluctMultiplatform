package com.apimorlabs.reluct.screens.onboarding.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.cards.headers.ListGroupHeadingHeader
import com.apimorlabs.reluct.compose.ui.notifications_desc_text
import com.apimorlabs.reluct.compose.ui.notifications_text
import com.apimorlabs.reluct.compose.ui.push_notifications
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.screens.onboarding.components.PermissionStatusCard
import com.apimorlabs.reluct.util.BackPressHandler
import com.apimorlabs.reluct.util.PermissionCheckHandler
import com.apimorlabs.reluct.util.PermissionsManager
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationsPage(
    isGranted: Boolean,
    permManager: State<PermissionsManager?>,
    updatePermissionCheck: (isGranted: Boolean) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackPressHandler { goBack() } // Handle Back Presses

    val drawableSize = 300.dp
    var permissionTries by remember { mutableStateOf(1) }

    PermissionCheckHandler {
        if (!isGranted) {
            updatePermissionCheck(
                permManager.value?.checkNotificationPermission() ?: false
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(Res.string.notifications_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(Res.drawable.push_notifications),
                contentDescription = null
            )
        }

        item {
            Text(
                text = stringResource(Res.string.notifications_desc_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            PermissionStatusCard(
                modifier = Modifier.padding(vertical = Dimens.MediumPadding.size),
                isGranted = isGranted
            ) {
                if (!isGranted) {
                    permManager.value?.apply {
                        if (permissionTries <= 2) {
                            requestNotificationPermission()
                        } else {
                            openAppNotificationSettings()
                        }
                        permissionTries++
                    }
                }
            }
        }
    }
}
