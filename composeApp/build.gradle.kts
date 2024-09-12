plugins {
    alias(libs.plugins.compose.multiplatform.app)
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(project(":compose-ui"))
                implementation(compose.materialIconsExtended)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.coroutines.swing)
            }
        }
    }
}
android {
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
