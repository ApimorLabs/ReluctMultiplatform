package work.racka.reluct.data.model.domain.auth

data class RegisterUser(
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val password: String,
    val repeatPassword: String
)