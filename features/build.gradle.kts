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

                // Normal deps
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.lifecycle.viewmodel)
                implementation(libs.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine.test)
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.features"
}
