buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.detektGradle) apply false
    alias(libs.plugins.detekt.setup) apply false
}

allprojects {


    afterEvaluate {
        apply(plugin = libs.plugins.detekt.setup.get().pluginId)

        // Remove log pollution until Android support in KMP improves.
        project.extensions
            .findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()
            ?.let { kmpExt ->
                kmpExt.sourceSets.removeAll {
                    setOf(
                        "androidAndroidTestRelease",
                        "androidTestFixtures",
                        "androidTestFixturesDebug",
                        "androidTestFixturesRelease",
                        "androidTestFixturesDemo"
                    )
                        .contains(it.name)
                }
            }
    }
}