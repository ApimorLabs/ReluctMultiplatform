package com.apimorlabs.reluct

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.apimorlabs.reluct.compose.ui.theme.Theme
import com.apimorlabs.reluct.features.settings.GetSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get Settings
        val settings = get<GetSettings>()
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()

        // Enable edge-to-edge experience and ProvideWindowInsets to the composable
        enableEdgeToEdgeForTheme(themeValue = settings.getTheme())

        // Fix MIUI issue
        triggerBackgroundChangeIfNeeded()

        setContent {
            App(settings = settings)
        }
    }

    /**
     * Function to enable edge to edge mode for Android
     */
    private fun ComponentActivity.enableEdgeToEdgeForTheme(themeValue: Int) {
        val style = when (themeValue) {
            Theme.LIGHT_THEME.themeValue -> SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )

            Theme.DARK_THEME.themeValue -> SystemBarStyle.dark(Color.TRANSPARENT)
            else -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        }
        enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
        // Fix for three-button nav not properly going edge-to-edge.
        // Issue: https://issuetracker.google.com/issues/298296168
        window.setFlags(LayoutParams.FLAG_LAYOUT_NO_LIMITS, LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /**
     * Function to tackle https://issuetracker.google.com/issues/227926002.
     * This will trigger the compose NavHost to load the startDestination without having a user input.
     */
    private fun triggerBackgroundChangeIfNeeded() {
        if (isMiui()) {
            lifecycleScope.launch {
                delay(400)
                window.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
    }

    /**
     * Function to determine if the device runs on MIUI by Xiaomi.
     */
    @SuppressLint("PrivateApi")
    private fun isMiui(): Boolean {
        return try {
            Class
                .forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .let { propertyClass ->
                    propertyClass
                        .invoke(propertyClass, "ro.miui.ui.version.name")
                        ?.toString()
                        ?.isNotEmpty()
                        ?: false
                }
        } catch (e: Exception) {
            // e.printStackTrace()
            println("Property not found: ${e.message}")
            false
        }
    }
}
