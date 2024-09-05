plugins {
    alias(libs.plugins.compose.multiplatform.lib)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //Projects
                implementation(project(":common-models"))

                // Normal deps
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(libs.coroutines.core)
                implementation(libs.kmpalette.core)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.compose.ui"
}
