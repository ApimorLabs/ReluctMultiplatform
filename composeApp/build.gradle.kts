plugins {
    alias(libs.plugins.compose.multiplatform.app)
    alias(libs.plugins.kotlinSeriazation)
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(project(":common-models"))
                implementation(project(":compose-ui"))
                implementation(project(":features"))
                implementation(compose.materialIconsExtended)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.chrisbanes.haze)
                implementation(libs.chrisbanes.haze.materials)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.coroutines.swing)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.splash.screen.core)
                implementation(libs.androidx.workmanager)
                implementation(libs.koin.androidx.workmanager)
            }
        }
    }
}
android {
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
