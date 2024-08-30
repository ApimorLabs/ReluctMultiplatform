plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
    alias(libs.plugins.kotlinSeriazation)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                api(libs.kotlinx.date.time)
                api(libs.kotlinx.collections.immutable)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.data.model"
}
