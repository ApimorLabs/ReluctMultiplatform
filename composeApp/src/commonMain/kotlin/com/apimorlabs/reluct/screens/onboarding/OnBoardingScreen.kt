package com.apimorlabs.reluct.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apimorlabs.reluct.features.onboarding.OnBoardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnBoardingScreen() {
    val viewModel: OnBoardingViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    OnBoardingUI(
        uiState = uiState,
        updateCurrentPage = viewModel::updateCurrentPage,
        updatePermission = viewModel::updatePermission,
        saveTheme = viewModel::saveTheme,
        onToggleAppBlocking = viewModel::toggleAppBlocking,
        onBoardingComplete = {
            viewModel.onBoardingComplete()
            // Navigate to auth: Implement onNavigateToAuth()
        }
    )
}
