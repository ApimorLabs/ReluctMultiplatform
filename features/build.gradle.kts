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
    }
}

android {
    namespace = "com.apimorlabs.reluct.features"
}
