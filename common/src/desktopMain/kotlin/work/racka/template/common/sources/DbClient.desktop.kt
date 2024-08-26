package work.racka.template.common.sources

internal actual class DbClient {
    actual fun platformName(): String = "Java ${System.getProperty("java.version")}"
}
