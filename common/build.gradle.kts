plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.lifecycle.viewmodel)
            }
        }
    }
}

android {
    namespace = "work.racka.template.common"
}
