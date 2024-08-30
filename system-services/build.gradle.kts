plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //Projects
                implementation(project(":data-model"))

                // Normal deps

            }
        }
    }
}

android {
    namespace = "work.racka.reluct.system.services"
}
