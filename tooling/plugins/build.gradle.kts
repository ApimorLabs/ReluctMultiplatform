plugins {
    `kotlin-dsl`
}

group = "com.apimorlabs.reluct.plugins"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    // Dependencies setup here to enable detection of corresponding source files for plugins
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.jetbrains.compose.gradlePlugin)
    compileOnly(libs.detekt.gradle.plugin)
}

gradlePlugin {
    // IDs for these custom plugins are obtained from libs.versions.toml for ease of use in project
    plugins {
        register("composeMultiplatformAppPlugin") {
            id = libs.plugins.compose.multiplatform.app.get().pluginId
            implementationClass = "com.apimorlabs.reluct.plugins.ComposeMultiplatformAppPlugin"
        }
        register("composeMultiplatformLibPlugin") {
            id = libs.plugins.compose.multiplatform.lib.get().pluginId
            implementationClass = "com.apimorlabs.reluct.plugins.ComposeMultiplatformLibPlugin"
        }
        register("kotlinMultiplatformLibPlugin") {
            id = libs.plugins.kotlin.multiplatform.lib.get().pluginId
            implementationClass = "com.apimorlabs.reluct.plugins.KotlinMultiplatformLibPlugin"
        }
        register("detektPlugin") {
            id = libs.plugins.detekt.setup.get().pluginId
            implementationClass = "com.apimorlabs.reluct.plugins.DetektConventionPlugin"
        }
    }
}