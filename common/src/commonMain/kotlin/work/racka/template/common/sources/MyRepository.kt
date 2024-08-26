package work.racka.template.common.sources

interface MyRepository {
    fun helloWorld(): String
}

internal class MyRepositoryImpl(
    private val dbClient: DbClient
) : MyRepository {
    override fun helloWorld(): String {
        return "Hello: ${dbClient.platformName()}"
    }
}
