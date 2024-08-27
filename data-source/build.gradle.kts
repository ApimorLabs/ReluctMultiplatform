plugins {
    alias(libs.plugins.kotlin.multiplatform.lib)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("ReluctDatabase") {
            packageName.set("work.racka.reluct.data.source.database")
            srcDirs.setFrom("src/commonMain/sqldelight")
            schemaOutputDirectory.set(
                file("src/commonMain/sqldelight/work/racka/reluct/data/source/database/files")
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

                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                //implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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
    namespace = "work.racka.reluct.data.source"
}
