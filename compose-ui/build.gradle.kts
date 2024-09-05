plugins {
    alias(libs.plugins.compose.multiplatform.lib)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //Projects
                implementation(project(":domain"))

                // Normal deps
                implementation(compose.materialIconsExtended)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.compose.ui"
}
