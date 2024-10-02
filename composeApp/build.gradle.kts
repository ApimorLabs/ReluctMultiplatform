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
