package work.racka.template.plugins

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationDistributions
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import work.racka.template.extensions.Versions
import work.racka.template.extensions.android
import work.racka.template.extensions.configureAndroid
import work.racka.template.extensions.configureAndroidCompose
import work.racka.template.extensions.configureKMP
import work.racka.template.extensions.libs
import java.io.File
import kotlin.jvm.optionals.getOrNull

/**
 * This a plugin setting up the application level of compose multiplatform.
 * Will likely be used once per main app module.
 */
class ComposeMultiplatformAppPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            val jbCompose = libs.findPlugin("jetbrainsCompose").getOrNull()?.orNull
            val composeCompiler = libs.findPlugin("compose-compiler").getOrNull()?.orNull
            val androidApp = libs.findPlugin("androidApplication").getOrNull()?.orNull
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
                implementation(composeExt.dependencies.preview)
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

        // Compose Desktop Setup
        with(composeExt) {
            with(extensions.getByType<DesktopExtension>()) {
                application {
                    mainClass = "MainKt"

                    // Put your icons in ./tooling/desktop/icons/launcher
                    val iconsRoot = rootProject.file("tooling/desktop/icons/launcher")
                    nativeDistributions {
                        packageName = Versions.DESKTOP_PACKAGE_NAME
                        packageVersion = Versions.DESKTOP_VERSION
                        description = "Template App"
                        copyright = "© 2024 APIMOR LABS. All rights reserved."
                        vendor = "APIMOR LABS"
                        version = Versions.DESKTOP_VERSION
                        licenseFile.set(rootProject.file("LICENSE"))

                        setupDistribution(iconsRoot)

                        // ProGuard
                        buildTypes.release.proguard {
                            configurationFiles.from(rootProject.file("tooling/proguard-config/desktop.pro"))
                            obfuscate.set(true)
                            isEnabled.set(true)
                        }
                    }
                }
            }
        }

        // Android Setup
        // Configure Android Resources
        android {
            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
            sourceSets["main"].res.srcDirs("src/androidMain/res")
            sourceSets["main"].resources.srcDirs("src/commonMain/resources")

            defaultConfig {
                applicationId = Versions.PACKAGE_NAME
            }
        }

        // Configure Android App level
        extensions.configure<ApplicationExtension> {

            defaultConfig {
                targetSdk = Versions.TARGET_SDK
            }

            namespace = Versions.PACKAGE_NAME

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        rootProject.file("tooling/proguard-config/proguard-rules.pro").absolutePath
                    )
                }
            }

            configureAndroid()
            configureAndroidCompose(this)
        }

    }
}

private fun JvmApplicationDistributions.setupDistribution(iconsRoot: File) {
    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
    modules("java.net.http", "java.sql")

    linux {
        iconFile.set(iconsRoot.resolve("linux.png"))
        debMaintainer = "apimorlabs@gmail.com"
        menuGroup = packageName
        appCategory = "Productivity"
    }

    windows {
        iconFile.set(iconsRoot.resolve("windows.ico"))
        shortcut = true
        menuGroup = packageName
        //https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
        // You must change this
        upgradeUuid = "0FC0D185-EFDF-4F64-86EF-CA8A855F49A7"
    }

    macOS {
        iconFile.set(iconsRoot.resolve("macos.icns"))
        bundleID = packageName
        appCategory = "public.app-category.productivity"
    }
}
