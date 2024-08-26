plugins {
    alias(libs.plugins.compose.multiplatform.app)
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies { implementation(project(":common")) }
        }
    }
}
android {
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
