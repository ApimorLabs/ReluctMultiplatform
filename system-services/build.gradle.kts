plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
    alias(libs.plugins.kotlinSeriazation)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //Projects
                implementation(project(":data-model"))

                // Normal deps
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.coroutines.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.system.services"
}
