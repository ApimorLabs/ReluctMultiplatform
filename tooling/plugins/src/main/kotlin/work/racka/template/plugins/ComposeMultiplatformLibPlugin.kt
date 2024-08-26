package work.racka.template.plugins

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import work.racka.template.extensions.Versions
import work.racka.template.extensions.android
import work.racka.template.extensions.configureAndroid
import work.racka.template.extensions.configureAndroidCompose
import work.racka.template.extensions.configureKMP
import work.racka.template.extensions.libs
import kotlin.jvm.optionals.getOrNull

/**
 * This a plugin setting up the library level of compose multiplatform.
 * To be applied in all library modules that would also contain compose code
 */
class ComposeMultiplatformLibPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            val jbCompose = libs.findPlugin("jetbrainsCompose").getOrNull()?.orNull
            val composeCompiler = libs.findPlugin("compose-compiler").getOrNull()?.orNull
            val androidApp = libs.findPlugin("androidLibrary").getOrNull()?.orNull
            val kmp = libs.findPlugin("kotlinMultiplatform").getOrNull()?.orNull
            jbCompose?.let { plugin -> apply(plugin.pluginId) }
            composeCompiler?.let { plugin -> apply(plugin.pluginId) }
            androidApp?.let { plugin -> apply(plugin.pluginId) }
            kmp?.let { plugin -> apply(plugin.pluginId) }
        }

        // Setup all KMP boilerplate
        val composeExt = extensions.getByType<ComposeExtension>()
        configureKMP()

        // Compose Dependencies
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.androidMain.dependencies {
                implementation(libs.findLibrary("androidx-activity-compose").get())
                implementation(libs.findLibrary("androidx-appcompat").get())
                implementation(libs.findLibrary("androidx-core-ktx").get())

                implementation(libs.findLibrary("koin-android").get())
                implementation(libs.findLibrary("koin-androidx-compose").get())
            }

            sourceSets.commonMain.dependencies {
                implementation(composeExt.dependencies.runtime)
                implementation(composeExt.dependencies.foundation)
                implementation(composeExt.dependencies.material)
                implementation(composeExt.dependencies.ui)
                implementation(composeExt.dependencies.components.resources)
                implementation(composeExt.dependencies.components.uiToolingPreview)

                api(libs.findLibrary("koin-core").get())
                implementation(libs.findLibrary("koin-compose").get())
                implementation(libs.findLibrary("koin-compose-viewmodel").get())

                implementation(libs.findLibrary("lifecycle-viewmodel").get())
                implementation(libs.findLibrary("navigation-compose").get())
            }

            sourceSets["desktopMain"].dependencies {
                implementation(composeExt.dependencies.desktop.currentOs)
            }
        }

        // Android Setup
        // Configure Android Resources
        android {
            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
            sourceSets["main"].res.srcDirs("src/androidMain/res")
            sourceSets["main"].resources.srcDirs("src/commonMain/resources")
        }

        // Configure Android Library
        extensions.configure<LibraryExtension> {
            defaultConfig {
                testOptions.targetSdk = Versions.TARGET_SDK
            }


            configureAndroid()
            configureAndroidCompose(this)
        }

    }
}

