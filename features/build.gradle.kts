plugins {
    alias(libs.plugins.compose.multiplatform.lib)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //Projects
                implementation(project(":common-models"))
                implementation(project(":data-source"))
                implementation(project(":domain"))
                implementation(project(":system-services"))

                // Normal deps
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

        val androidMain by getting {
            dependencies {
                implementation(libs.savedstate)
                implementation(libs.koin.androidx.workmanager)
                implementation(libs.androidx.workmanager)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.features"
}
