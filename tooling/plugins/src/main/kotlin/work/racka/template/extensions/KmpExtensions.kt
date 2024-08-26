package work.racka.template.extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

/**
 * This sets up Kotlin Multiplatform boilerplate
 * NOTE: This already calls [configureKotlinJvm]
 */
internal fun Project.configureKMP() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        // Checking if needed
        /*if (pluginManager.hasPlugin("com.android.library")) {
            androidTarget()
        }*/

        jvm("desktop")

        // Used for iOS
        /*listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach { target ->
            target.binaries.framework {
                baseName = path.substring(1).replace(':', '-')
            }
        }*/

        sourceSets.all {
            languageSettings {
                listOf(
                    "androidx.paging.ExperimentalPagingApi",
                    "com.arkivanov.decompose.ExperimentalDecomposeApi",
                    "kotlin.RequiresOptIn",
                    "kotlin.experimental.ExperimentalObjCName",
                    "kotlin.time.ExperimentalTime",
                    "kotlinx.cinterop.BetaInteropApi",
                    "kotlinx.cinterop.ExperimentalForeignApi",
                    "kotlinx.coroutines.DelicateCoroutinesApi",
                    "kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "kotlinx.coroutines.FlowPreview",
                    "kotlinx.coroutines.InternalCoroutinesApi",
                    "kotlinx.serialization.ExperimentalSerializationApi",
                    "org.mobilenativefoundation.store.store5.ExperimentalStoreApi",
                ).forEach { optIn(it) }
            }
        }

        // Used for iOS and macOS
        /*targets.withType<KotlinNativeTarget>().configureEach {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.add("-Xallocator=custom")
                        freeCompilerArgs.add("-XXLanguage:+ImplicitSignedToUnsignedIntegerConversion")
                        freeCompilerArgs.add("-Xadd-light-debug=enable")
                        freeCompilerArgs.add("-Xexpect-actual-classes")

                        freeCompilerArgs.addAll(
                            "-opt-in=kotlinx.cinterop.ExperimentalForeignApi",
                            "-opt-in=kotlinx.cinterop.BetaInteropApi",
                        )
                    }
                }
            }
        }*/

        targets.configureEach {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.add("-Xexpect-actual-classes")
                    }
                }
            }
        }
    }
    // Setup Kotlin JVM
    configureKotlinJvm()
}

internal fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) =
    addKspDependencyForAllTargets("", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target ->
                add(
                    "ksp${target.name.replaceFirstChar(Char::uppercaseChar)}$configurationNameSuffix",
                    dependencyNotation,
                )
            }
    }
}