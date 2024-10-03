package com.apimorlabs.reluct.screens.onboarding.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.back_text
import com.apimorlabs.reluct.compose.ui.components.buttons.OutlinedReluctButton
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.textFields.text.HighlightTextProps
import com.apimorlabs.reluct.compose.ui.components.textFields.text.HyperlinkText
import com.apimorlabs.reluct.compose.ui.continue_text
import com.apimorlabs.reluct.compose.ui.next_text
import com.apimorlabs.reluct.compose.ui.privacy_policy_hyperlink_text
import com.apimorlabs.reluct.compose.ui.privacy_policy_hyperlink_url
import com.apimorlabs.reluct.compose.ui.privacy_policy_terms_text
import com.apimorlabs.reluct.compose.ui.terms_of_service_hyperlink_text
import com.apimorlabs.reluct.compose.ui.terms_of_service_hyperlink_url
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.features.onboarding.states.OnBoardingPages
import com.apimorlabs.reluct.features.onboarding.states.OnBoardingState
import com.apimorlabs.reluct.util.PermissionsManager
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnBoardingBottomBar(
    permManager: State<PermissionsManager?>,
    uiStateProvider: () -> OnBoardingState,
    onUpdatePage: (OnBoardingPages) -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by remember { derivedStateOf { uiStateProvider() } }
    val nextText = stringResource(Res.string.next_text)
    val backText = stringResource(Res.string.back_text)
    val continueText = stringResource(Res.string.continue_text)

    val bottomButtons by remember(
        uiState.currentPage,
        uiState.permissionsState,
        uiState.appBlockingEnabled
    ) {
        derivedStateOf {
            when (uiState.currentPage) {
                is OnBoardingPages.Welcome -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = true,
                        positiveAction = { onUpdatePage(OnBoardingPages.Permissions) },
                        negativeText = "",
                        isNegativeEnabled = false,
                        negativeAction = {}
                    )
                }

                is OnBoardingPages.Permissions -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = true,
                        positiveAction = {
                            if (permManager.value?.isNotificationPermissionRequired() == true) {
                                onUpdatePage(OnBoardingPages.Notifications)
                            } else {
                                onUpdatePage(OnBoardingPages.UsageAccess)
                            }
                        },
                        negativeText = backText,
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Welcome) }
                    )
                }

                is OnBoardingPages.Notifications -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = uiState.permissionsState.notificationGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.Reminders) },
                        negativeText = backText,
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Permissions) }
                    )
                }

                is OnBoardingPages.Reminders -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = uiState.permissionsState.alarmsAndRemindersGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.UsageAccess) },
                        negativeText = backText,
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Notifications) }
                    )
                }

                is OnBoardingPages.UsageAccess -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = uiState.permissionsState.usageAccessGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.Overlay) },
                        negativeText = backText,
                        isNegativeEnabled = true,
                        negativeAction = {
                            if (permManager.value?.isNotificationPermissionRequired() == true) {
                                onUpdatePage(OnBoardingPages.Reminders)
                            } else {
                                onUpdatePage(OnBoardingPages.Permissions)
                            }
                        }
                    )
                }

                is OnBoardingPages.Overlay -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = uiState.permissionsState.overlayGranted ||
                                !uiState.appBlockingEnabled,
                        positiveAction = { onUpdatePage(OnBoardingPages.Themes) },
                        negativeText = backText,
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.UsageAccess) }
                    )
                }

                is OnBoardingPages.Themes -> {
                    BottomButtonsProperties(
                        positiveText = nextText,
                        isPositiveEnabled = true,
                        positiveAction = { onUpdatePage(OnBoardingPages.AllSet) },
                        negativeText = backText,
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Overlay) }
                    )
                }

                is OnBoardingPages.AllSet -> {
                    BottomButtonsProperties(
                        positiveText = continueText,
                        isPositiveEnabled = true,
                        positiveAction = { onCompleted() },
                        negativeText = "",
                        isNegativeEnabled = false,
                        negativeAction = { }
                    )
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(vertical = Dimens.SmallPadding.size)
            .fillMaxWidth() then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Privacy Policy and Terms of Service
        if (uiState.currentPage is OnBoardingPages.Welcome) {
            HyperlinkText(
                fullText = stringResource(Res.string.privacy_policy_terms_text),
                textAlign = TextAlign.Center,
                hyperLinks = persistentListOf(
                    HighlightTextProps(
                        text = stringResource(Res.string.privacy_policy_hyperlink_text),
                        url = stringResource(Res.string.privacy_policy_hyperlink_url),
                        color = MaterialTheme.colorScheme.primary
                    ),
                    HighlightTextProps(
                        text = stringResource(Res.string.terms_of_service_hyperlink_text),
                        url = stringResource(Res.string.terms_of_service_hyperlink_url),
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            )
        }

        Row(
            modifier = (if (bottomButtons.isNegativeEnabled) Modifier.fillMaxWidth() else Modifier)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (bottomButtons.isNegativeEnabled) {
                OutlinedReluctButton(
                    buttonText = bottomButtons.negativeText,
                    icon = null,
                    onButtonClicked = bottomButtons.negativeAction
                )
            }

            ReluctButton(
                buttonText = bottomButtons.positiveText,
                icon = null,
                enabled = bottomButtons.isPositiveEnabled,
                onButtonClicked = bottomButtons.positiveAction
            )
        }
    }
}

@Stable
private data class BottomButtonsProperties(
    val positiveText: String,
    val isPositiveEnabled: Boolean,
    val positiveAction: () -> Unit,
    val negativeText: String,
    val isNegativeEnabled: Boolean,
    val negativeAction: () -> Unit
)
