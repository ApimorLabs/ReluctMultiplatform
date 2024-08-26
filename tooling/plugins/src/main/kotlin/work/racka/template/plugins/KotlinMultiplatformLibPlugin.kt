package work.racka.template.plugins

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import work.racka.template.extensions.Versions
import work.racka.template.extensions.android
import work.racka.template.extensions.configureAndroid
import work.racka.template.extensions.configureKMP
import work.racka.template.extensions.libs
import kotlin.jvm.optionals.getOrNull

/**
 * This a plugin setting up the library level of kotlin multiplatform.
 * Will likely be used once per main app module.
 */
class KotlinMultiplatformLibPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            val androidApp = libs.findPlugin("androidLibrary").getOrNull()?.orNull
            val kmp = libs.findPlugin("kotlinMultiplatform").getOrNull()?.orNull
            androidApp?.let { plugin -> apply(plugin.pluginId) }
            kmp?.let { plugin -> apply(plugin.pluginId) }
        }

        // Setup all KMP boilerplate
        configureKMP()

        // Compose Dependencies
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.androidMain.dependencies {
                implementation(libs.findLibrary("androidx-activity-compose").get())
                implementation(libs.findLibrary("androidx-appcompat").get())
                implementation(libs.findLibrary("androidx-core-ktx").get())

                implementation(libs.findLibrary("koin-android").get())
            }

            sourceSets.commonMain.dependencies {
                api(libs.findLibrary("koin-core").get())
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
        }
    }
}

