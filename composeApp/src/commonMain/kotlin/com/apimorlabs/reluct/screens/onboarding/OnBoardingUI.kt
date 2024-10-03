package com.apimorlabs.reluct.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.features.onboarding.states.OnBoardingPages
import com.apimorlabs.reluct.features.onboarding.states.OnBoardingState
import com.apimorlabs.reluct.features.onboarding.states.PermissionType
import com.apimorlabs.reluct.screens.onboarding.components.OnBoardingBottomBar
import com.apimorlabs.reluct.screens.onboarding.pages.AlarmsAndRemindersPage
import com.apimorlabs.reluct.screens.onboarding.pages.AllSetPage
import com.apimorlabs.reluct.screens.onboarding.pages.NotificationsPage
import com.apimorlabs.reluct.screens.onboarding.pages.OverlayPage
import com.apimorlabs.reluct.screens.onboarding.pages.PermissionsPage
import com.apimorlabs.reluct.screens.onboarding.pages.ThemesPage
import com.apimorlabs.reluct.screens.onboarding.pages.UsageAccessPage
import com.apimorlabs.reluct.screens.onboarding.pages.WelcomePage
import com.apimorlabs.reluct.util.GetPermissionsManager
import com.apimorlabs.reluct.util.PermissionsManager

@Composable
fun OnBoardingUI(
    uiState: State<OnBoardingState>,
    updateCurrentPage: (OnBoardingPages) -> Unit,
    updatePermission: (permissionType: PermissionType, isGranted: Boolean) -> Unit,
    saveTheme: (themeValue: Int) -> Unit,
    onToggleAppBlocking: (isEnabled: Boolean) -> Unit,
    onBoardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {

    // Permission Manager
    val permManager = remember { mutableStateOf<PermissionsManager?>(null) }
    GetPermissionsManager(onPermissionsManager = { permManager.value = it })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            OnBoardingBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = Dimens.MediumPadding.size),
                uiStateProvider = { uiState.value },
                onUpdatePage = updateCurrentPage,
                onCompleted = onBoardingComplete,
                permManager = permManager
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.value.currentPage,
            modifier = Modifier
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { targetState ->
            when (targetState) {
                is OnBoardingPages.Welcome -> {
                    WelcomePage()
                }

                is OnBoardingPages.Permissions -> {
                    PermissionsPage(goBack = { updateCurrentPage(OnBoardingPages.Welcome) })
                }

                is OnBoardingPages.Notifications -> {
                    NotificationsPage(
                        goBack = { updateCurrentPage(OnBoardingPages.Permissions) },
                        isGranted = uiState.value.permissionsState.notificationGranted,
                        permManager = permManager,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.NOTIFICATION,
                                isGranted
                            )
                        }
                    )
                }

                is OnBoardingPages.Reminders -> {
                    AlarmsAndRemindersPage(
                        goBack = { updateCurrentPage(OnBoardingPages.Notifications) },
                        isGranted = uiState.value.permissionsState.alarmsAndRemindersGranted,
                        permManager = permManager,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.REMINDERS,
                                isGranted
                            )
                        }
                    )
                }

                is OnBoardingPages.UsageAccess -> {
                    UsageAccessPage(
                        goBack = {
                            if (permManager.value?.isNotificationPermissionRequired() == true) {
                                updateCurrentPage(OnBoardingPages.Reminders)
                            } else {
                                updateCurrentPage(OnBoardingPages.Permissions)
                            }
                        },
                        isGranted = uiState.value.permissionsState.usageAccessGranted,
                        permManager = permManager,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.USAGE_ACCESS,
                                isGranted
                            )
                        }
                    )
                }

                is OnBoardingPages.Overlay -> {
                    OverlayPage(
                        goBack = { updateCurrentPage(OnBoardingPages.UsageAccess) },
                        isGranted = uiState.value.permissionsState.overlayGranted,
                        permManager = permManager,
                        isAppBlockingEnabled = uiState.value.appBlockingEnabled,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.OVERLAY,
                                isGranted
                            )
                        },
                        onToggleAppBlocking = onToggleAppBlocking
                    )
                }

                is OnBoardingPages.Themes -> {
                    ThemesPage(
                        selectedTheme = uiState.value.currentThemeValue,
                        onSelectTheme = saveTheme,
                        goBack = { updateCurrentPage(OnBoardingPages.Overlay) }
                    )
                }

                is OnBoardingPages.AllSet -> {
                    AllSetPage(goBack = { updateCurrentPage(OnBoardingPages.Themes) })
                }
            }
        }
    }
}
