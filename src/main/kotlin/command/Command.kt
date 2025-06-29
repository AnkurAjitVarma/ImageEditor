package command

fun interface Command {
    suspend fun execute(environment: Environment)
}