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
                api(libs.moko.resources.core)
                api(libs.moko.resources.compose)
                api(libs.kmpalette.core)
                api(libs.kmpalette.extensions.bytearray)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(libs.coroutines.core)
                implementation(libs.sketch.compose)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.compose.ui"
    sourceSets["main"].apply {
        assets.srcDir(layout.buildDirectory.file("generated/moko/androidMain/assets"))
        res.srcDir(layout.buildDirectory.file("generated/moko/androidMain/res"))
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.apimorlabs.reluct.compose.ui"
}

