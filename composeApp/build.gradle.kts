plugins {
    alias(libs.plugins.compose.multiplatform.app)
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies { implementation(project(":common")) }
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
