plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSeriazation)
}

sqldelight {
    databases {
        create("ReluctDatabase") {
            packageName.set("com.apimorlabs.reluct.data.source.database")
            srcDirs.setFrom("src/commonMain/sqldelight")
            schemaOutputDirectory.set(
                file("src/commonMain/sqldelight/com/apimorlabs/reluct/data/source/database/files")
            )
            version = 1
            verifyMigrations.set(true)
            dialect(libs.sqldelight.sqlite.dialect)
        }
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data-model"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.coroutines.core)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.multiplatform.settings.core)
                implementation(libs.multiplatform.settings.noArg)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine.test)
                implementation(libs.multiplatform.settings.test)
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.android.driver)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
    }
}

android {
    namespace = "com.apimorlabs.reluct.data.source"
}
