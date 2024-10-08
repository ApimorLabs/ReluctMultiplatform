package com.apimorlabs.reluct

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apimorlabs.reluct.common.sources.MyViewModel
import com.apimorlabs.reluct.compose.ui.theme.ReluctAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun App() {
    ReluctAppTheme {
        KoinContext {
            val controller = rememberNavController()
            NavHost(
                navController = controller,
                startDestination = "home"
            ) {
                composable(
                    route = "home",
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    val viewModel = koinViewModel<MyViewModel>()
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = viewModel.getHelloWorldString()
                            )
                            Spacer(Modifier.padding(16.dp))
                            Button(onClick = { controller.navigate("details") }) {
                                Text(text = "Details")
                            }
                            Spacer(Modifier.padding(16.dp))
                            Button(onClick = { controller.navigate("charts") }) {
                                Text(text = "Charts")
                            }
                        }
                    }
                }

                composable(
                    route = "details",
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    DetailsScreen(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        onGoBack = { controller.popBackStack() }
                    )
                }

                composable(
                    route = "charts",
                    enterTransition = { scaleInEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    popExitTransition = { scaleOutPopExitTransition() }
                ) {
                    ChartsScreen(
                        onGoBack = { controller.popBackStack() }
                    )
                }
            }
        }
    }
}
