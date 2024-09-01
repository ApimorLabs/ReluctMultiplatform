plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //Projects
                implementation(project(":common-models"))
                implementation(project(":data-source"))
                implementation(project(":domain"))
                implementation(project(":features"))
                implementation(project(":system-services"))

                // Normal deps
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.lifecycle.viewmodel)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.common"
}
