package work.racka.reluct.data.source.database.models

data class TaskLabelDbObject(
    val id: String,
    val name: String,
    val description: String,
    val colorHexString: String
)
